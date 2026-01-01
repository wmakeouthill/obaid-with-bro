package com.snackbar.chat.infrastructure.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.application.port.out.AIChatPort;
import com.snackbar.chat.domain.entity.MensagemChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
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
    private static final String MODELO_PADRAO = "gpt-4o-mini";
    private static final int MAX_TOKENS = 300;

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String apiKey;

    public OpenAIAdapter(@Value("${openai.api.key:}") String openaiApiKey) {
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
    public ChatResponse chat(String systemPrompt, List<MensagemChat> historico, String mensagemAtual) {
        if (apiKey == null || apiKey.isBlank()) {
            return new ChatResponse("Serviço de IA não configurado. Defina a variável OPENAI_API_KEY.");
        }

        try {
            List<Map<String, Object>> mensagens = construirMensagens(systemPrompt, historico, mensagemAtual);
            String body = mapper.writeValueAsString(Map.of(
                    "model", MODELO_PADRAO,
                    "messages", mensagens,
                    "max_tokens", MAX_TOKENS));

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(DEFAULT_API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            return processarResposta(resp);
        } catch (Exception e) {
            log.error("Erro ao comunicar com OpenAI", e);
            return new ChatResponse("Erro ao chamar API de IA: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> construirMensagens(String systemPrompt, List<MensagemChat> historico,
            String mensagemAtual) {
        List<Map<String, Object>> mensagens = new ArrayList<>();
        mensagens.add(Map.of("role", "system", "content", systemPrompt));

        for (MensagemChat msg : historico) {
            String role = "user".equals(msg.role()) ? "user" : "assistant";
            mensagens.add(Map.of("role", role, "content", msg.content()));
        }

        mensagens.add(Map.of("role", "user", "content", mensagemAtual));
        return mensagens;
    }

    private ChatResponse processarResposta(HttpResponse<String> resp) {
        try {
            if (resp.statusCode() == 200) {
                JsonNode root = mapper.readTree(resp.body());
                JsonNode choices = root.get("choices");
                if (choices != null && choices.isArray() && !choices.isEmpty()) {
                    String reply = choices.get(0).get("message").get("content").asText();
                    return new ChatResponse(reply.trim());
                }
                return new ChatResponse("(sem resposta)");
            } else {
                log.error("Erro na API OpenAI: status={}, body={}", resp.statusCode(), resp.body());
                return new ChatResponse("Erro ao chamar API de IA: status=" + resp.statusCode());
            }
        } catch (Exception e) {
            log.error("Erro ao processar resposta da OpenAI", e);
            return new ChatResponse("Erro ao processar resposta: " + e.getMessage());
        }
    }
}
