# 🚀 Deploy no Google Cloud Run - Projeto Obaid

Este documento explica como fazer o deploy do projeto no Google Cloud Run.

## 📋 Pré-requisitos

1. **Google Cloud SDK (gcloud CLI)** instalado
2. **Docker** instalado e rodando
3. **Conta Google Cloud** com projeto criado
4. **Autenticação** configurada: `gcloud auth login`

## 🔐 Secret do Secret Manager

O projeto precisa do seguinte secret configurado:

### `openai-api-key`

**Descrição:** Chave da API da OpenAI para o chat do Obaid  
**Tipo:** String  
**Valor:** Sua chave da OpenAI (formato: `sk-...`)  
**Onde obter:** <https://platform.openai.com/api-keys>

**Comando para criar:**

```bash
echo -n 'sk-sua-chave-aqui' | gcloud secrets create openai-api-key --data-file=- --project=obaid-revival
```

## 🚀 Deploy

### Opção 1: Script Automático (Recomendado)

```powershell
.\deploy-obaid.ps1 obaid-revival southamerica-east1
```

O script irá:

1. ✅ Verificar autenticação
2. ✅ Configurar projeto
3. ✅ Habilitar APIs necessárias
4. ✅ Fazer build da imagem Docker
5. ✅ Fazer push para Container Registry
6. ✅ Verificar se o secret existe
7. ✅ Fazer deploy no Cloud Run

### Opção 2: Deploy Manual

Se preferir fazer o deploy manualmente:

```bash
# 1. Build do frontend (se necessário)
cd frontend
npm run build -- --configuration=production
cd ..

# 2. Build da imagem
docker build -f Dockerfile.cloud-run -t gcr.io/obaid-revival/obaid-revival:latest .

# 3. Push da imagem
docker push gcr.io/obaid-revival/obaid-revival:latest

# 4. Deploy no Cloud Run
gcloud run deploy obaid-revival \
  --image gcr.io/obaid-revival/obaid-revival:latest \
  --region southamerica-east1 \
  --platform managed \
  --allow-unauthenticated \
  --memory 512Mi \
  --cpu 1 \
  --timeout 300 \
  --max-instances 10 \
  --min-instances 0 \
  --port 8080 \
  --set-secrets="OPENAI_API_KEY=openai-api-key:latest" \
  --set-env-vars="SERVER_PORT=8080,SPRING_PROFILES_ACTIVE=prod,LOG_LEVEL=INFO" \
  --project=obaid-revival
```

## ⚙️ Configuração do Cloud Run (Free Tier)

| Configuração | Valor | Descrição |
|--------------|-------|-----------|
| **Memória** | 512Mi | Otimizado para free tier |
| **CPU** | 1 | Free tier |
| **Timeout** | 300s | 5 minutos |
| **Max Instâncias** | 10 | Limite para controle de custos |
| **Min Instâncias** | 0 | Scale to zero (economia) |
| **Porta** | 8080 | Porta padrão do Spring Boot |

## 🔄 Atualizar Secret

```bash
echo -n 'nova-chave' | gcloud secrets versions add openai-api-key --data-file=- --project=obaid-revival
```

## 🔍 Verificar Deploy

```bash
# Status do serviço
gcloud run services describe obaid-revival --region southamerica-east1 --project=obaid-revival

# Logs
gcloud run services logs read obaid-revival --region southamerica-east1 --project=obaid-revival
```

## 🌐 URL da Aplicação

Após o deploy: https://obaid-revival-473639113210.southamerica-east1.run.app
