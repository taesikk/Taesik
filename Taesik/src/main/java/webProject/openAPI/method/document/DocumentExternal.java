package com.forcs.eformsign.webhook.openAPI.method.document;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;
import com.forcs.eformsign.webhook.openAPI.method.token.TokenAccess;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DocumentExternal {

    public void document_external() {
        String url = "";

        try {
            checkValue();
            url = Constants.DOCUMENT_EXTERNAL_URL;

            httpConnectionDocumentExternal(url);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void httpConnectionDocumentExternal(String urlData) {
        String apiKey = "";
        String totalUrl = "";
        String jsondata = JsonData.DOCUMENT_EXTERNAL_JSON;
        try {
            //test1 API key
            apiKey = TokenAccess.encoding("4bc052ae-127e-462b-8161-16f2fdf2c9d4");
            //tester2 API key
            //apiKey = TokenAccess.encoding(Constants.API_KEY);
            totalUrl = urlData;
            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, apiKey, "POST");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            //conn.connect();
            System.out.println("apikey : " + apiKey);
            System.out.println("url : " + conn.getURL());
            System.out.println("code : " + conn.getResponseCode());
            System.out.println("error : " + conn.getErrorStream());
            Constants.print(conn, "문서 작성 외부자");

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void checkValue() {
        String company_id = "";
        String template_id = "";

        Scanner scanner = new Scanner(System.in);

        System.out.println("company_id 입력");
        company_id = scanner.nextLine();
        System.out.println("template_id 입력");
        template_id = scanner.nextLine();

        Constants.setDocumentExternalUrl(company_id.trim(), template_id.trim());


    }
}
