package com.snackbar.chat.application.port.out;

import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.domain.entity.MensagemChat;

import java.util.List;

public interface AIChatPort {
    ChatResponse chat(String systemPrompt, List<MensagemChat> historico, String mensagemAtual);
}
