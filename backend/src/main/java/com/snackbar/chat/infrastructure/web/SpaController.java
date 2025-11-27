package com.snackbar.chat.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller para fallback do SPA: serve `index.html` do frontend quando disponível.
 */
@Controller
@RequiredArgsConstructor
public class SpaController {

    @Value("${frontend.path:../frontend/dist}")
    private String frontendPath;

    /**
     * Serve `index.html` only for application routes (no file extension) and not for API paths.
     * This prevents serving `index.html` for JS/CSS requests which would return HTML with MIME text/html
     * and cause browser errors like "blocked due to MIME type ('text/html')".
     */
    @GetMapping(value = {"/", "{path:[^\\.]*}", "**/{path:[^\\.]*}"})
    public ResponseEntity<Resource> index(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Don't handle API calls
        if (uri.startsWith("/api/")) {
            return ResponseEntity.notFound().build();
        }

        Resource index = findIndex();
        if (index == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(index);
    }

    private Resource findIndex() {
        try {
            Path p = Paths.get(frontendPath).resolve("index.html").toAbsolutePath();
            if (Files.exists(p)) {
                return new UrlResource(p.toUri());
            }
        } catch (MalformedURLException e) {
            // ignore and fallback
        }

        ClassPathResource classPathIndex = new ClassPathResource("static/index.html");
        if (classPathIndex.exists()) return classPathIndex;
        return null;
    }
}
