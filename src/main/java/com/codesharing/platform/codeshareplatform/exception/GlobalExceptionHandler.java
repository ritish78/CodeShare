package com.codesharing.platform.codeshareplatform.exception;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         @Nullable Object object, Exception ex) {

        response.setStatus(HttpServletResponse.SC_OK);

        ModelAndView modelAndView = new ModelAndView();
        String errorPage = "";
        if (ex instanceof CodeNotFoundException) {
            errorPage = "pageNotFound";
        } else if (ex instanceof TimeExceededException) {
            errorPage = "timeExceeded";
        }

        modelAndView.addObject("exception", ex.getMessage());
        modelAndView.setViewName(errorPage);
        return modelAndView;
    }
}
