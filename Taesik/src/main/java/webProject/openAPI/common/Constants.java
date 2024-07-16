package com.forcs.eformsign.webhook.openAPI.common;

import java.io.*;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Constants {

    //Refresh Token, Access Token
    public static String REFRESH_TOKEN = "";
    public static String ACCESS_TOKEN = "";

    //json 파일 url
    public static final String TOKEN_FILE_URL = ".\\src\\main\\java\\com\\forcs\\eformsign\\webhook\\openAPI\\data\\token.json";
    public static final String TOKEN_ACCESS_FILE_URL = ".\\APITest_Auto\\src\\main\\java\\data\\tokenInfo.json";
    public static final String DOC_ID_FILE_URL = ".\\APITest_Auto\\src\\main\\java\\data\\DocumentId.json";
    //토큰발급 URL
    public static final String TOKEN_ACCESS_URL = "https://api.eformsign.com/v2.0/api_auth/access_token";

    //dlwhsktm
    public static String API_KEY = "";

    //일반 API url
    public static final String API_URL = "https://kr-api.eformsign.com/v2.0";

    public static final String REFRESH_TOKEN_URL = API_URL + "/api_auth/refresh_token?refresh_token=";

    //Document관련 url
    public static final String DOCUMENT_CREATE_URL = API_URL + "/api/documents?template_id=";

    public static String DOCUMENT_LIST_URL = API_URL + "/api/list_document";

    public static final String DOCUMENT_DELETE_URL = API_URL + "/api/documents";

    public static String DOCUMENT_EXTERNAL_URL = "";

    public static String DOCUMENT_INFO_URL = "";

    public static String DOCUMENT_DOWNLOAD_URL = "";

    public static String DOCUMENT_DOWNLOAD_ATTACH_FILE = "";

    public static String DOCUMENT_REQUEST_URL = API_URL + "/api/documents/";

    public static final String DOCUMENT_TEMPLATE_LIST = API_URL + "/api/forms";

    public static final String DOCUMENT_MASS_URL = API_URL + "/api/forms/mass_documents?template_id=";

    //member 관련 url
    public static String MEMBER_LIST_URL = "";

    public static final String MEMBER_FIX_DELETE_URL = API_URL + "/api/members/";


    //group 관련 url
    public static String GROUP_LIST_URL = "";

    public static final String GROUP_URL = API_URL + "/api/groups";


    //url 세팅 메소드
    public static void setDocumentListUrl(String field, String histories, String previous_status, String next_status) {
        DOCUMENT_LIST_URL = API_URL + "/api/list_document?include_fields=" + field + "&include_histories=" + histories + "&include_previous_status=" + previous_status + "&include_next_status=" + next_status;

    }

    public static void setDocumentExternalUrl(String company, String template) {
        DOCUMENT_EXTERNAL_URL = API_URL + "/api/documents/external?company_id=" + company + "&template_id=" + template;
    }

    public static void setDocumentDownloadUrl(String id, String type) {
        DOCUMENT_DOWNLOAD_URL = API_URL + "/api/documents/" + id + "/download_files?file_type=" + type;
    }

    public static void setDocumentDownloadAttachFile(String id, String temp) {
        DOCUMENT_DOWNLOAD_ATTACH_FILE = API_URL + "/api/documents/" + id + "/download_attach_files?doc_without_attachments=" + temp;
    }

    public static void setMemberListUrl(String member, String field, String delete, String name) {
        MEMBER_LIST_URL = API_URL + "/api/members?member_all=" + member + "&include_field=" + field + "&include_delete=" + delete + "&eb_name_search=" + name;
    }

    public static void setGroupListUrl(String member, String field) {
        GROUP_LIST_URL = API_URL + "/api/groups?include_member=" + member + "&include_field=" + field;
    }


    //HttpUrlConnection 공통 헤더 세팅
    public static HttpURLConnection headerSet(HttpURLConnection conn, String acc, String type) {
        try {
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + acc);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(type);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //서버로부터 받아온 json 읽어오기
    public static StringBuilder print(HttpURLConnection conn, String status) throws IOException {
        StringBuilder response = null;

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                    response.append("\n");
                }
                System.out.println("응답 코드 : " + String.valueOf(conn.getResponseCode()));
                System.out.println("응답 데이터 : " + response.toString());
                //logWrite(response.toString(), status, String.valueOf(conn.getResponseCode()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String result = new BufferedReader(new InputStreamReader(conn.getErrorStream()))
                    .lines().collect(Collectors.joining("\n"));
            System.out.println("error : " + result);
            response = new StringBuilder(result);
            //logWrite(result+"\n", status, String.valueOf(conn.getResponseCode()));
        }

        return response;
    }
}
