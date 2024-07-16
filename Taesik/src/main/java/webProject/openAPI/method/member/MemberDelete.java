package com.forcs.eformsign.webhook.openAPI.method.member;

import com.forcs.eformsign.webhook.openAPI.common.Constants;

import java.net.HttpURLConnection;
import java.net.URL;

public class MemberDelete {
    public static String memberId ="";
    public StringBuilder member_delete(){
        String url = "";
        StringBuilder sb=null;

        try {
//            DocumentRequest.fileRead(Constants.DOC_ID_FILE_URL);
            url = Constants.MEMBER_FIX_DELETE_URL + memberId;
            sb=httpConnectionMemberDelete(url);
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }

    public StringBuilder httpConnectionMemberDelete(String urlData){
        String totalUrl = "";
        String accessToken = "";
        StringBuilder sb=null;

        try {
            totalUrl = urlData;
            accessToken = Constants.ACCESS_TOKEN;

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn = Constants.headerSet(conn, accessToken, "DELETE");

            sb=Constants.print(conn, "구성원 삭제");

        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }
}
