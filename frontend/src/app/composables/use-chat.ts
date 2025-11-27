import { inject, signal, computed } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { Message } from '../models/message.model';

export function useChat() {
  const chatService = inject(ChatService);
  const messages = signal<Message[]>([]);
  const isLoading = signal<boolean>(false);
  const inputText = signal<string>('');
  const isTypingHidden = signal<boolean>(false);

  const canSend = computed(() => {
    const text = inputText().trim();
    return text.length > 0 && !isLoading();
  });

  const sendMessage = () => {
    const text = inputText().trim();
    if (!text || isLoading()) {
      return;
    }

    const mensagemVisivel = text.split(';')[0].trim() || text;
    const userMessage: Message = {
      from: 'user',
      text: mensagemVisivel,
      timestamp: new Date()
    };

    messages.update(arr => [...arr, userMessage]);
    inputText.set('');
    isTypingHidden.set(false);
    isLoading.set(true);

    chatService.enviarMensagem(text).subscribe({
      next: (response) => {
        const diaboMessage: Message = {
          from: 'diabo',
          text: response.reply,
          timestamp: new Date()
        };
        messages.update(arr => [...arr, diaboMessage]);
        isLoading.set(false);
      },
      error: () => {
        const errorMessage: Message = {
          from: 'diabo',
          text: 'Erro ao comunicar com o Obaid...',
          timestamp: new Date()
        };
        messages.update(arr => [...arr, errorMessage]);
        isLoading.set(false);
      }
    });
  };

  const handleKeyPress = (event: KeyboardEvent) => {
    if (event.key === ';') {
      isTypingHidden.set(true);
    }
  };

  const handleInputChange = (value: string) => {
    inputText.set(value);
    if (value.includes(';')) {
      isTypingHidden.set(true);
    }
  };

  const clearChat = () => {
    messages.set([]);
  };

  return {
    messages,
    isLoading,
    inputText,
    isTypingHidden,
    canSend,
    sendMessage,
    handleKeyPress,
    handleInputChange,
    clearChat
  };
}

