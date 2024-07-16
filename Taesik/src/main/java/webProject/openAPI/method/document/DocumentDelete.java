package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


import static com.forcs.eformsign.webhook.openAPI.common.JsonData.fileRead;

public class DocumentDelete {

    public static String doc_id = "";
    public static String data="";

    public StringBuilder document_delete(){
        String url = "";
        StringBuilder sb = null;

        try {
            url = Constants.DOCUMENT_DELETE_URL;

            sb=httpConnectionDocumentDelete(url);
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }

    public StringBuilder httpConnectionDocumentDelete(String urlData){
        String accessToken = "";
        String totalUrl = "";
        StringBuilder sb = null;
        String jsondata = "";

        try {
            Scanner scanner = new Scanner(System.in);

            accessToken = Constants.ACCESS_TOKEN;
            totalUrl = urlData;
            jsondata = "{\"document_ids\":[\""+data+"\"]}";

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "DELETE");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            sb = Constants.print(conn, "문서 삭제");
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }
}
