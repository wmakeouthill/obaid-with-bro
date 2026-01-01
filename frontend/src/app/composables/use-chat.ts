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

    // 1. Extrai TODAS as palavras significativas do texto (não só "medo de X")
    const todasPalavras = textoLower
      .replace(/[^\w\sáàâãéêíóôõúçÁÀÂÃÉÊÍÓÔÕÚÇ]/gi, ' ')
      .split(/\s+/)
      .filter(word => word.length > 2);

    // 2. Procura por padrões de medo (medo, pavor, pânico, horror, terror)
    const regexMedo = /(medo|pavor|pânico|horror|terror)\s+de\s+([^,.;\n]+)/gi;
    let match;

    while ((match = regexMedo.exec(textoLower)) !== null) {
      const depoisDe = match[2].trim();
      const palavrasDepois = depoisDe
        .replace(/[^\w\sáàâãéêíóôõúçÁÀÂÃÉÊÍÓÔÕÚÇ]/gi, ' ')
        .split(/\s+/)
        .filter(word => word.length > 2);

      palavras.push(...palavrasDepois);
    }

    // 3. Remove palavras muito comuns (stopwords)
    const palavrasComuns = [
      'tenho', 'medo', 'pavor', 'pânico', 'horror', 'terror',
      'que', 'com', 'para', 'por', 'uma', 'uns', 'umas',
      'os', 'as', 'meu', 'minha', 'meus', 'minhas',
      'sou', 'estou', 'ser', 'estar', 'ter', 'fazer',
      'dizer', 'saber', 'querer', 'poder', 'dever',
      'mas', 'também', 'muito', 'pouco', 'sempre', 'nunca',
      'isso', 'isso', 'aqui', 'ali', 'onde', 'quando', 'como',
      'the', 'and', 'for', 'are', 'but', 'not', 'you', 'all'
    ];

    // Adiciona todas as palavras significativas (não só de "medo de")
    const palavrasSignificativas = todasPalavras.filter(
      palavra => !palavrasComuns.includes(palavra.toLowerCase())
    );

    // Combina palavras do padrão "medo de X" com todas as outras significativas
    const resultado = [...new Set([...palavras, ...palavrasSignificativas])];

    // 4. Adiciona versões sem plural (remove 's' final) para matching mais robusto
    const comVariacoes: string[] = [];
    resultado.forEach(palavra => {
      comVariacoes.push(palavra);
      if (palavra.endsWith('s') && palavra.length > 3) {
        comVariacoes.push(palavra.slice(0, -1)); // "baratas" -> "barata"
      }
      if (palavra.endsWith('es') && palavra.length > 4) {
        comVariacoes.push(palavra.slice(0, -2)); // "ratões" -> "rat" (approximation)
      }
    });

    return [...new Set(comVariacoes)];
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

