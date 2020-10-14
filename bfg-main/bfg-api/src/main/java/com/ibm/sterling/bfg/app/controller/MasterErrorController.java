package com.ibm.sterling.bfg.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MasterErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
