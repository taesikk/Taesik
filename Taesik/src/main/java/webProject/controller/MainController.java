package com.forcs.eformsign.webhook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/main")
public class MainController {
    private final Logger logger = LoggerFactory.getLogger("log");
    //get방식으로 요청이 들어왔을때
    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<Object> get(@RequestBody(required = false) String params) { //String값으로 body를 받음
        System.out.println("MainController.get");

        if (params == null) {
            params = "{response_code : 200, result : completed}";
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{\"code\": 200, \n\"result\": \"success\" }"); // 클라이언트에 값 전송
    }

    //post방식으로 요청이 들어왔을때
    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<Object> post(@RequestBody(required = false) String params) { // String값으로 body를 받음
        System.out.println("MainController.post");
        String ip = "";
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = req.getHeader("X-FORWARDED-FOR");

        logger.info("(" + ip + ") " + params);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
