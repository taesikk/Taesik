package com.forcs.eformsign.webhook.openAPI.method.member;

import com.forcs.eformsign.webhook.openAPI.common.Constants;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MemberList {
    public static String member_all = "";
    public static String include_field = "";
    public static String include_delete = "";
    public static String eb_search_name = "";

    public StringBuilder member_list() {
        String url = "";
        StringBuilder sb = null;
        try {
            checkValue(member_all,include_field,include_delete,eb_search_name);

            url = Constants.MEMBER_LIST_URL;

           sb =  httpConnectionMemberList(url);
        } catch (Exception e) {
            e.getMessage();
        }
        return sb;
    }

    public StringBuilder httpConnectionMemberList(String urlData){
        String totalUrl = "";
        String accessToken = "";
        StringBuilder sb = null;
        try {
            totalUrl = urlData;
            accessToken = Constants.ACCESS_TOKEN;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "GET");

            sb = Constants.print(conn, "구성원 목록 조회");

        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }
    public void checkValue(String member, String field, String delete, String name) {
        String member_all = member;
        String include_field = field;
        String include_delete = delete;
        String eb_name_search = name;

        Constants.setMemberListUrl(member_all, include_field, include_delete, eb_name_search);
    }
}
