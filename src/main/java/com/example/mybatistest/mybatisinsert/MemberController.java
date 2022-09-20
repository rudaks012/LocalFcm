package com.example.mybatistest.mybatisinsert;

import com.example.mybatistest.mybatisinsert.util.JsonResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        try {
            if (!result.hasErrors()) {
                setMemberStatus(param, member);
                swForEachFunction(member, result, res);
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


    @RequestMapping(value = "/gubun", method = {RequestMethod.POST})
    public @ResponseBody JsonResponse gubun(@RequestBody Map<String, String> param, Member member,
        BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        if (!result.hasErrors()) {
            gubunMemberStatus(param, member);
            mybatisInsertService.fcmDeleteGubun(member);

            String[] splitRegular = member.getPush().split(PUSH_EXPRESSION);
            for (String value : splitRegular) {
                String[] push = value.split(REGULAR_EXPRESSION);
                splitGubunMemberStatusInsert(member, result, res, push);
            }
            res.setUrl(member.getMbr_token());
            restSetOkMessage(res);
        } else {
            restSetFalseMessage(result, res);
        }
        return res;
    }

    @GetMapping(value = "/fcm")
    public List<Member> fcmSelect(Member member) {
        List<Member> fcmListMember = mybatisInsertService.fcmListMember(member);
        int a = mybatisInsertService.realInsert(fcmListMember);
        System.out.println("a = " + a);

        return fcmListMember;
    }


    @RequestMapping(value = "/redirect", method = {RequestMethod.POST})
    public @ResponseBody JsonResponse token(@RequestBody String Param, Member member, BindingResult result, HttpServletRequest request) {
        JsonResponse res = new JsonResponse(request);

        member.setSys_nm(Param);

        if (!result.hasErrors()) {
            List<Member> fcmSelectSys = mybatisInsertService.fcmSelectSys(member);
            restSetOkMessage(res);
            res.setData(fcmSelectSys);

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
