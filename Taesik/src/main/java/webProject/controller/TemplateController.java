package com.forcs.eformsign.webhook.controller;

import com.forcs.eformsign.webhook.openAPI.method.document.DocumentList;
import com.forcs.eformsign.webhook.openAPI.method.document.DocumentMass;
import com.forcs.eformsign.webhook.openAPI.method.document.DocumentTemplateList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "")
public class TemplateController {

    @RequestMapping(value = "/templateList")
    public String templateList(Model model) {
        DocumentTemplateList documentTemplateList = new DocumentTemplateList();
        StringBuilder sb = documentTemplateList.document_template_list();
        File.logWrite("(템플릿 목록 조회) " + sb.toString());

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        String[] formId = new String[100];
        String[] name = new String[100];
        String[] createId = new String[100];
        String[] createName = new String[100];      String error = "";

        try {

            jsonObject = (JSONObject) parser.parse(sb.toString());
            if (jsonObject.get("ErrorMessage") != null) {
                System.out.println("errorstart");
                error = jsonObject.get("ErrorMessage").toString();
            }
            jsonArray = (JSONArray) jsonObject.get("templates");
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                formId[i] = jsonObject.get("form_id").toString();
                name[i] = jsonObject.get("name").toString();
                createId[i] = jsonObject.get("create_id").toString();
                createName[i] = jsonObject.get("create_name").toString();
            }
        } catch (Exception e) {
            e.getMessage();
        }

        model.addAttribute("error", error);
        model.addAttribute("form_id", formId);
        model.addAttribute("name", name);
        model.addAttribute("createId", createId);
        model.addAttribute("createName", createName);

        return "TemplateList";
    }

}
