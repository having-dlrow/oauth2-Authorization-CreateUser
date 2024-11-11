package com.example.oauth2client.config;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        printHeader((HttpServletRequest) request);
        chain.doFilter(request, response);
    }


    private void printHeader(HttpServletRequest req) {

        // header
        System.err.println("LogFilter: " + LocalDateTime.now() + " - " + req.getLocalAddr() + ":" + req.getLocalPort() + req.getServletPath());
        // body
        Collections.list(req.getHeaderNames())
                .forEach(header -> System.out.println("\tHeader: " + header + ": " + req.getHeader(header)));
        // footer
        System.out.println("\n\n");
    }
}
