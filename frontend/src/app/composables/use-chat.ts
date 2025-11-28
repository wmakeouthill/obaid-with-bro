import { inject, signal, computed } from '@angular/core';
import { ChatService } from '../services/chat.service';
import { Message } from '../models/message.model';

export function useChat() {
  const chatService = inject(ChatService);
  const messages = signal<Message[]>([]);
  const isLoading = signal<boolean>(false);
  const inputText = signal<string>('');
  const hiddenWords = signal<string[]>([]);
  const showVideo = signal<boolean>(false);

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

  const extractWordsFromText = (text: string): string[] => {
    const textoLower = text.toLowerCase();
    const palavras: string[] = [];
    
    // Procura por padrões como "medo de X", "tenho medo de X", "medo de X e Y"
    const regexMedoDe = /medo\s+de\s+([^,.\n]+)/gi;
    let match;
    
    while ((match = regexMedoDe.exec(textoLower)) !== null) {
      const depoisDeMedoDe = match[1].trim();
      // Extrai palavras do que vem depois de "medo de", preservando acentos
      const palavrasDepois = depoisDeMedoDe
        .replace(/[^\w\sáàâãéêíóôõúçÁÀÂÃÉÊÍÓÔÕÚÇ]/gi, ' ')
        .split(/\s+/)
        .filter(word => word.length > 2);
      
      palavras.push(...palavrasDepois);
    }
    
    // Remove palavras comuns que não são relevantes
    const palavrasComuns = ['tenho', 'medo', 'de', 'que', 'com', 'para', 'por', 'uma', 'um', 'uns', 'umas', 'o', 'a', 'os', 'as', 'meu', 'minha', 'meus', 'minhas', 'sou', 'estou', 'ser', 'estar', 'ter', 'fazer', 'dizer', 'saber', 'querer', 'poder', 'dever', 'e', 'ou', 'mas', 'também'];
    
    return palavras.filter(palavra => !palavrasComuns.includes(palavra.toLowerCase()));
  };

  const canSend = computed(() => {
    const text = inputText().trim();
    return text.length > 0 && !isLoading();
  });

  const sendMessage = () => {
    const text = inputText().trim();
    if (!text || isLoading()) {
      return;
    }

    const mensagemVisivel = text.split(';')[0].trim();
    const mensagemOculta = text.includes(';') ? text.split(';').slice(1).join(';').trim() : '';
    const temTextoVisivel = mensagemVisivel.length > 0;
    
    // Guarda palavras da mensagem oculta
    if (mensagemOculta) {
      const palavrasOcultas = extractWordsFromText(mensagemOculta);
      hiddenWords.update(words => [...words, ...palavrasOcultas]);
    }
    
    // Só mostra mensagem se tiver texto visível
    if (temTextoVisivel) {
      const userMessage: Message = {
        from: 'user',
        text: mensagemVisivel,
        timestamp: new Date()
      };
      messages.update(arr => [...arr, userMessage]);
    }
    
    inputText.set('');
    isLoading.set(true);

    chatService.enviarMensagem(text).subscribe({
      next: (response) => {
        // Só mostra resposta se houver resposta (não é mensagem apenas oculta)
        if (response.reply && response.reply.trim().length > 0) {
          const diaboMessage: Message = {
            from: 'diabo',
            text: response.reply,
            timestamp: new Date()
          };
          messages.update(arr => [...arr, diaboMessage]);
          
          // Verifica se a resposta contém palavras ocultas
          const respostaLower = response.reply.toLowerCase();
          const palavrasOcultas = hiddenWords();
          
          // Normaliza as palavras removendo acentos para comparação
          const normalizarPalavra = (palavra: string) => {
            return palavra
              .toLowerCase()
              .normalize('NFD')
              .replace(/[\u0300-\u036f]/g, '');
          };
          
          const respostaNormalizada = normalizarPalavra(respostaLower);
          const contemPalavraOculta = palavrasOcultas.some(palavra => {
            const palavraNormalizada = normalizarPalavra(palavra);
            return respostaNormalizada.includes(palavraNormalizada);
          });
          
          if (contemPalavraOculta) {
            // Mostra o vídeo 2 segundos depois que a palavra chave foi usada
            setTimeout(() => {
              showVideo.set(true);
            }, 2000);
          }
        }
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
    clearChat,
    showVideo
  };
}

