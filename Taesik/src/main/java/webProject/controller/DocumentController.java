package com.forcs.eformsign.webhook.controller;

import com.forcs.eformsign.webhook.openAPI.method.document.DocumentDelete;
import com.forcs.eformsign.webhook.openAPI.method.document.DocumentList;
import com.forcs.eformsign.webhook.openAPI.method.document.DocumentRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/document")
public class DocumentController {
    @RequestMapping(value = "/documentList")
    public String documentList(Model model) {
        DocumentList documentList = new DocumentList();
        StringBuilder sb = documentList.document_list(1);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        String[] doc_id = new String[100];
        String[] doc_name = new String[100];
        String[] create_name = new String[100];
        String[] create_id = new String[100];
        String[] recipient_id = new String[100];
        String[] recipient_name = new String[100];

        File.logWrite("(진행중 문서함 조회) " + sb.toString());

        try {
            jsonObject = (JSONObject) parser.parse(sb.toString());
            jsonArray = (JSONArray) jsonObject.get("documents");
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                doc_id[i] = jsonObject.get("id").toString();
                doc_name[i] = jsonObject.get("document_name").toString();
                JSONObject sample = (JSONObject) jsonObject.get("creator");
                create_name[i] = sample.get("name").toString();
                create_id[i] = sample.get("id").toString();
                sample = (JSONObject) jsonObject.get("current_status");
                JSONArray arraySample = (JSONArray) sample.get("step_recipients");
                sample = (JSONObject) arraySample.get(0);
                if (sample.get("id") != null) {
                    recipient_id[i] = sample.get("id").toString();
                } else {
                    recipient_id[i] = sample.get("email").toString();
                }
                if (sample.get("name") == null) {
                    recipient_name[i] = "(이름없음)";
                } else {
                    recipient_name[i] = sample.get("name").toString();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("doc_id", doc_id);
        model.addAttribute("doc_name", doc_name);
        model.addAttribute("create_id", create_id);
        model.addAttribute("create_name", create_name);
        model.addAttribute("recipient_id", recipient_id);
        model.addAttribute("recipient_name", recipient_name);

        return "document/DocumentListResult";
    }

    @RequestMapping(value = "/documentCompleteList")
    public String documentCompleteList(Model model) {
        DocumentList documentList = new DocumentList();
        StringBuilder sb = documentList.document_list(2);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        String[] doc_id = new String[100];
        String[] doc_name = new String[100];
        String[] create_name = new String[100];
        String[] create_id = new String[100];

        File.logWrite("(완료 문서함 조회) " + sb.toString());

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(sb.toString());
            jsonArray = (JSONArray) jsonObject.get("documents");
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                doc_id[i] = jsonObject.get("id").toString();
                doc_name[i] = jsonObject.get("document_name").toString();
                JSONObject sample = (JSONObject) jsonObject.get("creator");
                create_name[i] = sample.get("name").toString();
                create_id[i] = sample.get("id").toString();
            }
        } catch (Exception e) {
            System.out.println("error : " + e.getMessage());
        }

        model.addAttribute("doc_id", doc_id);
        model.addAttribute("doc_name", doc_name);
        model.addAttribute("create_id", create_id);
        model.addAttribute("create_name", create_name);

        return "document/DocumentCompleteListResult";
    }

