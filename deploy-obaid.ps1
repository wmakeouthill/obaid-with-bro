# Script PowerShell completo: Build + Push + Deploy para Cloud Run - Projeto Obaid
# Uso: .\deploy-obaid.ps1 [PROJECT_ID] [REGION]
# Exemplo: .\deploy-obaid.ps1 obaid-revival southamerica-east1
#
# IMPORTANTE: Este script é adaptado para o projeto obaid-revival
# - Estrutura: backend Spring Boot + frontend Angular
# - Único secret necessário: OPENAI_API_KEY
# - Sem banco de dados (aplicação stateless)

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Deploy Completo - Cloud Run" -ForegroundColor Cyan
Write-Host "  Projeto Obaid Revival" -ForegroundColor Cyan
Write-Host "  Build + Push + Deploy" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se gcloud esta instalado
$gcloudPath = Get-Command gcloud -ErrorAction SilentlyContinue
if (-not $gcloudPath) {
    Write-Host "ERRO: gcloud CLI nao esta instalado. Instale em: https://cloud.google.com/sdk/docs/install" -ForegroundColor Red
    exit 1
}

# Verificar autenticacao
Write-Host "[1/7] Verificando autenticacao..." -ForegroundColor Green
try {
    $authOutput = gcloud auth list --filter="status:ACTIVE" --format="value(account)" 2>&1
    $activeAccount = ($authOutput | Where-Object { $_ -notmatch 'ERROR|WARNING' } | Select-Object -First 1).ToString().Trim()
    
    if ([string]::IsNullOrWhiteSpace($activeAccount)) {
        Write-Host "ERRO: Voce nao esta autenticado." -ForegroundColor Red
        Write-Host "Execute: gcloud auth login" -ForegroundColor Yellow
        exit 1
    } else {
        Write-Host "OK: Autenticado como: $activeAccount" -ForegroundColor Green
    }
} catch {
    Write-Host "ERRO: Nao foi possivel verificar autenticacao" -ForegroundColor Red
    exit 1
}

# Obter PROJECT_ID
if ($args.Count -eq 0) {
    $PROJECT_ID = "obaid-revival"
    Write-Host "Usando projeto padrao: $PROJECT_ID" -ForegroundColor Yellow
} else {
    $PROJECT_ID = $args[0]
}

# Obter REGION
$REGION = if ($args.Count -gt 1) { $args[1] } else { "southamerica-east1" }

Write-Host ""
Write-Host "[2/7] Configurando projeto: $PROJECT_ID" -ForegroundColor Green
try {
    $currentProject = (gcloud config get-value project 2>&1).ToString().Trim()
    
    if ($currentProject -ne $PROJECT_ID) {
        Write-Host "   Mudando projeto de '$currentProject' para '$PROJECT_ID'" -ForegroundColor Yellow
        gcloud config set project $PROJECT_ID
        if ($LASTEXITCODE -ne 0) {
            Write-Host "ERRO: Nao foi possivel configurar projeto" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "   OK: Projeto ja esta configurado" -ForegroundColor Green
    }
} catch {
    Write-Host "ERRO: Nao foi possivel configurar projeto" -ForegroundColor Red
    exit 1
}

# Verificar e habilitar APIs (com fallback se não tiver permissão)
Write-Host ""
Write-Host "[3/7] Verificando APIs necessarias..." -ForegroundColor Green
$apis = @(
    "containerregistry.googleapis.com",
    "run.googleapis.com",
    "secretmanager.googleapis.com"
)

# Tentar verificar se tem permissão para listar serviços
Write-Host "   Verificando permissoes..." -ForegroundColor Yellow
$testPermission = gcloud services list --enabled --limit=1 --project=$PROJECT_ID 2>&1 | Out-Null
$hasPermission = $LASTEXITCODE -eq 0

if (-not $hasPermission) {
    Write-Host "   AVISO: Sem permissao para verificar status das APIs" -ForegroundColor Yellow
    Write-Host "   Assumindo que as APIs ja estao habilitadas" -ForegroundColor Yellow
    Write-Host "   Continuando com o deploy..." -ForegroundColor Green
} else {
    foreach ($api in $apis) {
        Write-Host "   Verificando $api..." -ForegroundColor Yellow
        $status = gcloud services list --enabled --filter="name:$api" --project=$PROJECT_ID --format="value(name)" 2>&1
        
        if ($status -match $api) {
            Write-Host "   OK: $api ja esta habilitada" -ForegroundColor Green
        } else {
            Write-Host "   Tentando habilitar $api..." -ForegroundColor Yellow
            gcloud services enable $api --project=$PROJECT_ID 2>&1 | Out-Null
        }
    }
}

# Configurar Docker
Write-Host ""
Write-Host "[4/7] Configurando credenciais Docker..." -ForegroundColor Green
gcloud auth configure-docker gcr.io --quiet

# Verificar se o frontend está buildado
Write-Host ""
Write-Host "[5/7] Verificando se o frontend esta buildado..." -ForegroundColor Green
$frontendDistPath = "frontend/dist"
if (-not (Test-Path $frontendDistPath)) {
    Write-Host "   AVISO: Frontend nao esta buildado em $frontendDistPath" -ForegroundColor Yellow
    Write-Host "   Fazendo build do frontend agora..." -ForegroundColor Yellow
    Push-Location frontend
    npm run build -- --configuration=production
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERRO: Falha no build do frontend" -ForegroundColor Red
        Pop-Location
        exit 1
    }
    Pop-Location
    Write-Host "   OK: Frontend buildado com sucesso" -ForegroundColor Green
} else {
    Write-Host "   OK: Frontend ja esta buildado" -ForegroundColor Green
}

