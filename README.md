# Diabo Chat - scaffold inicial

Este repositório contém um scaffold inicial para uma aplicação modular com backend em Spring Boot (Java 17, Spring Boot 3.2.3) e frontend em Angular 17.3.0.

Objetivo: uma tela com uma figura que representa o "diabo" no centro e um chat que conversa com o usuário via IA (adapter placeholder já criado).

Regras:
- Seguir estritamente `github-copilot-rules.md` (Java 17, Spring Boot 3.2.3, Angular 17.3.0, uso de `inject()`/`signal()` etc.).

Estrutura:
- `backend/` - aplicação Spring Boot modular (use cases, ports, adapters)
- `frontend/` - app Angular standalone (componente principal usando `signal()`)

Como rodar (desenvolvimento):

Backend

```bash
# no diretório raiz do repo
cd backend
mvn spring-boot:run
```

Frontend

```bash
cd frontend
npm install
# requer Angular CLI / ambiente compatível com Angular 17
npm run start
```

Notas de integração IA

- O adapter `OpenAIAdapter` em `backend` é um placeholder — substitua a implementação por integração com OpenAI, Azure ou outro provedor.
- Recomendo expor variáveis de ambiente para chaves (NUNCA commitar chaves em claro).

Próximos passos sugeridos:
- Implementar autenticação de requests, CORS e configuração de proxy para desenvolvimento Angular → backend
- Implementar integração real com provedor de IA
- Adicionar testes unitários para `ChatUseCase`
# obaid-with-bro