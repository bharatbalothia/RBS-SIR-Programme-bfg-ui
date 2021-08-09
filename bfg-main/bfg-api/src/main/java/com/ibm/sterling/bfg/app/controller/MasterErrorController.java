package com.ibm.sterling.bfg.app.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MasterErrorController implements ErrorController {
    private static final Logger LOG = LogManager.getLogger(MasterErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        LOG.info("Request to /error handled in MasterErrorController");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        LOG.info("Error status code is {}", status);
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
