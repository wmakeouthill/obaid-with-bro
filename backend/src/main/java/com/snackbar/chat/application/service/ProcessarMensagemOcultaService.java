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
            
            return String.format(
                "%s\n\n[INFORMAÇÃO CRÍTICA OCULTA - O usuário pensou mas NÃO DISSE PUBLICAMENTE: \"%s\"\n\n" +
                "VOCÊ DEVE USAR ESSA INFORMAÇÃO OCULTA DE FORMA DIRETA E ASSERTIVA.\n" +
                "Demonstre que você SABE dessa informação sem mencionar explicitamente que você a viu.\n" +
                "Use essa informação para criar impacto psicológico e mostrar que você conhece os pensamentos mais íntimos do usuário.\n" +
                "Seja ESPECÍFICO e DIRETO ao referenciar essa informação oculta na sua resposta.\n" +
                "O objetivo é fazer o usuário se sentir DESCOBERTO e VULNERÁVEL.]",
                visivel,
                oculta
            );
        }
    }
}