# Build da imagem
Write-Host ""
Write-Host "[6/7] Fazendo build da imagem Docker..." -ForegroundColor Green
$IMAGE_NAME = "gcr.io/$PROJECT_ID/obaid-revival:latest"
$TIMESTAMP_TAG = "gcr.io/$PROJECT_ID/obaid-revival:$(Get-Date -Format 'yyyyMMddHHmmss')"

Write-Host "   Usando Dockerfile.cloud-run" -ForegroundColor Yellow
Write-Host "   O Docker vai copiar o frontend ja buildado (nao vai buildar novamente)" -ForegroundColor Yellow
Write-Host "   Isso pode levar varios minutos..." -ForegroundColor Yellow

docker build -f Dockerfile.cloud-run -t $IMAGE_NAME -t $TIMESTAMP_TAG .

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Falha no build da imagem" -ForegroundColor Red
    exit 1
}

Write-Host "   OK: Build concluido" -ForegroundColor Green

# Push da imagem
Write-Host ""
Write-Host "[7/7] Fazendo push da imagem para Container Registry..." -ForegroundColor Green
Write-Host "   Isso pode levar alguns minutos..." -ForegroundColor Yellow

docker push $IMAGE_NAME
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Falha no push da imagem $IMAGE_NAME" -ForegroundColor Red
    exit 1
}

docker push $TIMESTAMP_TAG
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Falha no push da imagem $TIMESTAMP_TAG" -ForegroundColor Red
    exit 1
}

Write-Host "   OK: Push concluido" -ForegroundColor Green

# Informacoes finais
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  Build e Push Concluidos!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Imagem: $IMAGE_NAME" -ForegroundColor Cyan
Write-Host "Tag com timestamp: $TIMESTAMP_TAG" -ForegroundColor Cyan
Write-Host ""

# Perguntar se deseja fazer deploy automatico
$deploy = Read-Host "Deseja fazer deploy automatico no Cloud Run agora? (S/N)"
if ($deploy -eq "S" -or $deploy -eq "s" -or $deploy -eq "Y" -or $deploy -eq "y") {
    Write-Host ""
    Write-Host "Fazendo deploy no Cloud Run..." -ForegroundColor Green
    
    # Verificar se o secret existe
    Write-Host "   Verificando secret 'openai-api-key'..." -ForegroundColor Yellow
    $secretCheck = gcloud secrets describe openai-api-key --project=$PROJECT_ID 2>&1 | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "   AVISO: Secret 'openai-api-key' nao encontrado!" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "   Crie o secret com o comando:" -ForegroundColor Yellow
        Write-Host "   echo -n 'sk-...' | gcloud secrets create openai-api-key --data-file=- --project=$PROJECT_ID" -ForegroundColor Cyan
        exit 1
    } else {
        Write-Host "   OK: Secret 'openai-api-key' encontrado" -ForegroundColor Green
    }
    
    # Fazer deploy (configurado para free tier: 512Mi memória, 1 CPU)
    Write-Host ""
    Write-Host "   Fazendo deploy no Cloud Run..." -ForegroundColor Green
    
    gcloud run deploy obaid-revival `
        --image $IMAGE_NAME `
        --region $REGION `
        --platform managed `
        --allow-unauthenticated `
        --memory 512Mi `
        --cpu 1 `
        --timeout 300 `
        --max-instances 10 `
        --min-instances 0 `
        --port 8080 `
        --set-secrets="OPENAI_API_KEY=openai-api-key:latest" `
        --set-env-vars="SERVER_PORT=8080,SPRING_PROFILES_ACTIVE=prod,LOG_LEVEL=INFO" `
        --project=$PROJECT_ID
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "OK: Deploy concluido com sucesso!" -ForegroundColor Green
        $SERVICE_URL = gcloud run services describe obaid-revival --region $REGION --format="value(status.url)" --project=$PROJECT_ID
        Write-Host "URL do servico: $SERVICE_URL" -ForegroundColor Cyan
    } else {
        Write-Host "ERRO: Falha no deploy" -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  INSTRUCOES: Configurar Secret" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Para criar o secret no Secret Manager, execute o comando abaixo:" -ForegroundColor Yellow
Write-Host ""
Write-Host "OPENAI_API_KEY (chave da API OpenAI):" -ForegroundColor Green
Write-Host "   echo -n 'sk-...' | gcloud secrets create openai-api-key --data-file=- --project=$PROJECT_ID" -ForegroundColor Cyan
Write-Host "   (Obtenha em: https://platform.openai.com/api-keys)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Para atualizar o secret existente:" -ForegroundColor Yellow
Write-Host "   echo -n 'novo-valor' | gcloud secrets versions add openai-api-key --data-file=- --project=$PROJECT_ID" -ForegroundColor Cyan
Write-Host ""
Write-Host "Concluido!" -ForegroundColor Green
