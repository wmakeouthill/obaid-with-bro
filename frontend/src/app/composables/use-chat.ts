import { inject, signal, computed } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { Message } from '../models/message.model';

export function useChat() {
  const chatService = inject(ChatService);
  const messages = signal<Message[]>([]);
  const isLoading = signal<boolean>(false);
  const inputText = signal<string>('');

  const visibleText = computed(() => {
    const text = inputText();
    const indexDelimitador = text.indexOf(';');
    if (indexDelimitador === -1) {
      return text;
    }
    return text.substring(0, indexDelimitador);
  });

  const hasHiddenText = computed(() => {
    return inputText().includes(';');
  });

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

  const handleInputChange = (value: string) => {
    inputText.set(value);
  };

  const clearChat = () => {
    messages.set([]);
  };

  return {
    messages,
    isLoading,
    inputText,
    visibleText,
    hasHiddenText,
    canSend,
    sendMessage,
    handleInputChange,
    clearChat
  };
}

