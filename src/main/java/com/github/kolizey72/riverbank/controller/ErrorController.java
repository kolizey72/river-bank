package com.github.kolizey72.riverbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public ModelAndView errorPage(HttpServletRequest httpRequest) {
        ModelAndView modelAndView = new ModelAndView("error");
        String message;
        int statusCode = getStatusCode(httpRequest);
        switch (statusCode) {
            case 400 -> message = "400 Bad Request";
            case 403 -> message = "403 Forbidden";
            case 404 -> message = "404 Page not found";
            case 500 -> message = "500 Internal Server Error";
            default -> message = "" + statusCode;
        }

        modelAndView.addObject("message", message);

        return modelAndView;
    }

    private int getStatusCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
}
