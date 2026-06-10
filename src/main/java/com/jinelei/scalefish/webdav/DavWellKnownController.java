package com.jinelei.scalefish.webdav;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class DavWellKnownController {

    private static final Logger log = LoggerFactory.getLogger(DavWellKnownController.class);

    @GetMapping("/.well-known/caldav")
    public void caldav(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String location = request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath() + "/webdav/";
        log.debug(".well-known/caldav redirect -> {}", location);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", location);
    }

    @GetMapping("/.well-known/carddav")
    public void carddav(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String location = request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath() + "/webdav/";
        log.debug(".well-known/carddav redirect -> {}", location);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", location);
    }
}
