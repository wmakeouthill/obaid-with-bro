package com.snackbar.chat.application.port.out;

import com.snackbar.chat.application.dto.ChatResponse;

public interface AIChatPort {
    ChatResponse chat(String message);
}
