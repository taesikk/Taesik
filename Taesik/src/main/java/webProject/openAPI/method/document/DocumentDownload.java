package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DocumentDownload {

    public void document_download() {
        String url = "";

        try {
            checkValue();
            url = Constants.DOCUMENT_DOWNLOAD_URL;

            httpConnectionDocumentDownload(url);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void httpConnectionDocumentDownload(String urlData) {
        String totalUrl = "";
        String accessToken = "";

        try {
            accessToken = Constants.ACCESS_TOKEN;
            totalUrl = urlData;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "GET");

            Constants.print(conn, "문서 파일 다운로드");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void checkValue() {

        try {
            Object ob = new JSONParser().parse(new FileReader(".\\APITest_Auto\\src\\main\\java\\data\\DocumentDownload.json"));
            JSONObject jsonObject = (JSONObject) ob;
            String id = jsonObject.get("document_id").toString();
            String type = jsonObject.get("type").toString();

            Constants.setDocumentDownloadUrl(id, type);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
