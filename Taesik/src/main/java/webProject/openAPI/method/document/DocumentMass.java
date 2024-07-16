package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DocumentMass {

    public static String doc_id = "";
    public StringBuilder document_mass(){
        String url = "";
        StringBuilder sb=null;

        try {
            DocumentRequest.fileRead(Constants.DOC_ID_FILE_URL);
            url = Constants.DOCUMENT_MASS_URL + doc_id;

            sb=httpConnectionDocumentMass(url);
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }

    public StringBuilder httpConnectionDocumentMass(String urlData){
        String totalUrl= "";
        String accessToken = "";
        String jsondata = "";
        StringBuilder sb=null;

        try {
            totalUrl = urlData;
            accessToken = Constants.ACCESS_TOKEN;
            jsondata = JsonData.DOCUMENT_MASS_JSON;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "POST");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            sb=Constants.print(conn, "문서 일괄 작성");
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }
}