    @RequestMapping(value = "/documentRequest")
    public String documentRequest(String docId, String jsonData, Model model) {
        System.out.println("docId : " + docId + ", jsonData : " + jsonData);
        DocumentRequest.doc_id = docId;
        DocumentRequest.jsonData = jsonData;
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        String code = "";
        String result = "";

        DocumentRequest documentRequest = new DocumentRequest();
        StringBuilder sb = documentRequest.document_request();
        File.logWrite("(문서 재요청) " + sb.toString());

        try {
            jsonObject = (JSONObject) parser.parse(sb.toString());
            if (jsonObject.get("code").toString().equals("-1")) {
                code = jsonObject.get("status").toString();
                result = jsonObject.get("message").toString();
            } else {
                code = jsonObject.get("code").toString();
                result = jsonObject.get("ErrorMessage").toString();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("code", code);
        model.addAttribute("result", result);
        return "document/DocumentRequestResult";
    }

    @RequestMapping(value = "/documentDelete")
    public String documentDelete(String documentId, Model model) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        String code = "";
        String result = "";
        try {
            DocumentDelete documentDelete = new DocumentDelete();
            documentDelete.data = documentId;
            StringBuilder sb = documentDelete.document_delete();
            File.logWrite("(문서 삭제) " + sb.toString());

            jsonObject = (JSONObject) parser.parse(sb.toString());
            if (jsonObject.get("code").toString().equals("-1")) {
                code = jsonObject.get("status").toString();
                result = jsonObject.get("message").toString();
            } else {
                code = jsonObject.get("code").toString();
                result = jsonObject.get("ErrorMessage").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("code", code);
        model.addAttribute("result", result);

        return "document/DocumentRequestResult";
    }

    @RequestMapping(value = "/documentAllList")
    public String documentAllList(Model model) {
        DocumentList documentList = new DocumentList();
        StringBuilder sb = documentList.document_list(3);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        String[] doc_id = new String[100];
        String[] doc_name = new String[100];
        String[] create_name = new String[100];
        String[] create_id = new String[100];
        String[] status_type = new String[100];

        File.logWrite("(문서 목록 조회) " + sb.toString());

        try {
            jsonObject = (JSONObject) parser.parse(sb.toString());
            jsonArray = (JSONArray) jsonObject.get("documents");
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                doc_id[i] = jsonObject.get("id").toString();
                doc_name[i] = jsonObject.get("document_name").toString();
                JSONObject sample = (JSONObject) jsonObject.get("creator");
                create_name[i] = sample.get("name").toString();
                create_id[i] = sample.get("id").toString();
                sample = (JSONObject) jsonObject.get("current_status");
                status_type[i] = statusCallBack(sample.get("status_type").toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("doc_id", doc_id);
        model.addAttribute("doc_name", doc_name);
        model.addAttribute("create_id", create_id);
        model.addAttribute("create_name", create_name);
        model.addAttribute("status_type", status_type);
        return "document/DocumentAllList";
    }

    public String statusCallBack(String str) {
        Map<String, String> map = new HashMap<>();
        map.put("001", "문서 임시 저장");
        map.put("002", "문서 생성");
        map.put("003", "문서 최종 완료");
        map.put("010", "결재요청");
        map.put("011", "결제반려");
        map.put("012", "결제승인");
        map.put("013", "결재요청취소");
        map.put("020", "내부자 요청");
        map.put("021", "내부자 반려");
        map.put("022", "내부자 승인");
        map.put("023", "내부자 임시 저장");
        map.put("030", "외부자 요청");
        map.put("031", "외부자 반려");
        map.put("032", "외부자 승인");
        map.put("033", "외부자 재요청");
        map.put("034", "외부자 열람");
        map.put("035", "외부자 임시저장");
        map.put("040", "문서 취소 요청");
        map.put("041", "문서 취소 요청거절");
        map.put("042", "문서 취소");
        map.put("043", "문서 수정");
        map.put("044", "문서 수정 취소");
        map.put("045", "문서 반려요청");
        map.put("046", "문서 반려요청 거절");
        map.put("047", "문서 삭제요청");
        map.put("048", "문서 삭제요청 거절");
        map.put("049", "문서 삭제");
        map.put("050", "완료 문서 pdf 전송");
        map.put("051", "문서 이관");
        map.put("060", "참여자 요청");
        map.put("061", "참여자 반려");
        map.put("062", "참여자 승인");
        map.put("063", "참여자 재요청");
        map.put("064", "참여자 문서열람");
        map.put("070", "검토자 요청");
        map.put("071", "검토자 반려");
        map.put("072", "검토자 승인");
        map.put("073", "검토자 재요청");
        map.put("074", "검토자 문서열람");
        return map.get(str);
    }
}
