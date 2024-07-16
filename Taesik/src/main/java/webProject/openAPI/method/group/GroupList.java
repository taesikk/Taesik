package com.forcs.eformsign.webhook.openAPI.method.group;

import com.forcs.eformsign.webhook.openAPI.common.Constants;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GroupList {
    public static String check1="";
    public static String check2="";

    public StringBuilder mainSB;


    public void group_list(){
        String url = "";
        try {
            checkValue(check1,check2);
            url = Constants.GROUP_LIST_URL;
            httpConnectionGroupList(url);
        }catch (Exception e){
            e.getMessage();
        }
    }

    public StringBuilder httpConnectionGroupList(String urlData){
        String totalUrl = "";
        String accessToken = "";
        StringBuilder sb=null;
        try {
            totalUrl = urlData;
            accessToken = Constants.ACCESS_TOKEN;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "GET");

            sb = Constants.print(conn, "그룹 목록 조회");
            mainSB=sb;
//            System.out.println("mainSB : "+ mainSB.toString());
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }

    public void checkValue(String include_member, String include_field){
        Constants.setGroupListUrl(include_member, include_field);
    }
}