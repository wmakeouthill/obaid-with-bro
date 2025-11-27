package com.snackbar.chat.infrastructure.web;

import com.snackbar.chat.application.dto.ChatRequest;
import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.application.usecase.ChatUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatUseCase chatUseCase;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
