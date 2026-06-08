package com.jinelei.scalefish.webdav;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class DavWellKnownController {

    @GetMapping("/.well-known/caldav")
    public void caldav(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseUrl = request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath();
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", baseUrl + "/webdav/");
    }

    @GetMapping("/.well-known/carddav")
    public void carddav(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseUrl = request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath();
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", baseUrl + "/webdav/");
    }
}
