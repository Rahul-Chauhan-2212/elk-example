package org.example;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class ELKController {

    private static final Logger LOG = LoggerFactory.getLogger(ELKController.class);

    @Autowired
    RestTemplate restTemplete;

    @GetMapping("/elkdemo")
    public String helloWorld() {
        String response = "Hello user ! " + new Date();
        LOG.info("/elkdemo - > " + response);

        return response;
    }

    @GetMapping("/elk")
    public String helloWorld1() {

        String response = restTemplete.exchange("http://localhost:8081/elkdemo", HttpMethod.GET, null,
                new ParameterizedTypeReference<String>() {

                }).getBody();
        LOG.debug("/elk - > " + response);

        try {
            String exceptionrsp = restTemplete.exchange("http://localhost:8081/exception", HttpMethod.GET, null,
                    new ParameterizedTypeReference<String>() {

                    }).getBody();
            LOG.warn("/elk trying to print exception - > " + exceptionrsp);
            response = response + " === " + exceptionrsp;
        } catch (Exception e) {

        }

        return response;
    }

    @GetMapping("/exception")
    public String exception() {
        String rsp = "";
        try {
            int i = 1 / 0;
            // should get exception
        } catch (Exception e) {
            LOG.error("error occurred ->", e);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            LOG.trace("Exception As String :: - > " + sStackTrace);

            rsp = sStackTrace;
        }

        return rsp;
    }
}