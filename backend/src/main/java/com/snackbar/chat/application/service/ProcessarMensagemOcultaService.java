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
                "%s\n\n[CONTEXTO OCULTO - o usuário pensou mas não disse: %s]",
                visivel,
                oculta
            );
        }
    }
}

