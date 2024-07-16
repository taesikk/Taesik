package com.forcs.eformsign.webhook.controller;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.method.member.MemberDelete;
import com.forcs.eformsign.webhook.openAPI.method.member.MemberFix;
import com.forcs.eformsign.webhook.openAPI.method.member.MemberList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/member")
public class MemberController {
    @RequestMapping(value = "/")
    public String mainHome(){
        return "index";
    }
    @RequestMapping(value = "/memberList")
    public String memberList(String accessToken, String memberAll, String includeField, String includeDelete, String name, Model model){
        System.out.println("memberAll : " + memberAll);

        Constants.ACCESS_TOKEN = accessToken;
        MemberList.member_all = memberAll;
        MemberList.include_field = includeField;
        MemberList.include_delete = includeDelete;
        MemberList.eb_search_name = name;

        MemberList memberList = new MemberList();
        StringBuilder sb = memberList.member_list();
        File.logWrite("(멤버 목록 조회) " + sb.toString());


        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        String[] id = new String[100];
        String[] username = new String[100];
        String[] userDepartment = new String[100];
        String[] userPosition = new String[100];

        try {
            jsonObject = (JSONObject) parser.parse(sb.toString());
            jsonArray = (JSONArray) jsonObject.get("members");
            for (int i=0;i<jsonArray.size();i++){
                jsonObject = (JSONObject) jsonArray.get(i);
                id[i] = jsonObject.get("id").toString();
                username[i] = jsonObject.get("name").toString();
                userDepartment[i]=jsonObject.get("department").toString();
                userPosition[i]=jsonObject.get("position").toString();
            }
        }catch (Exception e){
            e.getMessage();
        }

        model.addAttribute("result", id);
        model.addAttribute("username", username);
        model.addAttribute("userDepartment", userDepartment);
        model.addAttribute("userPosition", userPosition);
        model.addAttribute("json", sb.toString());
        return "member/MemberListResult";
    }
    @RequestMapping(value = "/memberFix")
    public String memberFix(String accessToken, String memberId,String jsonName,Boolean jsonEnabled,String jsonNumber
                            ,String jsonTel,String jsonDepartment, String jsonPosition, Model model){

        Constants.ACCESS_TOKEN = accessToken;
        MemberFix.member_id = memberId; // url에 들어가는 변수
        MemberFix.jsonName = jsonName;
        MemberFix.jsonEnabled = jsonEnabled;
        MemberFix.jsonNumber = jsonNumber;
        MemberFix.jsonTel = jsonTel;
        MemberFix.jsonDepartment = jsonDepartment;
        MemberFix.jsonPosition = jsonPosition;


        MemberFix memberFix = new MemberFix();
        StringBuilder sb =memberFix.member_fix();
        File.logWrite("(멤버 수정) " + sb.toString());

        JSONParser parser = new JSONParser();
        JSONObject object;
        String companyId = new String();
        String name = new String();
        String id = new String();
        try {
            object = (JSONObject) parser.parse(sb.toString());
            companyId = object.get("company_id").toString();
            object = (JSONObject) object.get("member");
            name=object.get("name").toString();
            id=object.get("id").toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("companyId", companyId);
        model.addAttribute("name", name);
        model.addAttribute("id", id);
        model.addAttribute("result", sb.toString());
        return "member/MemberFixResult";
    }

    @RequestMapping(value = "/memberDelete")
    public String memberDelete(String accessToken, String memberId,Model model){
        Constants.ACCESS_TOKEN = accessToken;
        MemberDelete.memberId=memberId;

        MemberDelete memberDelete = new MemberDelete();
        StringBuilder sb = memberDelete.member_delete();
        File.logWrite("(멤버 삭제) " + sb.toString());

        JSONParser parser = new JSONParser();
        JSONObject object;
        String code = new String();
        String message = new String();
        String status = new String();
        try {
            object = (JSONObject) parser.parse(sb.toString());
            code = object.get("code").toString();
            message = object.get("message").toString();
            status = object.get("status").toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("code", code);
        model.addAttribute("message", message);
        model.addAttribute("status", status);
        model.addAttribute("result", sb.toString());

        return "member/MemberDeleteResult";
    }
}
