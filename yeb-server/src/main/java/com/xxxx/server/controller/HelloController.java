package com.xxxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HelloController {

    @GetMapping("hello")
    public  String hello(){
        return  "hello";
    }

    @GetMapping("/employee/basic/hello")
    public  String hello2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/employee/advanced/hello").forward(request,response);
        System.out.println("/employee/basic/hello");
        return  "/employee/basic/hello";

    }

    @GetMapping("/employee/advanced/hello")
    public  String hello3(){
        System.out.println("/employee/advanced/hello");
        return  "/employee/advanced/hello";
    }



}
