package com.example.mybatistest.mybatisinsert;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MybatisInsertController {

    private static Long idx = 1L;
    private static final int THREAD_COUNT = 8;
    @Autowired
    private MybatisInsertService mybatisInsertService;

    @RequestMapping("/alrim/ad/edunavi/alrim/informForPortal.do")
    public @ResponseBody Map<String, Object> select() throws InterruptedException {
        Map<String, Object> rtnObj = new HashMap<>();

        StopWatch beforeStopWatch = new YongjuStopWatch();
        beforeStopWatch.start("before");

        List<Allim> selectTest = mybatisInsertService.select();

        List<List<Allim>> targets = findMembers(selectTest);
        beforeStopWatch.stop();
        System.out.println(beforeStopWatch.prettyPrint());

        StopWatch stopWatch = new YongjuStopWatch();

        System.out.println("=================================================");
        System.out.println("============== 자 이제 시작 합니다. ================");
        System.out.println("=================================================");

        int i = 1;

        for (List<Allim> target : targets) {
            stopWatch.start((i++) + "번째");

            allimInsertForPortal(target);

            stopWatch.stop();

            System.out.println(stopWatch.prettyPrint());
        }

        System.out.println("=============================================");
        System.out.println("============== 종료 되었습니다 ================");
        System.out.println("=============================================");

        rtnObj.put("Select ", selectTest);


//        mybatisInsertService.delete();

        return rtnObj;
    }

    private List<List<Allim>> findMembers(List<Allim> selectTest) {

        Allim allim = new Allim();

        List<List<Allim>> insertList = new ArrayList<>();

        long totalMembers = 0;
        for (Allim members : selectTest) {
            allim.setOrg_code(members.getOrg_code());
            allim.setNotice_code(members.getNotice_code());

            List<Allim> memberList = mybatisInsertService.findMembers(allim);
            for (Allim value : memberList) {
                value.setIdx(idx++);
            }

            insertList.add(memberList);
            totalMembers += memberList.size();
        }
        System.out.println("totalMembers = " + totalMembers);
        return insertList;
    }

    private void allimInsertForPortal(List<Allim> insertList) throws InterruptedException {

        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        List<List<Allim>> listByGuava = Lists.partition(insertList, insertList.size() / THREAD_COUNT);

            for (List<Allim> standard : listByGuava) {
                executor.execute(() -> allimInsert(standard));
            }
            executor.shutdown();

        while (!executor.awaitTermination(1, TimeUnit.SECONDS));

        executor.shutdownNow();
    }

    private void allimInsert(List<Allim> standard) {
        List<List<Allim>> insertsList = Lists.partition(standard, standard.size() / 100);
        for (List<Allim> allims : insertsList) {
            mybatisInsertService.insert(allims);
        }
    }
}
