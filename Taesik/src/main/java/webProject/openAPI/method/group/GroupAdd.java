package com.forcs.eformsign.webhook.openAPI.method.group;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;

import java.io.FileWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GroupAdd {
    public StringBuilder mainSB;

    public static String name;
    public static String description;
    public static String members;

    public void group_add() {
        String url = "";
        try {
            url = Constants.GROUP_URL;
            httpConnectionGroupAdd(url);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public StringBuilder httpConnectionGroupAdd(String urlData) {
        String totalUrl = "";
        String accessToken = "";
        String bodyData ="{\"group\":{\"name\":\""+name+"\",\"description\":\""+description+"\",\"members\":[\""+members+"\"]}}";
        StringBuilder sb = null;
        Scanner scanner = new Scanner(System.in);
        try {
            totalUrl = urlData;
            accessToken = Constants.ACCESS_TOKEN;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "POST");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = bodyData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            sb = Constants.print(conn, "그룹 추가");
            mainSB = sb;

        } catch (Exception e) {
            e.getMessage();
        }
        return sb;
    }
}
