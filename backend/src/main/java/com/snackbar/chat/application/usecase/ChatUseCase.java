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
        
        // Se tiver texto visível, adiciona ao histórico
        if (!mensagemProcessada.visivel().isBlank()) {
            MensagemChat mensagemUsuario = MensagemChat.criarMensagemUsuario(mensagemProcessada.visivel());
            gerenciarHistoricoChatPort.adicionarMensagem(mensagemUsuario);
        }
        
        // Se tiver informação oculta, SEMPRE guarda no histórico como mensagem de sistema para manter o contexto
        if (mensagemProcessada.temMensagemOculta()) {
            String contextoOculto = String.format(
                "[INFORMAÇÃO CRÍTICA OCULTA - O usuário pensou mas NÃO DISSE PUBLICAMENTE: \"%s\"]",
                mensagemProcessada.oculta()
            );
            MensagemChat mensagemSistema = MensagemChat.criarMensagemSistema(contextoOculto);
            gerenciarHistoricoChatPort.adicionarMensagem(mensagemSistema);
        }
        
        // Se a mensagem for apenas oculta (sem texto visível), não responde
        if (mensagemProcessada.visivel().isBlank() && mensagemProcessada.temMensagemOculta()) {
            return new ChatResponse("");
        }
        
        String mensagemParaIA = mensagemProcessada.construirMensagemParaIA();
        String systemPrompt = obaidPromptService.obterSystemPrompt();
        var historico = gerenciarHistoricoChatPort.obterHistorico();
        
        ChatResponse resposta = aiChatPort.chat(systemPrompt, historico, mensagemParaIA);
        
        // Só adiciona resposta ao histórico se houver resposta
        if (resposta.reply() != null && !resposta.reply().isBlank()) {
            MensagemChat mensagemAssistente = MensagemChat.criarMensagemAssistente(resposta.reply());
            gerenciarHistoricoChatPort.adicionarMensagem(mensagemAssistente);
        }
        
        return resposta;
    }
}
