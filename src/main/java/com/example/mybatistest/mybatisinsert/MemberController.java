package com.example.mybatistest.mybatisinsert;

import com.example.mybatistest.mybatisinsert.util.JsonResponse;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {
    public static final String PARAMETER_SW_ONE = "1";
    public static final String PARAMETER_SW_TWO = "2";
    public static final String PARAMETER_SW_THREE = "3";
    public static final String REGULAR_EXPRESSION = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
    public static final String PUSH_EXPRESSION = "\\^";
    public static final int ZERO = 0;
    public static final int THREAD_COUNT = 8;
    @Autowired
    private MybatisInsertService mybatisInsertService;

    @RequestMapping(value = {"/token"}, method = {RequestMethod.POST})
    public @ResponseBody JsonResponse token(@RequestBody Map<String, String> param, Member member,
        BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        try {
            if (!result.hasErrors()) {
                setMemberStatus(param, member);
                swForEachFunction(member, result, res);// sw값에 따른 분기처리 sw = 1 : 신규, sw = 2 : 업데이트, sw = 3 : 신규(토큰값 있는데 변경된 경우)
                res.setUrl(member.getMbr_token());
                restSetOkMessage(res);
            } else {
                restSetFalseMessage(result, res);
            }
        } catch (Exception e) {
            restSetFalseMessage(result, res);
        }

        return res;
    }


    @RequestMapping(value = "/gubun", method = {RequestMethod.POST})
    public @ResponseBody JsonResponse gubun(@RequestBody Map<String, String> param, Member member,
        BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        if (!result.hasErrors()) {
            gubunMemberStatus(param, member);
            mybatisInsertService.fcmDeleteGubun(member); //등록하기전 삭제 후 등록

            String[] splitRegular = member.getPush().split(PUSH_EXPRESSION);
            for (String value : splitRegular) {
                String[] push = value.split(REGULAR_EXPRESSION);
                splitGubunMemberStatusInsert(member, result, res, push); // 구분자로 구분 후 insert
            }
            res.setUrl(member.getMbr_token());
            restSetOkMessage(res);
        } else {
            restSetFalseMessage(result, res);
        }
        return res;
    }

    @GetMapping(value = "/fcmTest")
    public List<Member> fcmSelect(Member member) {
        List<Member> fcmListMember = mybatisInsertService.fcmListMember(member); // 여기에서 push를 보낼 글과 인원을 구함
        System.out.println("fcmListMember = " + fcmListMember);
        int a = mybatisInsertService.realInsert(fcmListMember);
        System.out.println("a = " + a);

        return fcmListMember;
    }


    @RequestMapping(value = "/sendFCM")
    public String index(Member member) throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Member> tokenList = mybatisInsertService.fcmPushList(member);
        if (tokenList.size() < THREAD_COUNT) {
            pushInsert(tokenList);
        }else {
            multiThreadPush(executor, tokenList);
            executor.shutdown();
            while (!executor.awaitTermination(1, TimeUnit.SECONDS));
            executor.shutdownNow();
        }

        return "jsonView";
    }

    private void multiThreadPush(ExecutorService executor, List<Member> tokenList) {
        List<List<Member>> listByGuava = Lists.partition(tokenList, tokenList.size() / THREAD_COUNT);
        for (List<Member> list : listByGuava) {
            executor.execute(() -> {
                try {
                    pushInsert(list);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void pushInsert(List<Member> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i+"번째");
            String token = list.get(i).getMbr_token();
            String push_sj = list.get(i).getPush_sj();
            String push_nm = list.get(i).getPush_nm();
            String link = list.get(i).getLink();

            final String apiKey = "AAAADMrXXXE:APA91bEEhyCxwOHeNBLrebLXOUb1keIuuzx_vnnZrVnGreV0JED-vy9A1LT3NALYxcf1t69tS5RgopVcno9U0oUZ9jy5IHfSkMMICo1p73VDoqoI2dq0mUOfc4XDddlk3bVzgwli6kZB";
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);

            conn.setDoOutput(true);

            // 이걸로 보내면 특정 토큰을 가지고있는 어플에만 알림을 날려준다  위에 둘중에 한개 골라서 날려주자
            String input = "{\"notification\":{\"title\":\""+push_sj+"\",\"body\":\""+push_nm+"\",\"link\":\""+link+"\"},\"to\":\"" + token + "\"}";
            //input custom data
//            String input = "{\"notification\":{\"title\":\""+push_sj+"\",\"body\":\""+push_nm+"\",\"click_action\":\""+link+"\"},\"custom_data\":{\"click_action\":\""+link+"\"},\"to\":\"" + token + "\"}";
//            String input = "{\"notification\":{\"title\":\""+push_sj+"\",\"body\":\""+push_nm+"\",\"click_action\":\""+link+"\"},\"data\":{\"click_action\":\""+link+"\"},\"to\":\"" + token + "\"}";
//            String input = "{\"notification\":{\"title\":\""+push_sj+"\",\"body\":\""+push_nm+"\",\"custom\":\""+link+"\"},\"to\":\"/topics/all\"}";
//            String input = "{\"notification\":{\"title\":\""+push_sj+"\",\"body\":\""+push_nm+"\",\"click_action\":\""+link+"\"},\"to\":\"\" + token + \"\"}";

            OutputStream os = conn.getOutputStream();

            // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
            os.write(input.getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + input);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        }
    }


    private void swForEachFunction(Member member, BindingResult result, JsonResponse res) {

        validate(member, result, res);

        if (PARAMETER_SW_ONE.equals(member.getSw())) {
            int worksNormally = mybatisInsertService.fcmInsertPost(member);
            checkWorksStatus(result, res, worksNormally);
        }
        if (PARAMETER_SW_TWO.equals(member.getSw())) {
            int worksNormally = mybatisInsertService.fcmUpdatePost(member);
            checkWorksStatus(result, res, worksNormally);
        }
        if (PARAMETER_SW_THREE.equals(member.getSw()) && isTokenCheck(member)) {
            int worksNormally = mybatisInsertService.fcmDuplicatedTokenUpdate(member);
            int duplicateInsert = mybatisInsertService.fcmInsertPost(member);
            checkWorksStatus(result, res, worksNormally, duplicateInsert);
        }
    }

    private void validate(Member member, BindingResult result, JsonResponse res) {
        if (isSwEmptyAndNull(member)) {
            restSetFalseMessage(result, res);
        }
        if (isTokenEmptyAndNull(member)) {
            restSetFalseMessage(result, res);
        }
    }

    private boolean isTokenEmptyAndNull(Member member) {
        return Objects.isNull(member.getMbr_token()) || member.getMbr_token().isEmpty();
    }

    private boolean isSwEmptyAndNull(Member member) {
        return member.getSw().isEmpty() && member.getSw() == null;
    }


    private void setMemberStatus(Map<String, String> param, Member member) {
        member.setMbr_id(param.get("mbr_id"));
        member.setMbr_nm(param.get("mbr_nm"));
        member.setMbr_token(param.get("mbr_token"));
        member.setOld_token(param.get("old_token"));
        member.setSw(param.get("sw"));
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

    private boolean isTokenCheck(Member member) {
        return member.getOld_token() != null && !Objects.equals(member.getOld_token(), "");
    }

    private void splitGubunMemberStatusInsert(Member member, BindingResult result, JsonResponse res,
        String[] push) {
        for (int j = ZERO; j < push.length; j++) {
            if (j == ZERO) {
                member.setSys_id(push[j]);
            } else if (j == 1) {
                continue;
            } else if (isEven(j)) {
                member.setBbs_id(push[j]);
            } else {
                member.setPush_yn(push[j]);
                int worksNormally = mybatisInsertService.fcmGubunInsert(member);
                checkWorksStatus(result, res, worksNormally);
            }
        }
    }

    private boolean isEven(int j) {
        return j % 2 == 0;
    }

    private void gubunMemberStatus(Map<String, String> param, Member member) {
        member.setMbr_token(param.get("mbr_token"));
        System.out.println("member.getMbr_token() = " + member.getMbr_token());
        member.setPush(param.get("push"));
        System.out.println("member.getPush() = " + member.getPush());
        member.setSw(param.get("sw"));
        System.out.println("member.getSw() = " + member.getSw());
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
