package com.ibm.sterling.bfg.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BfgApplication extends SpringBootServletInitializer/* implements WebApplicationInitializer */ {
    public static void main(String[] args) {
        SpringApplication.run(BfgApplication.class, args);
    }
}

@RestController
class RESTController {

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
