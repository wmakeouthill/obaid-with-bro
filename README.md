<div align="center">

# 👹 OBAID - O Diabo que Conhece Seus Medos

<img src="frontend/src/assets/obaid.png" alt="Obaid" width="200"/>

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17.3.0-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)
[![OpenAI](https://img.shields.io/badge/OpenAI-GPT--3.5-412991?style=for-the-badge&logo=openai&logoColor=white)](https://openai.com/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4.2-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

**Uma aplicação de chat com IA que interpreta uma entidade demoníaca manipuladora, capaz de descobrir e explorar os medos mais profundos de quem conversa com ela.**

[🎮 Como Brincar](#-como-brincar-com-o-obaid) • [🛠️ Instalação](#️-instalação) • [🏗️ Arquitetura](#️-arquitetura) • [📊 Estatísticas](#-estatísticas-do-projeto)

</div>

---

## 📖 Sobre o Projeto

O **OBAID** é um chatbot com personalidade de uma entidade demoníaca antiga e manipuladora. Ele conversa com os usuários de forma envolvente, criando tensão psicológica através de humor negro, sarcasmo e insinuações. O diferencial do Obaid é sua capacidade de receber **dicas ocultas** que ficam invisíveis na conversa, permitindo criar situações surpreendentes e assustadoras.

### ✨ Funcionalidades Principais

- 🎭 **Personalidade Única**: Obaid é manipulador, estratégico e usa psicologia reversa
- 🔮 **Dicas Ocultas**: Sistema de mensagens secretas que alimentam a IA sem aparecer na conversa
- 🎬 **Animação de Susto**: Vídeo especial quando Obaid revela o medo da pessoa
- 📱 **Design Responsivo**: Funciona perfeitamente em desktop e mobile
- 🧠 **Memória de Contexto**: Obaid lembra de todas as informações ocultas durante a conversa
- 🎯 **Revelação Gradual**: Estratégia de manipulação que constrói tensão progressivamente

---

## 🎮 Como Brincar com o OBAID

O OBAID foi criado para **assustar seus amigos** de forma divertida! Aqui está como funciona:

### 🎯 O Truque das Dicas Ocultas

A mágica do Obaid está no sistema de **mensagens ocultas**. Tudo que você digitar **após o ponto e vírgula ( `;` )** fica **invisível na conversa**, mas a IA recebe a informação!

#### Exemplo Prático:

```
Você digita:    Olá, tudo bem? ; ele tem medo de baratas
```

```
Aparece na tela: Olá, tudo bem?
```

**A IA recebe**: `Olá, tudo bem? [INFORMAÇÃO CRÍTICA OCULTA: ele tem medo de baratas]`

### 📋 Passo a Passo para Assustar um Amigo

1. **Prepare a vítima**: Chame seu amigo para "testar um chatbot de IA"
2. **Dê a primeira dica oculta**: 
   ```
   Oi ; meu amigo que está digitando tem pavor de aranhas, ele nunca contou isso pra ninguém
   ```
3. **Deixe a conversa rolar**: O Obaid vai começar a fazer insinuações sutis
4. **Alimente mais contexto durante a conversa**:
   ```
   Que legal ; ele também tem medo do escuro desde criança
   ```
5. **Veja a mágica acontecer**: Obaid vai gradualmente revelar que "sabe" coisas que seu amigo nunca contou!

### 🎭 Estratégia do Obaid

O Obaid usa uma estratégia de **revelação gradual**:

| Interação | Comportamento |
|-----------|---------------|
| 1ª-2ª | Insinuações muito sutis, desperta curiosidade |
| 3ª-4ª | Referências mais específicas, ainda sem citar diretamente |
| 5ª+ | Revela diretamente o medo da pessoa |
| Se perguntarem | Revela tudo de forma assertiva |

### 💡 Dicas para Maximizar o Susto

- **Seja específico**: Quanto mais detalhes você der, mais assustador fica
- **Dê múltiplas dicas**: Você pode enviar mensagens só com `;` para adicionar contexto sem aparecer nada na tela
- **Use informações pessoais**: Nome de parentes, lugares, eventos específicos
- **Deixe a vítima curiosa**: Quando ela perguntar "como você sabe disso?", a revelação é ainda melhor!

### ⚡ Animação de Susto

Quando o Obaid finalmente **cita diretamente o medo** que você revelou ocultamente, uma **animação especial** é ativada para aumentar o impacto!

---

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** e **Hexagonal Architecture**:

```
┌─────────────────────────────────────────────────────────────┐
│                         FRONTEND                            │
│                      Angular 17.3.0                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐│
│  │ Components  │ │ Composables │ │      Services           ││
│  │(Standalone) │ │ (Signals)   │ │  (HttpClient)           ││
│  └─────────────┘ └─────────────┘ └─────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼ REST API (/api/chat)
┌─────────────────────────────────────────────────────────────┐
│                         BACKEND                             │
│                    Spring Boot 3.2.3                        │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                   Infrastructure                        ││
│  │  ┌───────────────┐  ┌────────────────────────────────┐ ││
│  │  │ ChatController│  │       OpenAIAdapter            │ ││
│  │  │   (REST)      │  │  (GPT-3.5 Turbo Integration)   │ ││
│  │  └───────────────┘  └────────────────────────────────┘ ││
│  └─────────────────────────────────────────────────────────┘│
│  ┌─────────────────────────────────────────────────────────┐│
│  │                    Application                          ││
│  │  ┌───────────────┐  ┌────────────────────────────────┐ ││
│  │  │  ChatUseCase  │  │ProcessarMensagemOcultaService │ ││
│  │  │               │  │   (Separar visível/oculto)    │ ││
│  │  └───────────────┘  └────────────────────────────────┘ ││
│  └─────────────────────────────────────────────────────────┘│
│  ┌─────────────────────────────────────────────────────────┐│
│  │                      Domain                             ││
│  │  ┌───────────────────┐  ┌───────────────────────────┐  ││
│  │  │ ObaidPromptService│  │     MensagemChat          │  ││
│  │  │ (Personalidade)   │  │    (Entity)               │  ││
│  │  └───────────────────┘  └───────────────────────────┘  ││
│  └─────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       OpenAI API                            │
│                    (GPT-3.5 Turbo)                          │
└─────────────────────────────────────────────────────────────┘
```

### 📁 Estrutura de Diretórios

```
obaid-with-bro/
├── 📂 backend/                    # Spring Boot Application
│   ├── 📂 src/main/java/com/snackbar/chat/
│   │   ├── 📂 application/        # Use Cases, DTOs, Ports, Services
│   │   ├── 📂 domain/             # Entities, Domain Services
│   │   └── 📂 infrastructure/     # Controllers, AI Adapter
│   └── 📄 pom.xml
├── 📂 frontend/                   # Angular Application
│   ├── 📂 src/app/
│   │   ├── 📂 composables/        # use-chat.ts (Signal-based state)
│   │   ├── 📂 models/             # TypeScript interfaces
│   │   └── 📂 services/           # HTTP services
│   └── 📄 package.json
├── 📄 Dockerfile.cloud-run        # Deploy para Google Cloud Run
└── 📄 deploy-completo-obaid-revival.ps1  # Script de deploy
```

---

## 🛠️ Instalação

### Pré-requisitos

- **Java 17** (JDK)
- **Maven 3.8+**
- **Node.js 18+** (opcional, Maven instala automaticamente)
- **Chave da API OpenAI**

### Configuração Rápida

1. **Clone o repositório**
```bash
git clone https://github.com/Barbosa-Blue/obaid-with-bro.git
cd obaid-with-bro
```

2. **Configure a chave da OpenAI**
```bash
export OPENAI_API_KEY="sk-sua-chave-aqui"
```

3. **Execute o backend (inclui build do frontend)**
```bash
cd backend
mvn spring-boot:run
```

4. **Acesse a aplicação**
```
http://localhost:8080
```

### Desenvolvimento Frontend Separado

```bash
cd frontend
npm install
npm run start
# Acesse http://localhost:4200
```

### Build para Produção

```bash
cd backend
mvn -DskipTests package
java -jar target/diabo-chat-backend-0.1.0.jar
```

---

## ☁️ Deploy no Google Cloud Run

O projeto inclui configuração completa para deploy no Google Cloud Run:

```powershell
# PowerShell (Windows)
.\deploy-completo-obaid-revival.ps1 obaid-revival southamerica-east1
```

**Configurações otimizadas para Free Tier:**
- 512Mi de memória (~200 horas/mês gratuitas)
- 1 vCPU
- Secrets via Google Secret Manager

Consulte [DEPLOY-OBAID-REVIVAL.md](./DEPLOY-OBAID-REVIVAL.md) para instruções detalhadas.

---

## 🛡️ Stack Tecnológica

| Camada | Tecnologia | Versão |
|--------|------------|--------|
| **Frontend** | Angular (Standalone Components) | 17.3.0 |
| **Frontend** | TypeScript | 5.4.2 |
| **Frontend** | RxJS | 7.8.0 |
| **Backend** | Java | 17 |
| **Backend** | Spring Boot | 3.2.3 |
| **Backend** | Lombok | Latest |
| **AI** | OpenAI GPT | 3.5-turbo |
| **Build** | Maven + frontend-maven-plugin | 1.12.1 |
| **Container** | Docker (Alpine + JRE 17) | - |
| **Cloud** | Google Cloud Run | - |

---

## 📊 Estatísticas do Projeto

### 👥 Contribuidores

| Contribuidor | Commits |
|--------------|---------|
| [@wmakeouthill](https://github.com/wmakeouthill) | 10 |
| Wesley de Carvalho Augusto Correia | 6 |
| José Rhuan Rogerio | 4 |

### 📈 Issues

![GitHub Issues](https://img.shields.io/github/issues/Barbosa-Blue/obaid-with-bro?style=flat-square)
![GitHub Closed Issues](https://img.shields.io/github/issues-closed/Barbosa-Blue/obaid-with-bro?style=flat-square)

| Issue | Status | Descrição |
|-------|--------|-----------|
| [#18](https://github.com/Barbosa-Blue/obaid-with-bro/issues/18) | 🟢 Aberta | Ajuste responsividade do chat no mobile |
| [#16](https://github.com/Barbosa-Blue/obaid-with-bro/issues/16) | ✅ Fechada | Animação de vídeo quando revelar o medo |
| [#15](https://github.com/Barbosa-Blue/obaid-with-bro/issues/15) | ✅ Fechada | Ajuste final de personalidade |
| [#13](https://github.com/Barbosa-Blue/obaid-with-bro/issues/13) | ✅ Fechada | Personalidade precisando de refino |
| [#11](https://github.com/Barbosa-Blue/obaid-with-bro/issues/11) | ✅ Fechada | Criar imagem com IA do Obaid moderno |
| [#10](https://github.com/Barbosa-Blue/obaid-with-bro/issues/10) | ✅ Fechada | Ajustes de layout e hover |
| [#9](https://github.com/Barbosa-Blue/obaid-with-bro/issues/9) | ✅ Fechada | Scroll automático nas mensagens |
| [#8](https://github.com/Barbosa-Blue/obaid-with-bro/issues/8) | ✅ Fechada | Personalidade do Obaid |
| [#6](https://github.com/Barbosa-Blue/obaid-with-bro/issues/6) | ✅ Fechada | API key da OpenAI não reconhecida |
| [#4](https://github.com/Barbosa-Blue/obaid-with-bro/issues/4) | ✅ Fechada | Funcionalidade de dicas ocultas |
| [#3](https://github.com/Barbosa-Blue/obaid-with-bro/issues/3) | ✅ Fechada | Obaid sem personalidade |
| [#2](https://github.com/Barbosa-Blue/obaid-with-bro/issues/2) | ✅ Fechada | Frontend não servido corretamente |

### 🔀 Pull Requests

![GitHub Pull Requests](https://img.shields.io/github/issues-pr/Barbosa-Blue/obaid-with-bro?style=flat-square)
![GitHub Closed Pull Requests](https://img.shields.io/github/issues-pr-closed/Barbosa-Blue/obaid-with-bro?style=flat-square)

| PR | Título | Branch | Status |
|----|--------|--------|--------|
| [#19](https://github.com/Barbosa-Blue/obaid-with-bro/pull/19) | Ajuste responsividade celular | `branche-wesley-develop` | ✅ Merged |
| [#17](https://github.com/Barbosa-Blue/obaid-with-bro/pull/17) | Script CI/CD de deploy completo | `branche-wesley-develop` | ✅ Merged |
| [#14](https://github.com/Barbosa-Blue/obaid-with-bro/pull/14) | Ajuste fino prompts | `branche-wesley-develop` | ✅ Merged |
| [#12](https://github.com/Barbosa-Blue/obaid-with-bro/pull/12) | MVP completo | `branche-wesley-develop` | ✅ Merged |
| [#7](https://github.com/Barbosa-Blue/obaid-with-bro/pull/7) | API fix | `branche-wesley-develop` | ✅ Merged |
| [#5](https://github.com/Barbosa-Blue/obaid-with-bro/pull/5) | Correções iniciais | `main` | ✅ Merged |
| [#1](https://github.com/Barbosa-Blue/obaid-with-bro/pull/1) | Commit inicial | `branche-wesley-develop` | ✅ Merged |

---

## 🔐 Segurança

- ⚠️ **NUNCA** commite sua chave da API OpenAI no repositório
- Use variáveis de ambiente: `OPENAI_API_KEY`
- Para produção, use Google Secret Manager ou similar
- Usuário não-root nos containers Docker

---

## 📜 Licença

Este projeto é privado e pertence a [@wmakeouthill](https://github.com/wmakeouthill).

---

<div align="center">

**Feito com 👹 por [wmakeouthill](https://github.com/wmakeouthill)**

*"Eu conheço seus medos... melhor do que você imagina."*

</div>
