//package co.whalesoft.push;
//
//import com.google.common.collect.Lists;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
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
//    public void fcmSelect() throws Exception {
//        Push push = new Push();
//        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
//        List<Push> fcmListPush = pushService.fcmListMember(push); // 여기에서 push를 보낼 글과 인원을 구함
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
//        } else {
//            logger.info("푸시할 글이 없습니다.");
//        }
////        updatePushSttus(fcmListPush);
//
//    }
//
//    private void updatePushSttus(List<Push> fcmListPush) {
//        for (Push sentPushList : fcmListPush) {
//            pushService.updatePushSttus(sentPushList);
//        }
//    }
//
//    private void multiThreadPush(ExecutorService executor, List<Push> tokenList) {
//
//        List<List<Push>> listByGuava = Lists.partition(tokenList, tokenList.size() / THREAD_COUNT);
//        for (List<Push> list : listByGuava) {
//            executor.execute(() -> {
//                //1초후 실행
//                try {
//                    Thread.sleep(1000);
//                    System.out.println("ThreadName() = " + Thread.currentThread().getName());
//                    pushInsert(list);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//    }
//
//    private void pushInsert(List<Push> list) throws IOException {
//        for (int i = 0; i < list.size(); i++) {
//            String token = list.get(i).getMbr_tkn_value();
//            String push_sj = list.get(i).getPush_sj();
//            String push_nm = list.get(i).getPush_nm();
//            String link = list.get(i).getLink_info();
//
//            URL url = new URL(FCM_URL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Authorization", "key=" + API_KEY);
//
//            conn.setDoOutput(true);
//
//            String PushMessage =
//                "{\"to\": \"" + token + "\",\"priority\" : \"high\",\"data\" :{\"title\" :\""
//                    + push_sj + "\",\"body\" : \"" + push_nm + "\",\"link\" : \"" + link + "\"}}";
//
//            OutputStream os = conn.getOutputStream();
//
//            // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
//            os.write(PushMessage.getBytes("UTF-8"));
//            os.flush();
//            os.close();
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + PushMessage);
//            System.out.println("Response Code : " + responseCode);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            // print result
//            System.out.println(response.toString());
//        }
//    }
//
//}
