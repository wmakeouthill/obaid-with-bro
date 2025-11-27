package com.snackbar.chat.application.usecase;

import com.snackbar.chat.application.dto.ChatRequest;
import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.application.port.out.AIChatPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUseCase {
    private final AIChatPort aiChatPort;

    public ChatResponse execute(ChatRequest request) {
        return aiChatPort.chat(request.message());
    }
}
