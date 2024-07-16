package com.forcs.eformsign.webhook.controller;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.method.group.GroupAdd;
import com.forcs.eformsign.webhook.openAPI.method.group.GroupDelete;
import com.forcs.eformsign.webhook.openAPI.method.group.GroupFix;
import com.forcs.eformsign.webhook.openAPI.method.group.GroupList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/group")
public class GroupsController {
    @RequestMapping(value = "/groupsListSearch")
    public String groups_listsearch(String accessToken, String includeMember, String includeField, Model model) {
        System.out.println("groupsListSearch");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Constants.ACCESS_TOKEN = accessToken;
        GroupList.check1 = includeMember;
        GroupList.check2 = includeField;

        GroupList groupList = new GroupList();
        groupList.group_list();

        String resultSB = groupList.mainSB.toString();
        File.logWrite("(그룹 목록 조회) " + resultSB);


        JSONObject jsonObject;
        JSONArray array;
        String[] id = new String[100];
        String[] name = new String[100];
        String[] description = new String[100];
        try {
            JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(resultSB);
            array = (JSONArray) jsonObject.get("groups");

            for (int i = 0; i < array.size(); i++) {
                jsonObject = (JSONObject) array.get(i);
                id[i] = jsonObject.get("id").toString();
                name[i] = jsonObject.get("name").toString();
                description[i] = jsonObject.get("description").toString();
//                System.out.println("id : "+id[i]+" name : "+name[i]+" description : "+description[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("description", description);
        model.addAttribute("result", resultSB);

        return "group/GroupsListSearchResult";

    }

    @RequestMapping(value = "/groupsCreate")
    public String groups_create(String accessToken, String name, String description, String members, Model model) {
        System.out.println("groupsCreate");

        Constants.ACCESS_TOKEN = accessToken;
        GroupAdd.name = name;
        GroupAdd.description = description;
        GroupAdd.members = members;

        GroupAdd groupAdd = new GroupAdd();
        groupAdd.group_add();

        String resultSB = groupAdd.mainSB.toString();
        File.logWrite("(그룹 생성) " + resultSB);


        JSONObject object;
        String idJsp = new String();
        String nameJsp = new String();
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(resultSB);
            object = (JSONObject) object.get("group");
            idJsp = object.get("id").toString();
            nameJsp = object.get("name").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("idJsp", idJsp);
        model.addAttribute("nameJsp", nameJsp);
        model.addAttribute("result", resultSB);

        return "group/GroupsCreateResult";
    }

    @RequestMapping(value = "/groupsDelete")
    public String groups_delete(String accessToken, String group_id, Model model) {
        System.out.println("groupsDelete");

        Constants.ACCESS_TOKEN = accessToken;
        GroupDelete.group_id = group_id;

        GroupDelete groupDelete = new GroupDelete();
        groupDelete.group_delete();

        String resultSB = groupDelete.mainSB.toString();
        File.logWrite("(그룹 삭제) " + resultSB);


        JSONObject object;
        String codeJsp = null;
        String messageJsp = null;
        Integer statusJsp = null;
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(resultSB);
            codeJsp = object.get("code").toString();
            messageJsp = object.get("message").toString();
            statusJsp = Integer.parseInt(object.get("status").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("codeJsp", codeJsp);
        model.addAttribute("messageJsp", messageJsp);
        model.addAttribute("statusJsp", statusJsp);
        model.addAttribute("result", resultSB);

        return "group/GroupsDeleteResult";
//        return ResponseEntity.status(302).header("Location", "/home").build();
    }

    @RequestMapping(value = "/groupsUpdate")
    public String groups_update(String accessToken, String group_id, String name, String description, String member, Model model) {
        System.out.println("groupsUpdate");

        Constants.ACCESS_TOKEN = accessToken;
        GroupFix.group_id = group_id;
        GroupFix.name = name;
        GroupFix.description = description;
        GroupFix.member = member;

        GroupFix groupFix = new GroupFix();
        groupFix.group_fix();

        String resultSB = groupFix.mainSB.toString();
        File.logWrite("(그룹 수정) " + resultSB);

        JSONObject object;
        String idJsp = new String();
        String nameJsp = new String();
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(resultSB);
            object = (JSONObject) object.get("group");
            idJsp = object.get("id").toString();
            nameJsp = object.get("name").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("idJsp", idJsp);
        model.addAttribute("nameJsp", nameJsp);
        model.addAttribute("result", resultSB);

        return "group/GroupsUpdateResult";
    }
}
