package com.forcs.eformsign.webhook.openAPI.method.token;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenRefresh {
    public StringBuilder token_refresh() {
        StringBuilder sb = null;
        try {
            String url = Constants.REFRESH_TOKEN_URL + Constants.REFRESH_TOKEN;

            sb = httpConnectionRefresh(url);
        } catch (Exception e) {
            e.getMessage();
        }
        return sb;
    }

    public static StringBuilder httpConnectionRefresh(String urlData) {

        String accessToken = "";
        String newRefreshToken = "";
        String newAccessToken = "";
        JSONObject jsonObject = new JSONObject();
        StringBuilder response = null;

        try {
            URL url = new URL(urlData);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            accessToken = Constants.ACCESS_TOKEN;

            //POST 방식 헤더 세팅
            conn = Constants.headerSet(conn, accessToken, "POST");

            conn.connect();
            try {
                response = Constants.print(conn, "토큰 갱신, 재발급");

                newRefreshToken = response.substring(response.indexOf("refresh_token") + 16, response.indexOf("access_token") - 3);
                //System.out.println("new refreshtoken : " + newRefreshToken);
                newAccessToken = response.substring(response.indexOf("access_token") + 15, response.length() - 4);
                //System.out.println("new accesstoken : " + newAccessToken);

                Constants.ACCESS_TOKEN = newAccessToken;
                Constants.REFRESH_TOKEN = newRefreshToken;

                jsonObject.put("refresh-token", newRefreshToken);
                jsonObject.put("access-token", newAccessToken);

                FileWriter file = new FileWriter(Constants.TOKEN_FILE_URL);
                file.write(jsonObject.toJSONString());
                file.flush();
                file.close();
            }catch (Exception e){
                e.getMessage();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
