package com.forcs.eformsign.webhook.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class File {

    public static JSONObject fileRead(String addr) {
        JSONObject jsonObject = null;
        JSONParser parser = new JSONParser();
        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(".\\src\\main\\java\\com\\forcs\\eformsign\\webhook\\openAPI\\data\\" + addr));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return jsonObject;
    }

    public static void fileWrite(String addr, JSONObject jsonObject) {
        try {
            FileWriter fileWriter = new FileWriter(".\\src\\main\\java\\com\\forcs\\eformsign\\webhook\\openAPI\\data\\" + addr);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void logWrite(String data){
        LocalDateTime now = LocalDateTime.now();
        String formatNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {
            FileWriter fileWriter = new FileWriter(".\\src\\main\\resources\\logback\\syslog\\APILog.txt", true);
            fileWriter.write("(" + formatNow + ") " + data);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
