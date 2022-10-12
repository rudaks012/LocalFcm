package co.whalesoft.alrim;

import co.whalesoft.YongjuStopWatch;
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
public class AlrimController {

    private static Long idx = 1L;
    private static final int THREAD_COUNT = 8;
    @Autowired
    private AlrimService alrimService;

    @RequestMapping("/alrim/ad/edunavi/alrim/informForPortal.do")
    public @ResponseBody Map<String, Object> select() throws InterruptedException {
        Map<String, Object> rtnObj = new HashMap<>();

        StopWatch beforeStopWatch = new YongjuStopWatch();
        beforeStopWatch.start("before");

        List<Alrim> selectRegisteredPost = alrimService.selectRegisteredPost();

        List<List<Alrim>> targets = findMembers(selectRegisteredPost);
        beforeStopWatch.stop();
        System.out.println(beforeStopWatch.prettyPrint());

        StopWatch stopWatch = new YongjuStopWatch();

        System.out.println("=================================================");
        System.out.println("============== 자 이제 시작 합니다. ================");
        System.out.println("=================================================");

        int i = 1;

        for (List<Alrim> target : targets) {
            stopWatch.start((i++) + "번째");

            allimInsertForPortal(target);

            stopWatch.stop();

            System.out.println(stopWatch.prettyPrint());
        }

        System.out.println("=============================================");
        System.out.println("============== 종료 되었습니다 ================");
        System.out.println("=============================================");

        rtnObj.put("Select ", selectRegisteredPost);

//        mybatisInsertService.informDelete();
        updateNtcnSttus(selectRegisteredPost);

        return rtnObj;
    }

    private void updateNtcnSttus(List<Alrim> selectRegisteredPost) {
        for (Alrim alrim : selectRegisteredPost) {
            alrimService.updateNtcnSttus(alrim);
        }
    }

    private List<List<Alrim>> findMembers(List<Alrim> targetList) {

        Alrim alrim = new Alrim();

        List<List<Alrim>> insertList = new ArrayList<>();
        List<Alrim> memberList = new ArrayList<>();

        long totalMembers = 0;
        for (Alrim members : targetList) {
            alrim.setAdminist_code(members.getAdminist_code());
            alrim.setSys_id(members.getSys_id());
            alrim.setBbs_id(members.getBbs_id());
            alrim.setChnnl_sn(members.getChnnl_sn());

            if (alrim.getSys_id().equals("arc")) {
                memberList = alrimService.selectArcTarget(alrim);
            } else { // 포탈일 경우
                memberList = alrimService.selectInfoTarget(alrim);
            }

            for (Alrim value : memberList) {
                value.setIdx(idx++);
            }

            insertList.add(memberList);
            totalMembers += memberList.size();
        }
        System.out.println("totalMembers = " + totalMembers);
        return insertList;
    }

    private void allimInsertForPortal(List<Alrim> insertList) throws InterruptedException {

        if (insertList.size() < THREAD_COUNT) {
            allimInsert(insertList);
        } else {
            final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            List<List<Alrim>> listByGuava = Lists.partition(insertList, insertList.size() / THREAD_COUNT);

            for (List<Alrim> standard : listByGuava) {
                executor.execute(() -> allimInsert(standard));
            }
            executor.shutdown();

            while (!executor.awaitTermination(1, TimeUnit.SECONDS));

            executor.shutdownNow();
        }

    }

    private void allimInsert(List<Alrim> standard) {
        if (standard.size() < 100) {
            alrimService.insertInfoTarget(standard);
        } else {
            List<List<Alrim>> insertsList = Lists.partition(standard, standard.size() / 100);
            for (List<Alrim> alrims : insertsList) {
                alrimService.insertInfoTarget(alrims);
            }
        }
    }
}
