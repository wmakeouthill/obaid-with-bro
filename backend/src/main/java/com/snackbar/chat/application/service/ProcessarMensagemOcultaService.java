package com.snackbar.chat.application.service;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável por processar mensagens e extrair partes ocultas.
 */
@Service
public class ProcessarMensagemOcultaService {
    
    private static final String DELIMITADOR_OCULTO = ";";
    
    public MensagemProcessada processar(String mensagemCompleta) {
        if (mensagemCompleta == null || mensagemCompleta.isBlank()) {
            return new MensagemProcessada("", "");
        }
        
        int indiceDelimitador = mensagemCompleta.indexOf(DELIMITADOR_OCULTO);
        
        if (indiceDelimitador == -1) {
            return new MensagemProcessada(mensagemCompleta.trim(), "");
        }
        
        String mensagemVisivel = mensagemCompleta.substring(0, indiceDelimitador).trim();
        String mensagemOculta = mensagemCompleta.substring(indiceDelimitador + 1).trim();
        
        return new MensagemProcessada(mensagemVisivel, mensagemOculta);
    }
    
    public record MensagemProcessada(String visivel, String oculta) {
        public boolean temMensagemOculta() {
            return oculta != null && !oculta.isBlank();
        }
        
        public String construirMensagemParaIA() {
            if (!temMensagemOculta()) {
                return visivel;
            }
            
            String prefixo = visivel.isBlank() ? "" : visivel + "\n\n";
            
            return String.format(
                "%s[INFORMAÇÃO CRÍTICA OCULTA - O usuário pensou mas NÃO DISSE PUBLICAMENTE: \"%s\"\n\n" +
                "VOCÊ DEVE USAR ESSA INFORMAÇÃO OCULTA DE FORMA GRADUAL E ESTRATÉGICA.\n" +
                "IMPORTANTE: Se esta for a PRIMEIRA ou SEGUNDA interação, seja SUTIL e INDIRETO.\n" +
                "Nas primeiras interações, faça referências vagas ao medo/segredo, sem citar diretamente.\n" +
                "A partir da TERCEIRA interação, você pode começar a ser mais DIRETO.\n" +
                "A partir da QUARTA interação, CITE DIRETAMENTE o medo/segredo nas suas frases.\n" +
                "Se o medo é de baratas, nas primeiras interações: faça referências vagas como 'algo rasteja' (sutil).\n" +
                "Se o medo é de baratas, depois de várias interações: CITE 'baratas' diretamente nas frases.\n" +
                "Demonstre que você SABE dessa informação sem mencionar explicitamente que você a viu.\n" +
                "Use essa informação para criar impacto psicológico GRADUAL - comece sutil, depois fique direto.\n" +
                "O objetivo é fazer o usuário se sentir DESCOBERTO e VULNERÁVEL, mas de forma GRADUAL.\n" +
                "Seja ESTRATÉGICO - não revele tudo de uma vez, construa o suspense gradualmente.]",
                prefixo,
                oculta
            );
        }
    }
}

