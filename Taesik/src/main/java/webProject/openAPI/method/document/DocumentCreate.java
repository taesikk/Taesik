package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;



public class DocumentCreate {

    public void document_create(int num){
        try {
            String url = Constants.DOCUMENT_CREATE_URL;

            httpConnectionDocumentCreate(url, num);
        }catch (NullPointerException e){
            e.getMessage();
        }
    }

    public void httpConnectionDocumentCreate(String urlData, int num){
        String accessToken = "";
        String jsondata = "";
        String temp = "";
        String totalUrl = "";
        String status="";

        try {
            accessToken = Constants.ACCESS_TOKEN;
            Object ob = new JSONParser().parse(new FileReader(Constants.DOC_ID_FILE_URL));
            JSONObject jsonObject = (JSONObject) ob;

            //문서 종류에 따라서 다른 json 삽입
            if (num == 1){
                jsondata = JsonData.DOCUMENT_CREATE_JSON;
                totalUrl = urlData + jsonObject.get("document_create1");
                status = "새문서 작성(시작-완료)";
            } else if (num == 2) {
                jsondata = JsonData.DOCUMENT_CREATE_JSON2;
                totalUrl = urlData + jsonObject.get("document_create2");
                status = "새문서 작성(시작-참여자-완료)";
            }
            else{
                jsondata = JsonData.DOCUMENT_CREATE_JSON3;
                totalUrl = urlData + jsonObject.get("document_create3");
                status = "새문서 작성(시작-참여자-검토자-완료)";
            }

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "POST");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            conn.connect();

            //문서 삭제, 문서 정보 조회를 위한 id 인덱싱
            StringBuilder response = Constants.print(conn, status);
            String id = response.substring(response.indexOf("id")+5,response.indexOf("document_name") - 3);

            String deleteJson = "{\"document_ids\":[\""+ id +"\"]}";
            FileWriter file = new FileWriter(".\\APITest_Auto\\src\\main\\java\\data\\DocumentDelete.json");
            file.write(deleteJson);
            file.flush();
            file.close();

            /* DocumentDelete documentDelete = new DocumentDelete();
            documentDelete.doc_id = id;

            if (num == 2){
                DocumentInfo documentInfo = new DocumentInfo();
                documentInfo.info_id = id;
            }*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
