package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DocumentList {

    public StringBuilder document_list(int num) {
        String url = "";
        StringBuilder sb = null;
        try {
            url = Constants.DOCUMENT_LIST_URL;

            sb = httpConnectionDocumentList(url,num);

        } catch (NullPointerException e) {
            e.getMessage();
        }
        return sb;
    }

    public StringBuilder httpConnectionDocumentList(String urlData,int number) {
        String accessToken = "";
        String jsondata = "";
        String totalUrl = urlData;
        StringBuilder sb = null;
        try {
            accessToken = Constants.ACCESS_TOKEN;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (number==1){
                jsondata = JsonData.DOCUMENT_LIST_JSON;
            }else if (number==2) {
                jsondata = JsonData.DOCUMENT_COMPLETE_LIST_JSON;
            } else {
                jsondata = JsonData.DOCUMENT_ALL_LIST_JSON;
            }


            conn = Constants.headerSet(conn, accessToken, "POST");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (number==1){
                sb = Constants.print(conn, "진행 문서 목록 조회");
            }else if (number==2) {
                sb=Constants.print(conn, "완료 문서 목록 조회");
            } else {
                sb = Constants.print(conn, "문서 관리");
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return sb;
    }
}
