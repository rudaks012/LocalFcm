package com.example.mybatistest.mybatisinsert;

import com.example.mybatistest.mybatisinsert.util.JsonResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private MybatisInsertService mybatisInsertService;

    @GetMapping("/member")
    public List<Member> listToJson() {
        Member member = new Member();
        Gson gson = new Gson();

        List<Member> listToJson = mybatisInsertService.listToJson(member);
        String json = gson.toJson(listToJson);

        Type type = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        List<Map<String, String>> deserialize = gson.fromJson(json, type);
        for (Map<String, String> stringStringMap : deserialize) {
            if (!stringStringMap.containsKey("mbr_token")) {
                mybatisInsertService.fcmJsonInsertGubun(stringStringMap);
            } else {
                mybatisInsertService.fcmInsertUser(stringStringMap);

            }
        }
        return listToJson;
    }

    @RequestMapping(value = {"/token"}, method = {RequestMethod.POST})
    public @ResponseBody JsonResponse token(@RequestBody Map<String, String> param, Member member,
        BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        if (!result.hasErrors()) {
            setMemberStatus(param, member);

            if ("1".equals(member.getSw())) {
                int worksNormally = mybatisInsertService.fcmInsertPost(member);
                checkWorksStatus(result, res, worksNormally);
            } else if ("2".equals(member.getSw())) {
                int worksNormally = mybatisInsertService.fcmUpdatePost(member);
                checkWorksStatus(result, res, worksNormally);
            } else if ("3".equals(member.getSw()) && isTokenCheck(member)) {
                int worksNormally = mybatisInsertService.fcmDuplicatedTokenUpdate(member);
                int duplicateInsert = mybatisInsertService.fcmInsertPost(member);
                checkWorksStatus(result, res, worksNormally, duplicateInsert);
            }
            res.setUrl(member.getMbr_token());
            restSetOkMessage(res);
        } else {
            restSetFalseMessage(result, res);
        }
        return res;
    }


    @RequestMapping(value = "/gubun", method = {RequestMethod.POST})
    public @ResponseBody JsonResponse gubun(@RequestBody Map<String, String> param, Member member,
        BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        if (!result.hasErrors()) {
            member.setMbr_token(param.get("mbr_token"));
            member.setPush(param.get("push"));
            member.setSw(param.get("sw"));
            System.out.println("member = " + member.getPush());

            String regularExpression = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
            String[] splitRegular = member.getPush().split("\\^");
            System.out.println("splitPush = " + Arrays.toString(splitRegular));

            for (String value : splitRegular) {
                String[] push = value.split(regularExpression);
                System.out.println("push = " + Arrays.toString(push));
                for (int j = 0; j < push.length; j++) {
                    if (j == 0) {
                        System.out.println("0 입니다" + push[j] + "  " + j);
                        member.setSys_id(push[j]);
                    } else if (j == 1) {
                        continue;
                    } else if (j % 2 == 0) {
                        System.out.println("짝수 입니다" + push[j] + "  " + j);
                        member.setBbs_id(push[j]);
                    } else if (j % 3 == 0) {
                        System.out.println("홀수 입니다" + push[j] + "  " + j);
                        member.setPush_yn(push[j]);
                    } else {
                        res.setValid(false);
                        res.setMessage("Fault");
                        res.setResult(result.getAllErrors());
                    }
                    //insert 구문 붙인다
                }
            }

            System.out.println("splitPush.length = " + splitRegular.length);

            res.setUrl(member.getMbr_token());
            res.setValid(true);
            res.setMessage("OK");

        } else {
            res.setValid(false);
            res.setMessage("Fault");
            res.setResult(result.getAllErrors());
        }
        return res;
    }

    private void setMemberStatus(Map<String, String> param, Member member) {
        member.setMbr_id(param.get("mbr_id"));
        member.setMbr_nm(param.get("mbr_nm"));
        member.setMbr_token(param.get("mbr_token"));
        member.setOld_token(param.get("old_token"));
        member.setSw(param.get("sw"));

        System.out.println("param = " + param.get("mbr_id"));
        System.out.println("param = " + param.get("mbr_nm"));
        System.out.println("param = " + param.get("mbr_token"));
        System.out.println("param = " + param.get("old_token"));
        System.out.println("param = " + param.get("sw"));
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
        return member.getOld_token() != null && member.getOld_token() != "";
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

    @RequestMapping("/token1")
    @ResponseBody
    public String test() {
        return "test";
    }
}
