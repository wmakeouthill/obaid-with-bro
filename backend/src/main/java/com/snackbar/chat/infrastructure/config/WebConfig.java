package com.snackbar.chat.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Caminho relativo (para desenvolvimento) onde o frontend é gerado: ../frontend/dist
     * Pode ser substituído por propriedade de ambiente: -Dfrontend.path=...
     */
    @Value("${frontend.path:../frontend/dist}")
    private String frontendPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve arquivos estáticos: primeiro do diretório frontend/dist, depois fallback para classpath
        String location = "file:" + frontendPath + "/";
        registry.addResourceHandler("/**")
            .addResourceLocations(location, "classpath:/static/", "classpath:/public/")
            .setCachePeriod(0);
    }
}
