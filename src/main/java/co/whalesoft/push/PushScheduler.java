package co.whalesoft.push;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class PushScheduler {

    public static final String FCM_ANDROID_KEY = "AAAAJBqm8-M:APA91bEgVmA6-gxNzbHcr_2ayNmlaI_vG5Qf-3scBe5utL4DZA8BFd0dcKRJQJ635irU3XTzxHQVJxKcYucsTcM_VLkGAhX9aYshr6BIKtYfXWgfvuMj8DPUHjCHWuBOMdx0WChBMbp3";
    public static final String IOS_API_KEY = "AAAAMbL9g6o:APA91bF72Xq1G1d5rP-sPrezWIxu39t5vHDjhqq36sLlWQz9Rvc5Ww5ODWspvQh2dhvBgswD4J0hStzbmYjQ0tNRMcOUFutunfpcHa7CbM0xpDjw0-Xozte-Oh1O6pWy5JlF1VXb-XQn";
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    public static final int THREAD_COUNT = 8;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PushService pushService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void fcmPushServer() throws Exception {
        Push push = new Push();
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Push> fcmListPush = pushService.fcmListMember(push); // 여기에서 push를 보낼 글과 인원을 구함

        if (fcmListPush.size() > 0) {

            pushService.realInsert(fcmListPush);

            List<Push> tokenList = pushService.fcmPushList(push);
            if (tokenList.size() < THREAD_COUNT) {
                pushFCMDataInsert(tokenList);
            } else {
                multiThreadPush(executor, tokenList);
                executor.shutdown();
                while (!executor.awaitTermination(1, TimeUnit.SECONDS));
            }
        } else {
        }
        updatePushSttus(fcmListPush);
    }
    @Scheduled(cron = "0/10 * * * * ?")
    public void fcmResvePushServer() throws Exception {
        Push push = new Push();
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Push> fcmListPush = nullSafeResveList(push); // 여기에서 push를 보낼 글과 인원을 구함
        List<Push> mberTyArrayList = new ArrayList<>();

        if (fcmListPush.size() > 0) {

            for (int i = 0; i < fcmListPush.size(); i++) {
                Push listPush = fcmListPush.get(i);
                if (!listPush.getMber_ty().equals("")) {
                    String[] mberArray = listPush.getMber_ty().split(",");
                    mberTyArrayList = pushService.fcmMberTyResveList(mberArray);
                    fcmListPush.remove(i);
                }
            }
            fcmListPush.addAll(mberTyArrayList);

            pushService.realInsert(fcmListPush);

            List<Push> tokenList = pushService.fcmPushList(push);
            if (tokenList.size() < THREAD_COUNT) {
                pushFCMDataInsert(tokenList);
            } else {
                multiThreadPush(executor, tokenList);
                executor.shutdown();
                while (!executor.awaitTermination(1, TimeUnit.SECONDS))
                    ;
            }
        } else {
//            logger.info("푸시할 글이 없습니다.");
        }
        updatePushSttus(fcmListPush);
    }

    private List<Push> nullSafeResveList(Push push) {
        return Optional.ofNullable(pushService.fcmResveListMember(push))
                       .orElse(Lists.newArrayList());
    }

    private void updatePushSttus(List<Push> fcmListPush) {
        for (Push sentPushList : fcmListPush) {
            pushService.updatePushSttus(sentPushList);
        }
    }

    private void multiThreadPush(ExecutorService executor, List<Push> tokenList) {

        List<List<Push>> listByGuava = Lists.partition(tokenList, tokenList.size() / THREAD_COUNT);
        for (List<Push> subLists : listByGuava) {
            executor.execute(() -> {
                //1초후 실행 -> (실서버)
                try {
                    Thread.sleep(1000);
                    pushFCMDataInsert(subLists);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void pushFCMDataInsert(List<Push> pushDataLists) throws IOException {
        for (Push pushDataList : pushDataLists) {
            if (!pushDataList.getBbs_id().equals("InOut")) {

            String token = pushDataList.getMbr_tkn_value();

            String push_sj = null;
            if (pushDataList.getBbs_id().equals("InOut")) {
                push_sj = pushDataList.getPush_nm();
//                token = pushDataList.getPush_tkn_value();
                token = "c4uB5wloTueN02oDjjRc3B:APA91bFUUU5f7jmpvsxhUE2XPU0BASKxvAao5ak55fvdjvW8g0s9o1w39V2OJtZInTu-z50ggH5GVNRuVRMLisfcAB1XtZSQ9Ht3xsYAd2jXMdzBuq0sUMChpvuCZPgDo2bIunVmV5u6";
            } else {
                push_sj = pushDataList.getPush_sj();
            }
            int push_sn = pushDataList.getFcm_sn();
//            String push_nm = pushDataList.getPush_nm();
            String link = pushDataList.getLink_info();
            int number = pushDataList.getFcm_sn();
            String apiKey = pushDataList.getDevice_se().equals("A") ? FCM_ANDROID_KEY : IOS_API_KEY;

            URL url = new URL(FCM_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            conn.setDoOutput(true);

            Map<String, Object> pushMessage = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            if (pushDataList.getDevice_se().equals("A")) {
                pushMessage.put("to", token);
                pushMessage.put("priority", "high");
                data.put("title", push_sj);
//                data.put("body", push_nm);
                data.put("body", "");
                data.put("number", number);
                data.put("link", link);
//            pushMessage.put("notification", data);
                pushMessage.put("data", data);
            } else {
                Map<String, Object> iosData = new HashMap<>();
                pushMessage.put("to", token);
                pushMessage.put("priority", "high");
                data.put("title", push_sj);
//                data.put("body", push_nm);
                data.put("body", "");
                data.put("number", number);
                data.put("sound", "default");
                data.put("content_available", "true");
                pushMessage.put("notification", data);
                iosData.put("link", link);
                pushMessage.put("data", iosData);
            }

            String pushMessageJson = new Gson().toJson(pushMessage);
            logger.info("json : " + pushMessageJson);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(pushMessageJson);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            logger.info("Response Code : " + responseCode);
            if (responseCode == 200) {
                pushService.updateFcmPushSttus(push_sn);
            }

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                boolean notRegistered = response.toString().contains("NotRegistered");
                if (notRegistered) {
                    pushService.deleteFcmNotRegistered(pushDataList);
                }
                logger.info("response : " + response.toString());
            } catch (NullPointerException e) {
                logger.info("pushFCMDataInsert NullPointerException");
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    logger.info("pushFCMDataInsert IOException");
                }
            }
        }
    }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void pushInsertAfterDeletion() {
        List<Push> unsentPushList = selectUnsentPushList();

        selectRequestListInsert();
        selectSendPushListInsert();
        resetManageTable();
        resetPushUsers(); // fcm 테이블 초기화
        getResetSerial(); // 시리얼 초기화

        if (unsentPushList.size() > 0) {
            // 게시판에 존재하는 데이터 모두 삭제
            pushService.insertUnsentPushList(unsentPushList); // 삭제된 데이터 다시 삽입(보내지 않은 데이터)
        }else {
            logger.info("등록할 글이 없습니다.");

        }
    }

    private void selectRequestListInsert() {
        List<Push> pushRequestList = pushService.selectPushRequestList();
        if (pushRequestList.size() > 0) {
            pushService.insertPushRequestList(pushRequestList);// 여기까지 통계 관련 데이터 Insert
            logger.info("신청 PUSH 통계 테이블 삽입 완료");
        } else {
            logger.info("신청 PUSH 통계 테이블 삽입할 데이터가 없습니다.");
        }
    }

    private void selectSendPushListInsert() {
        List<Push> pushSendList = pushService.selectPushSendList();

        if (pushSendList.size() > 0) {
            pushService.insertPushSendList(pushSendList);// 여기까지 통계 관련 데이터 Insert
            logger.info("발송 PUSH 통계 테이블 삽입 완료");
        } else {
            logger.info("발송 PUSH 통계 테이블 삽입할 데이터가 없습니다.");
        }
    }

    private void resetManageTable() {
        pushService.deleteManageTable();
    }

    private void getResetSerial() {
        pushService.resetSerial();
        logger.info("시리얼 초기화 완료");
    }

    private List<Push> selectUnsentPushList() {
        return pushService.selectUnsentPushList();
    }

    private void resetPushUsers() {
        pushService.deleteFcmUsers();
    }
}
