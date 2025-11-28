# Deploy para Obaid Revival - Google Cloud Run

Este documento explica como usar o script de deploy adaptado para o projeto **obaid-revival** no Google Cloud Run.

## 📋 Diferenças do Projeto Original

O projeto **obaid-revival** tem uma estrutura simplificada em relação ao projeto original:

### Estrutura Simplificada
- ✅ **Backend único**: Apenas um módulo Maven (`diabo-chat-backend`)
- ✅ **Frontend Angular**: Standalone, sem múltiplos módulos
- ✅ **Stateless**: Aplicação sem banco de dados
- ✅ **Credencial única**: Apenas `OPENAI_API_KEY` (API do ChatGPT)

### Removido do Deploy Original
- ❌ Múltiplos módulos Maven (kernel-compartilhado, gestao-cardapio, etc.)
- ❌ Banco de dados (Cloud SQL, MySQL)
- ❌ Secrets de DB (db-password)
- ❌ Secrets de JWT (jwt-secret)
- ❌ Configurações de Cloud SQL Connection

## 📁 Arquivos Criados

1. **`Dockerfile.cloud-run.obaid-revival`**: Dockerfile adaptado para a estrutura simplificada
2. **`deploy-completo-obaid-revival.ps1`**: Script PowerShell adaptado para o projeto obaid-revival

## 🚀 Como Usar

### Pré-requisitos

1. **Google Cloud SDK instalado**:
   ```powershell
   # Verificar instalação
   gcloud --version
   ```

2. **Autenticação configurada**:
   ```powershell
   gcloud auth login
   ```

3. **Docker instalado e rodando**

### Passo 1: Criar o Secret no Secret Manager

Antes de fazer o deploy, você precisa criar o secret com a chave da API OpenAI:

```powershell
# Criar o secret
echo -n 'sk-sua-chave-aqui' | gcloud secrets create openai-api-key --data-file=- --project=obaid-revival

# Ou adicionar uma nova versão se já existir
echo -n 'sk-sua-chave-aqui' | gcloud secrets versions add openai-api-key --data-file=- --project=obaid-revival
```

### Passo 2: Executar o Script de Deploy

```powershell
# Com parâmetros
.\deploy-completo-obaid-revival.ps1 obaid-revival southamerica-east1

# Ou sem parâmetros (o script pedirá interativamente)
.\deploy-completo-obaid-revival.ps1
```

### Passo 3: O Script Executará

1. ✅ Verificação de autenticação
2. ✅ Configuração do projeto GCP
3. ✅ Habilitação de APIs necessárias
4. ✅ Configuração de credenciais Docker
5. ✅ Build da imagem Docker
6. ✅ Push da imagem para Container Registry
7. ✅ Deploy no Cloud Run (opcional, se você escolher)

## 🔧 Configurações do Cloud Run

O script configura automaticamente (otimizado para **Free Tier**):

- **Service Name**: `obaid-revival`
- **Memory**: 512Mi (free tier: até 200 horas/mês gratuitas)
- **CPU**: 1 (free tier)
- **Timeout**: 300 segundos
- **Max Instances**: 10
- **Min Instances**: 0
- **Port**: 8080
- **Secrets**: `OPENAI_API_KEY=openai-api-key:latest`
- **Environment Variables**:
  - `SERVER_PORT=8080`
  - `SPRING_PROFILES_ACTIVE=prod`
  - `LOG_LEVEL=INFO`

### 💰 Free Tier Limits

- **360.000 GiB-segundos de memória/mês** (gratuito)
- Com 512Mi: até **~200 horas/mês** gratuitas
- Com 1Gi: até **~100 horas/mês** gratuitas
- **2 milhões de requisições/mês** (gratuito)

## 📝 Estrutura do Dockerfile

O Dockerfile usa multi-stage build:

1. **Stage 1 (frontend-build)**: Build do Angular
   - Instala dependências
   - Builda o frontend para `dist/`

2. **Stage 2 (maven-build)**: Build do backend
   - Copia o frontend buildado para `../frontend/dist` (relativo ao backend)
   - O Maven copia automaticamente para `resources/static/` via plugin
   - Gera o JAR final com frontend incluído

3. **Stage 3 (runtime)**: Imagem final otimizada
   - Apenas JRE 17
   - JAR da aplicação
   - Usuário não-root para segurança

## 🔐 Segurança

- ✅ Secrets gerenciados via Google Secret Manager
- ✅ Usuário não-root no container
- ✅ Imagem Alpine (menor superfície de ataque)
- ✅ Aplicação stateless (sem dados sensíveis em disco)

## 🐛 Troubleshooting

### Erro: "Secret não encontrado"
```powershell
# Criar o secret manualmente
gcloud secrets create openai-api-key --project=obaid-revival
echo -n 'sk-...' | gcloud secrets versions add openai-api-key --data-file=- --project=obaid-revival
```

### Erro: "Build do Docker falhou"
- Verifique se o Docker está rodando
- Verifique se há espaço em disco suficiente
- Verifique os logs do build: `docker build -f Dockerfile.cloud-run.obaid-revival .`

### Erro: "Push falhou"
- Verifique as credenciais: `gcloud auth configure-docker gcr.io`
- Verifique se o projeto está correto: `gcloud config get-value project`

### Erro: "Deploy falhou"
- Verifique se o secret existe: `gcloud secrets list --project=obaid-revival`
- Verifique se as APIs estão habilitadas: `gcloud services list --enabled --project=obaid-revival`

## 📚 Referências

- [Google Cloud Run Documentation](https://cloud.google.com/run/docs)
- [Secret Manager Documentation](https://cloud.google.com/secret-manager/docs)
- [Container Registry Documentation](https://cloud.google.com/container-registry/docs)

## ⚠️ Notas Importantes

1. **Nunca commite a chave da API** no repositório
2. **Use sempre Secret Manager** para credenciais
3. **O frontend é incluído no JAR** durante o build do Maven
4. **A aplicação é stateless** - não há banco de dados
5. **O script cria o secret automaticamente** se você escolher fazer deploy e o secret não existir

