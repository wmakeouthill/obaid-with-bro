package com.snackbar.chat.application.usecase;

import com.snackbar.chat.application.dto.ChatRequest;
import com.snackbar.chat.application.dto.ChatResponse;
import com.snackbar.chat.application.port.in.GerenciarHistoricoChatPort;
import com.snackbar.chat.application.port.out.AIChatPort;
import com.snackbar.chat.application.service.ProcessarMensagemOcultaService;
import com.snackbar.chat.domain.entity.MensagemChat;
import com.snackbar.chat.domain.service.ObaidPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUseCase {
    private final AIChatPort aiChatPort;
    private final GerenciarHistoricoChatPort gerenciarHistoricoChatPort;
    private final ObaidPromptService obaidPromptService;
    private final ProcessarMensagemOcultaService processarMensagemOcultaService;

    public ChatResponse execute(ChatRequest request) {
        var mensagemProcessada = processarMensagemOcultaService.processar(request.message());
        String mensagemParaIA = mensagemProcessada.construirMensagemParaIA();
        
        MensagemChat mensagemUsuario = MensagemChat.criarMensagemUsuario(mensagemProcessada.visivel());
        gerenciarHistoricoChatPort.adicionarMensagem(mensagemUsuario);
        
        String systemPrompt = obaidPromptService.obterSystemPrompt();
        var historico = gerenciarHistoricoChatPort.obterHistorico();
        
        ChatResponse resposta = aiChatPort.chat(systemPrompt, historico, mensagemParaIA);
        
        MensagemChat mensagemAssistente = MensagemChat.criarMensagemAssistente(resposta.reply());
        gerenciarHistoricoChatPort.adicionarMensagem(mensagemAssistente);
        
        return resposta;
    }
}
