package com.forcs.eformsign.webhook.openAPI.method.token;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class TokenAccess {
    //토큰값을 저장하기 위한 Map
    public static String member_id = "";

    public StringBuilder token_access() {
        StringBuilder sb = null;
        try {
            //result에 eformsign_signature, executionTime값 불러오기, json set
            Map<String, Object> result = new EformsignSignatureMake().signMake();
            String signature = result.get("eformsign_signature").toString();
            long executionTime = (Long) result.get("execution_time");
            String jsondata = "{\"execution_time\" : " + executionTime + ", \"member_id\" : \"" + member_id + "\"}";
            System.out.println("eformsign_signature : " + signature);
            System.out.println("executionTime : " + executionTime);

            //http 통신 메소드 호출
            sb = httpGetConnection(Constants.TOKEN_ACCESS_URL, signature, jsondata);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return sb;
    }

    public static StringBuilder httpGetConnection(String UrlData, String signature, String json) {
        String totalUrl = UrlData;
        String encodeBytes = "";
        String eformsign_signature = signature;
        String jsondata = json;
        JSONObject jsonObject = new JSONObject();
        StringBuilder response = null;

        //API KEY Base64로 인코딩
        encodeBytes = encoding(Constants.API_KEY);

        //System.out.println("Base64 : " + encodeBytes);
        System.out.println();

        String refreshToken = "";
        String accessToken = "";

        try {
            //URL,HttpConnection 객체 생성
            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //conn객체 request-header 세팅
            conn.setRequestProperty("eformsign_signature", eformsign_signature);
            conn = Constants.headerSet(conn, encodeBytes, "POST");

            //request-body conn객체에 쓰기
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.close();
            }

            conn.connect();
            try { // 결과값 받아서 refresh-token, access-token 인덱싱
                response = Constants.print(conn, "토큰 발급");
                refreshToken = response.substring(response.indexOf("refresh_token") + 16, response.indexOf("access_token") - 3);
                //System.out.println("refreshtoken : " + refreshToken);
                accessToken = response.substring(response.indexOf("access_token") + 15, response.length() - 4);
                //System.out.println("accesstoken : " + accessToken);

                Constants.REFRESH_TOKEN = refreshToken;
                Constants.ACCESS_TOKEN = accessToken;

                //token.json 파일에 쓰기
                jsonObject.put("refresh-token", refreshToken);
                jsonObject.put("access-token", accessToken);


                FileWriter file = new FileWriter(Constants.TOKEN_FILE_URL);
                file.write(jsonObject.toJSONString());
                file.flush();
                file.close();

            }catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String encoding(String param){
        String temp = Base64.getEncoder().encodeToString(param.getBytes(StandardCharsets.UTF_8));
        return temp;
    }
}
