package com.example.myhc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器
 * 页面跳转控制器
 *
 *
 */
@Controller
@RequestMapping("/html")
public class IndexController {

    @RequestMapping("/**")
    public void toHtml(HttpServletRequest request, Model model){
        model.addAllAttributes(request.getParameterMap());
    }
}