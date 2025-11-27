package com.snackbar.chat.infrastructure.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.application.port.out.AIChatPort;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/**
 * Adapter para integração com a API da OpenAI.
 * A chave deve ser fornecida via variável de ambiente `OPENAI_API_KEY`
 * ou via propriedade `-Dopenai.api.key=...`.
 */
@Component
public class OpenAIAdapter implements AIChatPort {
    private static final Logger log = LoggerFactory.getLogger(OpenAIAdapter.class);

    private static final String DEFAULT_API_URL = "https://api.openai.com/v1/chat/completions";
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiKey;

    public OpenAIAdapter(@Value("${openai.api.key:}") String openaiApiKey) {
        // Prefer the Spring property `openai.api.key` if provided. If not, fallback to env var.
        if (openaiApiKey != null && !openaiApiKey.isBlank()) {
            this.apiKey = openaiApiKey;
            log.info("OpenAI key provided via Spring property 'openai.api.key'");
        } else {
            this.apiKey = System.getenv("OPENAI_API_KEY");
            if (this.apiKey != null && !this.apiKey.isBlank()) {
                log.info("OpenAI key provided via environment variable 'OPENAI_API_KEY'");
            }
        }
    }

    @Override
    public ChatResponse chat(String message) {
        if (apiKey == null || apiKey.isBlank()) {
            // Não expõe chave em logs; retorna mensagem amigável
            return new ChatResponse("Serviço de IA não configurado. Defina a variável OPENAI_API_KEY.");
        }

        try {
            // payload simples para Chat Completions
            Map<String,Object> payload = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", new Object[] {
                            Map.of("role", "system", "content", "Você é o Diabo brincalhão que conversa com o usuário de forma irreverente mas segura."),
                            Map.of("role", "user", "content", message)
                    },
                    "max_tokens", 256
            );

            String body = mapper.writeValueAsString(payload);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(DEFAULT_API_URL))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                JsonNode root = mapper.readTree(resp.body());
                JsonNode first = root.path("choices").path(0).path("message").path("content");
                String reply = first.isMissingNode() ? "" : first.asText();
                if (reply == null || reply.isBlank()) reply = "(sem resposta)";
                return new ChatResponse(reply.trim());
            } else {
                return new ChatResponse("Erro ao chamar API de IA: status=" + resp.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ChatResponse("Erro ao comunicar com IA: " + e.getMessage());
        }
    }
}
