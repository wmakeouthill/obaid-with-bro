# Pasta `container` — configs locais

Este diretório contém arquivos de configuração destinados ao uso local e/ou para montar como ConfigMap em ambientes de containerização.

- **`configmap-local.properties`**: arquivo de template para adicionar a chave `openai.api.key` localmente.

Como usar (desenvolvimento):

1. Coloque sua chave no arquivo (não commite):

```properties
openai.api.key=sk-...sua-chave...
```

2. Inicie o backend apontando para este arquivo como fonte adicional de propriedades:

```bash
cd backend
mvn spring-boot:run -Dspring.config.additional-location=file:./container/configmap-local.properties
```

Alternativas:
- Exportar a variável de ambiente `OPENAI_API_KEY` antes de iniciar o backend.
- Em k8s, monte `configmap-local.properties` como ConfigMap e injete no pod em `/app/container/configmap-local.properties` e use `spring.config.additional-location` conforme necessário.

**Importante:** Nunca adicione chaves reais ao repositório. Use este arquivo apenas localmente e mantenha-o ignorado pelo Git.

Observação: o `pom.xml` do backend foi configurado para que o comando `mvn spring-boot:run` carregue automaticamente o `container/configmap-local.properties` como local adicional (via `spring.config.additional-location`), portanto o passo acima não é obrigatório se você executar o comando normal `mvn spring-boot:run` no módulo `backend`.
