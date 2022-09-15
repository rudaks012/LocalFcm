package com.example.mybatistest.mybatisinsert;

import com.example.mybatistest.mybatisinsert.util.JsonResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
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
        BindingResult result, HttpServletRequest request) throws Exception {
        JsonResponse res = new JsonResponse(request);
        System.out.println("check");
        if (!result.hasErrors()) {
            member.setMbr_id(param.get("mbr_id"));
            member.setMbr_nm(param.get("mbr_nm"));
            member.setMbr_token(param.get("mbr_token"));
            member.setOld_token(param.get("old_token"));

//            int insertCheck = mybatisInsertService.fcmInsertUser(param);
            int insertCheck = mybatisInsertService.fcmInsertPost(member);
            if (insertCheck != 0) {
                System.out.println("1");
            } else {
                System.out.println("0");
            }

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

    @RequestMapping("/token1")
    @ResponseBody
    public String test() {
        return "test";
    }
}
