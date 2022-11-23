package co.whalesoft.push;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PushController {

    public static final String PARAMETER_SW_ONE = "1";
    public static final String PARAMETER_SW_TWO = "2";
    public static final String PARAMETER_SW_THREE = "3";
    public static final String REGULAR_EXPRESSION = "[^\uAC00-\uD7A30-9a-zA-Z()·/\\-\\s]";
    public static final String PUSH_EXPRESSION = "\\^";
    public static final String API_KEY = "AAAADMrXXXE:APA91bEEhyCxwOHeNBLrebLXOUb1keIuuzx_vnnZrVnGreV0JED-vy9A1LT3NALYxcf1t69tS5RgopVcno9U0oUZ9jy5IHfSkMMICo1p73VDoqoI2dq0mUOfc4XDddlk3bVzgwli6kZB";
    public static final String IOS_API_KEY = "AAAAcWUn1bA:APA91bHgZSuVe9pHZ9N_-wllSjdeJUBe66s8utnELwdvUgg2Vb7N1WMIDL9cGs00nyekYQeVgH5Yqbq3GqLvQAVEA-hjoZWDZLoMm9CmQS5QUtuniYypKCPKAnbqh_nR9mIzc2879Rtc";
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    public static final int ZERO = 0;
    public static final int THREAD_COUNT = 8;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PushService pushService;

//    @RequestMapping(value = {"/push/edunavi/am/token.do"}, method = {RequestMethod.POST})
//    public @ResponseBody JsonResponse insertToken(@RequestBody Map<String, String> param, Push push, BindingResult result, HttpServletRequest request) {
//
//        JsonResponse res = new JsonResponse(request);
//        //IOException 설정
//        try {
//            if (!result.hasErrors()) {
//                setMemberStatus(param, push);
//                swForEachFunction(push, result, res);// sw값에 따른 분기처리 sw = 1 : 신규, sw = 2 : 업데이트, sw = 3 : 신규(토큰값 있는데 변경된 경우)
//                res.setUrl(push.getPush_tkn_value());
//                restSetOkMessage(res);
//            } else {
//                restSetFalseMessage(result, res);
//            }
//        } catch (NullPointerException e) {
//            restSetFalseMessage(result, res);
//        }
//        return res;
//    }
//
//    @RequestMapping(value = "/push/edunavi/am/gubun.do", method = {RequestMethod.POST})
//    public @ResponseBody JsonResponse insertCheckGubun(@RequestBody Map<String, String> param, Push push, BindingResult result, HttpServletRequest request) {
//        JsonResponse res = new JsonResponse(request);
//
//        if (!result.hasErrors()) {
//            gubunMemberStatus(param, push);
//            pushService.deleteGubunMember(push); //등록하기전 삭제 후 등록
//
//            String[] splitRegular = push.getPush().split(PUSH_EXPRESSION);
//            for (String value : splitRegular) {
//                String[] splitPush = value.split(REGULAR_EXPRESSION);
//                splitGubunMemberStatusInsert(push, splitPush, result, res ); // 구분자로 구분 후 insert
//            }
////            res.setUrl(push.getPush_tkn_value());
//            restSetOkMessage(res);
//        } else {
//            restSetFalseMessage(result, res);
//        }
//        return res;
//    }
    //    private void swForEachFunction(Push push, BindingResult result, JsonResponse res) {
//        System.out.println("push = " + push);
//
//        validate(push, result, res);
//
//        if (PARAMETER_SW_ONE.equals(push.getSw())) {
//            int worksNormally = pushService.insertMberTkn(push);
//            checkWorksStatus(result, res, worksNormally);
//        }
//        if (PARAMETER_SW_TWO.equals(push.getSw())) {
//            int worksNormally = pushService.updateMberTkn(push);
//            checkWorksStatus(result, res, worksNormally);
//        }
//        if (PARAMETER_SW_THREE.equals(push.getSw()) && isTokenCheck(push)) {
//            int worksNormally = pushService.updateDplcteMberTkn(push);
//            int duplicateInsert = pushService.insertMberTkn(push);
//            checkWorksStatus(result, res, worksNormally, duplicateInsert);
//        }
//    }

//    private void validate(Push push, BindingResult result, JsonResponse res) {
//        if (isSwEmptyAndNull(push)) {
//            restSetFalseMessage(result, res);
//        }
//        if (isTokenEmptyAndNull(push)) {
//            restSetFalseMessage(result, res);
//        }
//    }

//    private boolean isTokenEmptyAndNull(Push push) {
//        return Objects.isNull(push.getPush_tkn_value()) || push.getPush_tkn_value().isEmpty();
//    }
//
//    private boolean isSwEmptyAndNull(Push push) {
//        return push.getSw().isEmpty() && push.getSw() == null;
//    }
//
//    private void setMemberStatus(Map<String, String> param, Push push) {
//        push.setMber_id(param.get("mbr_id"));
//        System.out.println("param = " + param);
//        push.setMber_nm(param.get("mbr_nm"));
//        push.setMbr_tkn_value(param.get("mbr_token"));
//        push.setOld_token(param.get("old_token"));
//        push.setSw(param.get("sw"));
//        push.setDevice_se(param.get("gubun"));
//    }

//    private void checkWorksStatus(BindingResult result, JsonResponse res, int worksNormally) {
//        if (worksNormally == 0) {
//            restSetFalseMessage(result, res);
//        } else {
//            restSetOkMessage(res);
//        }
//    }
//
//    private void checkWorksStatus(BindingResult result, JsonResponse res, int worksNormally,
//        int duplicateInsert) {
//        if (worksNormally == 0 && duplicateInsert == 0) {
//            restSetFalseMessage(result, res);
//        } else {
//            restSetOkMessage(res);
//        }
//    }
//
//    private boolean isTokenCheck(Push push) {
//        return push.getOld_token() != null && !Objects.equals(push.getOld_token(), "");
//    }

//    private void splitGubunMemberStatusInsert(Push member, String[] splitPush, BindingResult result, JsonResponse res) {
//        for (int j = ZERO; j < splitPush.length; j++) {
//            if (j == ZERO) {
//                member.setSys_id(splitPush[j]);
//            } else if (j == 1) {
//                member.setSys_nm(splitPush[j]);
//            } else if (isEven(j)) {
//                if (splitPush[j].equals("EXPRN")||splitPush[j].equals("EDU")||splitPush[j].equals("PBLPRFR")) {
//                    member.setBbs_id("0");
//                    member.setResve_reqst_ty(splitPush[j]);
//                }else {
//                    member.setBbs_id(splitPush[j]);
//                }
//            } else {
//                member.setPush_at(splitPush[j]);
//                int worksNormally = pushService.fcmGubunInsert(member);
//                checkWorksStatus(result, res, worksNormally);
//            }
//        }
//    }

//    private boolean isEven(int j) {
//        return j % 2 == 0;
//    }
//
//    private void gubunMemberStatus(Map<String, String> param, Push push) {
//        push.setMbr_tkn_value(param.get("mbr_token"));
//        push.setPush(param.get("push"));
//        push.setSw(param.get("sw"));
//    }

//    private void restSetOkMessage(JsonResponse res) {
//        res.setValid(true);
//        res.setMessage("OK");
//    }
//
//    private void restSetFalseMessage(BindingResult result, JsonResponse res) {
//        res.setValid(false);
//        res.setMessage("Fault");
//        res.setResult(result.getAllErrors());
//    }

    @GetMapping(value = "/push/edunavi/am/send.do")
    public void fcmPushServer(Push push) throws Exception {
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
            logger.info("푸시할 글이 없습니다.");
        }
        updatePushSttus(fcmListPush);
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
                    pushFCMDataInsert(subLists);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void pushFCMDataInsert(List<Push> pushDataLists) throws IOException {
        for (Push pushDataList : pushDataLists) {
            String token = pushDataList.getMbr_tkn_value();
            String push_sj = null;
            if (pushDataList.getBbs_id().equals("InOut")) {
                push_sj = pushDataList.getPush_nm();
            }else {
                push_sj = pushDataList.getPush_sj();
            }
            int push_sn = pushDataList.getFcm_sn();
//            String push_nm = pushDataList.getPush_nm();
            String link = pushDataList.getLink_info();
            String apiKey = pushDataList.getDevice_se().equals("A") ? API_KEY : IOS_API_KEY;

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



//    삭제 관련 메서드

@GetMapping(value = "/push/edunavi/am/send1.do")
    public void pushInsertAfterDeletion() {
        List<Push> unsentPushList = selectUnsentPushList();

    selectRequestListInsert();
    selectSendPushListInsert();
    logger.info("통계 테이블 삽입 완료");
    resetManageTable();
    resetPushUsers(); // fcm 테이블 초기화
    logger.info("푸시 테이블 초기화 완료");
    getResetSerial(); // 시리얼 초기화
    logger.info("시리얼 초기화 완료");
        if (unsentPushList.size() > 0) {
//            selectRequestListInsert();  // 1. 통계 테이블 삽입(발송)
//            selectSendPushListInsert();
             // 게시판에 존재하는 데이터 모두 삭제
            pushService.insertUnsentPushList(unsentPushList); // 삭제된 데이터 다시 삽입(보내지 않은 데이터)
        }else {
            logger.info("등록할 글이 없습니다.");
        }
//        deletePushUsers(); // fcm 테이블 초기화
//        getResetSerial(); // 시리얼 초기화
    }

    private void selectRequestListInsert() {
        List<Push> pushRequestList = pushService.selectPushRequestList();
        pushService.insertPushRequestList(pushRequestList);  // 여기까지 통계 관련 데이터 Insert
    }

    private void selectSendPushListInsert() {
        List<Push> pushSendList = pushService.selectPushSendList();
        pushService.insertPushSendList(pushSendList);
    }

    private void resetManageTable() {
        pushService.deleteManageTable();
    }

    private void getResetSerial() {
        pushService.resetSerial();
    }

    private List<Push> selectUnsentPushList() {
        return pushService.selectUnsentPushList();
    }

    private void resetPushUsers() {
            pushService.deleteFcmUsers();
    }
}
