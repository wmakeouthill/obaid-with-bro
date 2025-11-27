const fs = require('fs');
const path = require('path');

const distDir = path.resolve(__dirname, 'dist');
if (!fs.existsSync(distDir)) fs.mkdirSync(distDir, { recursive: true });

const html = `<!doctype html>
<html lang="pt-br">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Diabo Chat</title>
  <style>
    body{margin:0;font-family:Arial,Helvetica,sans-serif;background:#111;color:#eee}
    .page{display:flex;flex-direction:column;align-items:center;justify-content:center;height:100vh;gap:24px}
    .diabo{width:220px;height:220px;filter:drop-shadow(0 6px 10px rgba(0,0,0,0.6))}
    .chat-area{width:90%;max-width:720px;background:#141414;border-radius:8px;padding:12px}
    .messages{max-height:220px;overflow:auto;display:flex;flex-direction:column;gap:8px}
    .bubble{display:inline-block;padding:8px 12px;border-radius:12px;max-width:70%}
    .user{background:#1a73e8;color:#fff;margin-left:auto}
    .diabo-msg{background:#2b2b2b;color:#f0a0a0;margin-right:auto}
    .composer{display:flex;gap:8px;margin-top:8px}
    .composer input{flex:1;padding:8px;border-radius:6px;border:1px solid #222;background:#0e0e0e;color:#eee}
    .composer button{padding:8px 14px;border-radius:6px;background:#a00000;border:none;color:white;cursor:pointer}
    .composer button:disabled{opacity:.5;cursor:not-allowed}
  </style>
</head>
<body>
  <div class="page">
    <div class="diabo-area">
      <svg class="diabo" viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Figura do diabo">
        <circle cx="100" cy="100" r="80" fill="#a00000" />
        <circle cx="70" cy="80" r="10" fill="#000" />
        <circle cx="130" cy="80" r="10" fill="#000" />
        <path d="M60 60 C50 40, 30 30, 20 20" stroke="#600" stroke-width="6" fill="none"/>
        <path d="M140 60 C150 40, 170 30, 180 20" stroke="#600" stroke-width="6" fill="none"/>
        <path d="M70 140 C90 160, 110 160, 130 140" stroke="#300" stroke-width="8" fill="none" stroke-linecap="round"/>
      </svg>
    </div>
    <div class="chat-area">
      <div id="messages" class="messages"></div>
      <div class="composer">
        <input id="input" placeholder="Fale com o diabo..." />
        <button id="send">Enviar</button>
      </div>
    </div>
  </div>

  <script>
    const messagesEl = document.getElementById('messages');
    const inputEl = document.getElementById('input');
    const sendBtn = document.getElementById('send');

    function addMessage(text, from){
      const div = document.createElement('div');
      const span = document.createElement('div');
      span.className = 'bubble ' + (from === 'user' ? 'user' : 'diabo-msg');
      span.textContent = text;
      div.appendChild(span);
      messagesEl.appendChild(div);
      messagesEl.scrollTop = messagesEl.scrollHeight;
    }

    sendBtn.addEventListener('click', async ()=>{
      const text = inputEl.value.trim();
      if(!text) return;
      addMessage(text, 'user');
      inputEl.value = '';
      try{
        const res = await fetch('/api/chat', {
          method: 'POST', headers: {'Content-Type':'application/json'},
          body: JSON.stringify({ message: text })
        });
        if(!res.ok) throw new Error('Network');
        const data = await res.json();
        addMessage(data.reply || 'Sem resposta', 'diabo');
      } catch(e){
        addMessage('Erro ao comunicar com IA.', 'diabo');
      }
    });

    inputEl.addEventListener('keydown', (e)=>{ if(e.key === 'Enter') sendBtn.click(); });
  </script>
</body>
</html>`;

fs.writeFileSync(path.join(distDir, 'index.html'), html, 'utf8');
console.log('Static dist generated in', distDir);
