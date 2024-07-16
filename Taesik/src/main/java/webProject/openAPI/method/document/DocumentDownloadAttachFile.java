package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DocumentDownloadAttachFile {

    public void document_download_attach_file() {
        String url = "";

        try {
            checkValue();
            url = Constants.DOCUMENT_DOWNLOAD_ATTACH_FILE;

            httpConnectionDocumentDownloadAttachFile(url);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void httpConnectionDocumentDownloadAttachFile(String urlData) {
        String totalUrl = "";
        String accessToken = "";

        try {
            totalUrl = urlData;
            accessToken = Constants.ACCESS_TOKEN;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "GET");

            Constants.print(conn, "문서 첨부파일 다운로드");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void checkValue() {
        try {
            Object ob = new JSONParser().parse(new FileReader(".\\APITest_Auto\\src\\main\\java\\data\\DocumentDownloadAttachFile.json"));
            JSONObject jsonObject = (JSONObject) ob;
            String id = jsonObject.get("document_id").toString();
            String doc_without_attachments = jsonObject.get("doc_without_attachments").toString();

            Constants.setDocumentDownloadAttachFile(id, doc_without_attachments);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
