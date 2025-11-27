package com.snackbar.chat.infrastructure.ai;

import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.application.port.out.AIChatPort;
import org.springframework.stereotype.Component;

/**
 * Adapter placeholder para integração com provedor de IA.
 * Implementação mínima e segura: retorna eco da mensagem. Substituir por integração real.
 */
@Component
public class OpenAIAdapter implements AIChatPort {

    @Override
    public ChatResponse chat(String message) {
        // TODO: integrar com OpenAI, Azure OpenAI ou outro provedor.
        // Mantemos a implementação mínima para permitir desenvolvimento incremental.
        String reply = "Diabo diz: '" + message + "' (resposta de exemplo)";
        return new ChatResponse(reply);
    }
}
