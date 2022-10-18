package co.whalesoft.push;

import co.whalesoft.util.JsonResponse;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PushController {

    public static final String PARAMETER_SW_ONE = "1";
    public static final String PARAMETER_SW_TWO = "2";
    public static final String PARAMETER_SW_THREE = "3";
    public static final String REGULAR_EXPRESSION = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
    public static final String PUSH_EXPRESSION = "\\^";
    public static final String API_KEY = "AAAADMrXXXE:APA91bEEhyCxwOHeNBLrebLXOUb1keIuuzx_vnnZrVnGreV0JED-vy9A1LT3NALYxcf1t69tS5RgopVcno9U0oUZ9jy5IHfSkMMICo1p73VDoqoI2dq0mUOfc4XDddlk3bVzgwli6kZB";
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    public static final int ZERO = 0;
    public static final int THREAD_COUNT = 8;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PushService pushService;

    @RequestMapping(value = {"/push/edunavi/am/token.do"}, method = {RequestMethod.POST})
    public @ResponseBody JsonResponse insertToken(@RequestBody Map<String, String> param, Push push, BindingResult result, HttpServletRequest request) {

        JsonResponse res = new JsonResponse(request);

        try {
            if (!result.hasErrors()) {
                setMemberStatus(param, push);
                swForEachFunction(push, result, res);// sw값에 따른 분기처리 sw = 1 : 신규, sw = 2 : 업데이트, sw = 3 : 신규(토큰값 있는데 변경된 경우)
                res.setUrl(push.getPush_tkn_value());
                restSetOkMessage(res);
            } else {
                restSetFalseMessage(result, res);
            }
        } catch (Exception e) {
            restSetFalseMessage(result, res);
        }

        return res;
    }

    @RequestMapping(value = "/push/edunavi/am/gubun.do", method = {RequestMethod.POST})
    public @ResponseBody JsonResponse insertCheckGubun(@RequestBody Map<String, String> param, Push push, BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        if (!result.hasErrors()) {
            gubunMemberStatus(param, push);
            pushService.deleteGubunMember(push); //등록하기전 삭제 후 등록

            String[] splitRegular = push.getPush().split(PUSH_EXPRESSION);
            for (String value : splitRegular) {
                String[] splitPush = value.split(REGULAR_EXPRESSION);
                splitGubunMemberStatusInsert(push, splitPush, result, res ); // 구분자로 구분 후 insert
            }
            res.setUrl(push.getPush_tkn_value());
            restSetOkMessage(res);
        } else {
            restSetFalseMessage(result, res);
        }
        return res;
    }

    @GetMapping(value = "/push/edunavi/am/send.do")
    public String fcmPushServer(Push push) throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Push> fcmListPush = pushService.fcmListMember(push); // 여기에서 push를 보낼 글과 인원을 구함

        if (fcmListPush.size() > 0) {
            pushService.realInsert(fcmListPush);

            List<Push> tokenList = pushService.fcmPushList(push);
            if (tokenList.size() < THREAD_COUNT) {
                pushInsert(tokenList);
            } else {
                multiThreadPush(executor, tokenList);
                executor.shutdown();
                while (!executor.awaitTermination(1, TimeUnit.SECONDS));
            }
            deletePushUsers(tokenList);
        } else {
            logger.info("푸시할 글이 없습니다.");
        }
//        updatePushSttus(fcmListPush);


        return "jsonView";
    }

    private void deletePushUsers(List<Push> tokenList) {
        for (Push deleteList : tokenList) {
            pushService.deleteFcmUsers(deleteList);
        }
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
                //1초후 실행
                try {
                    Thread.sleep(1000);
                    System.out.println("ThreadName() = " + Thread.currentThread().getName());
                    pushInsert(subLists);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void pushInsert(List<Push> pushDataLists) throws IOException {
        for (Push pushDataList : pushDataLists) {
            String token = pushDataList.getMbr_tkn_value();
            String push_sj = pushDataList.getPush_sj();
            String push_nm = pushDataList.getPush_nm();
            String link = pushDataList.getLink_info();

            URL url = new URL(FCM_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setDoOutput(true);

//            String PushMessage =
//                "{\"to\": \"" + token + "\",\"priority\" : \"high\",\"data\" :{\"title\" :\"" + push_sj + "\",\"body\" : \"" + push_nm + "\",\"link\" : \"" + link + "\"}}";
            Map<String, Object> pushMessage = new HashMap<>();
            pushMessage.put("to", token);
            pushMessage.put("priority", "high");
            Map<String, Object> data = new HashMap<>();
            data.put("title", push_sj);
            data.put("body", push_nm);
            data.put("link", link);
//            pushMessage.put("notification", data);
            pushMessage.put("data", data);

            String pushMessageJson = new Gson().toJson(pushMessage);
            System.out.println("json = " + pushMessageJson);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(pushMessageJson);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("response = " + response.toString());
//
//            OutputStream os = conn.getOutputStream();
//
//            // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
//            os.write(PushMessage.getBytes(StandardCharsets.UTF_8));
//            os.flush();
//            os.close();

//            int responseCode = conn.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Post parameters : " + PushMessage);
//            System.out.println("Response Code : " + responseCode);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();

//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
            // print result
            System.out.println(response);
        }
    }

    private void swForEachFunction(Push push, BindingResult result, JsonResponse res) {

        validate(push, result, res);

        if (PARAMETER_SW_ONE.equals(push.getSw())) {
            int worksNormally = pushService.insertMberTkn(push);
            checkWorksStatus(result, res, worksNormally);
        }
        if (PARAMETER_SW_TWO.equals(push.getSw())) {
            int worksNormally = pushService.updateMberTkn(push);
            checkWorksStatus(result, res, worksNormally);
        }
        if (PARAMETER_SW_THREE.equals(push.getSw()) && isTokenCheck(push)) {
            int worksNormally = pushService.updateDplcteMberTkn(push);
            int duplicateInsert = pushService.insertMberTkn(push);
            checkWorksStatus(result, res, worksNormally, duplicateInsert);
        }
    }

    private void validate(Push push, BindingResult result, JsonResponse res) {
        if (isSwEmptyAndNull(push)) {
            restSetFalseMessage(result, res);
        }
        if (isTokenEmptyAndNull(push)) {
            restSetFalseMessage(result, res);
        }
    }

    private boolean isTokenEmptyAndNull(Push push) {
        return Objects.isNull(push.getPush_tkn_value()) || push.getPush_tkn_value().isEmpty();
    }

    private boolean isSwEmptyAndNull(Push push) {
        return push.getSw().isEmpty() && push.getSw() == null;
    }

    private void setMemberStatus(Map<String, String> param, Push push) {
        push.setMbr_id(param.get("mbr_id"));
        push.setMbr_nm(param.get("mbr_nm"));
        push.setMbr_tkn_value(param.get("mbr_token"));
        System.out.println("push.getMbr_tkn_value() = " + push.getMbr_tkn_value());
        push.setOld_token(param.get("old_token"));
        System.out.println("push.getOld_token() = " + push.getOld_token());
        push.setSw(param.get("sw"));
        System.out.println("push.getSw() = " + push.getSw());
    }

    private void checkWorksStatus(BindingResult result, JsonResponse res, int worksNormally) {
        if (worksNormally == 0) {
            restSetFalseMessage(result, res);
        } else {
            restSetOkMessage(res);
        }
    }

    private void checkWorksStatus(BindingResult result, JsonResponse res, int worksNormally,
        int duplicateInsert) {
        if (worksNormally == 0 && duplicateInsert == 0) {
            restSetFalseMessage(result, res);
        } else {
            restSetOkMessage(res);
        }
    }

    private boolean isTokenCheck(Push push) {
        return push.getOld_token() != null && !Objects.equals(push.getOld_token(), "");
    }

    private void splitGubunMemberStatusInsert(Push member, String[] splitPush, BindingResult result, JsonResponse res) {
        for (int j = ZERO; j < splitPush.length; j++) {
            if (j == ZERO) {
                member.setSys_id(splitPush[j]);
            } else if (j == 1) {
                continue;
            } else if (isEven(j)) {
                member.setBbs_id(splitPush[j]);
            } else {
                member.setPush_at(splitPush[j]);
                int worksNormally = pushService.fcmGubunInsert(member);
                checkWorksStatus(result, res, worksNormally);
            }
        }
    }

    private boolean isEven(int j) {
        return j % 2 == 0;
    }

    private void gubunMemberStatus(Map<String, String> param, Push push) {
        push.setMbr_tkn_value(param.get("mbr_token"));
        push.setPush(param.get("push"));
        push.setSw(param.get("sw"));
    }

    private void restSetOkMessage(JsonResponse res) {
        res.setValid(true);
        res.setMessage("OK");
    }

    private void restSetFalseMessage(BindingResult result, JsonResponse res) {
        res.setValid(false);
        res.setMessage("Fault");
        res.setResult(result.getAllErrors());
    }
}
