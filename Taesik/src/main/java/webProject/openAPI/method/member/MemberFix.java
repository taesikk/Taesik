package com.forcs.eformsign.webhook.openAPI.method.member;

import com.forcs.eformsign.webhook.openAPI.common.Constants;
import com.forcs.eformsign.webhook.openAPI.common.JsonData;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class MemberFix {
    public static String member_id ="";
    public static String jsonName ="";
    public static Boolean jsonEnabled = Boolean.valueOf("true");
    public static String jsonNumber ="";
    public static String jsonTel ="";
    public static String jsonDepartment ="";
    public static String jsonPosition ="";

    public StringBuilder member_fix(){
        String url = "";
        StringBuilder sb = null;

        try {
            url = Constants.MEMBER_FIX_DELETE_URL;

            sb=httpConnectionMemberFix(url);
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }

    public StringBuilder httpConnectionMemberFix(String urlData){
        String totalUrl = "";
        String accessToken = "";
        String jsondata = "";
        StringBuilder sb = null;

        try {
            jsondata = "{\"account\":{\"id\":\""+member_id+"\",\"name\":\""+jsonName+"\",\"enabled\":"+jsonEnabled+",\"contact\":{\"number\":\""+jsonNumber+"\",\"tel\":\""+jsonTel+"\"},\"department\":\""+jsonDepartment+"\",\"position\":\""+jsonPosition+"\",\"role\":[\"company_manager\",\"template_manager\"]}}";
            totalUrl = urlData+ member_id;
            accessToken = Constants.ACCESS_TOKEN;


            //conn객체에 PATCH 추가
            allowMethods("PATCH");

            URL url = new URL(totalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //PATCH로 헤더 셋팅
            conn = Constants.headerSet(conn, accessToken, "PATCH");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsondata.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            sb = Constants.print(conn, "구성원 수정");
        }catch (Exception e){
            e.getMessage();
        }
        return sb;
    }

    private static void allowMethods(String methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
