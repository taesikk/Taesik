package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DocumentInfo {
    public static String info_id = "";

    public void document_info() {
        String url = "";

        try {
            DocumentRequest.fileRead(Constants.DOC_ID_FILE_URL);
            url = Constants.DOCUMENT_DELETE_URL + "/" + info_id;
            checkValue(url);

            httpConnectionDocumentInfo(Constants.DOCUMENT_INFO_URL);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void httpConnectionDocumentInfo(String urlData) {
        String accessToken = "";
        String totalUrl = "";

        try {
            accessToken = Constants.ACCESS_TOKEN;
            totalUrl = urlData;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "GET");

            Constants.print(conn, "문서 정보 조회");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void checkValue(String url) {
        String include_fields = "true";
        String include_histories = "true";
        String include_previous_status = "true";
        String include_next_status = "true";

        Constants.DOCUMENT_INFO_URL = url + "?include_fields=" + include_fields + "&include_histories=" + include_histories + "&include_previous_status=" + include_previous_status + "&include_next_status=" + include_next_status;
    }
}
