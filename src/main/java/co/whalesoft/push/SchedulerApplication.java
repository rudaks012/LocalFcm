//package co.whalesoft.push;
//
//import com.google.common.collect.Lists;
//import com.google.gson.Gson;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class SchedulerApplication {
//
//    public static final String API_KEY = "AAAADMrXXXE:APA91bEEhyCxwOHeNBLrebLXOUb1keIuuzx_vnnZrVnGreV0JED-vy9A1LT3NALYxcf1t69tS5RgopVcno9U0oUZ9jy5IHfSkMMICo1p73VDoqoI2dq0mUOfc4XDddlk3bVzgwli6kZB";
//    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
//    public static final int THREAD_COUNT = 8;
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private PushService pushService;
//
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void fcmPushServer( ) throws Exception {
//        Push push = new Push();
//        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
//        List<Push> fcmListPush = pushService.fcmListMember(push); // 여기에서 push를 보낼 글과 인원을 구함
//
//        if (fcmListPush.size() > 0) {
//            pushService.realInsert(fcmListPush);
//
//            List<Push> tokenList = pushService.fcmPushList(push);
//            if (tokenList.size() < THREAD_COUNT) {
//                pushInsert(tokenList);
//            } else {
//                multiThreadPush(executor, tokenList);
//                executor.shutdown();
//                while (!executor.awaitTermination(1, TimeUnit.SECONDS));
//            }
//            deletePushUsers(tokenList);
//        } else {
//            logger.info("푸시할 글이 없습니다.");
//        }
////        updatePushSttus(fcmListPush);
//    }
//    private void deletePushUsers(List<Push> tokenList) {
//        for (Push deleteList : tokenList) {
//            pushService.deleteFcmUsers(deleteList);
//        }
//    }
//    private void updatePushSttus(List<Push> fcmListPush) {
//        for (Push sentPushList : fcmListPush) {
//            pushService.updatePushSttus(sentPushList);
//        }
//    }
//
//    private void multiThreadPush(ExecutorService executor, List<Push> tokenList) {
//
//        List<List<Push>> listByGuava = Lists.partition(tokenList, tokenList.size() / THREAD_COUNT);
//        for (List<Push> subLists : listByGuava) {
//            executor.execute(() -> {
//                //1초후 실행
//                try {
//                    Thread.sleep(1000);
//                    System.out.println("ThreadName() = " + Thread.currentThread().getName());
//                    pushInsert(subLists);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//    }
//
//    private void pushInsert(List<Push> pushDataLists) throws IOException {
//        for (Push pushDataList : pushDataLists) {
//            String token = pushDataList.getMbr_tkn_value();
//            String push_sj = pushDataList.getPush_sj();
//            String push_nm = pushDataList.getPush_nm();
//            String link = pushDataList.getLink_info();
//
//            URL url = new URL(FCM_URL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Authorization", "key=" + API_KEY);
//            conn.setDoOutput(true);
//
////            String PushMessage =
////                "{\"to\": \"" + token + "\",\"priority\" : \"high\",\"data\" :{\"title\" :\"" + push_sj + "\",\"body\" : \"" + push_nm + "\",\"link\" : \"" + link + "\"}}";
//            Map<String, Object> pushMessage = new HashMap<>();
//            pushMessage.put("to", token);
//            pushMessage.put("priority", "high");
//            Map<String, Object> data = new HashMap<>();
//            data.put("title", push_sj);
//            data.put("body", push_nm);
//            data.put("link", link);
////            pushMessage.put("notification", data);
//            pushMessage.put("data", data);
//
//            String pushMessageJson = new Gson().toJson(pushMessage);
//            System.out.println("json = " + pushMessageJson);
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            wr.write(pushMessageJson);
//            wr.flush();
//            wr.close();
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode = " + responseCode);
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            boolean notRegistered = response.toString().contains("NotRegistered");
//
//            if (notRegistered) {
//                pushService.deleteFcmNotRegistered(pushDataList);
//            }
//            System.out.println(response);
//        }
//    }
//
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void deleteTwoDayDataSchedule() {
//        int deleteTwoDayData = pushService.deleteTwoDayData();
//        if (deleteTwoDayData > 0) {
//            logger.info("2일전 데이터 삭제 완료");
//        } else {
//            logger.info("삭제할 데이터가 없습니다.");
//        }
//    }
//}
