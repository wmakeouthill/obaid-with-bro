import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChatRequest {
  message: string;
}

export interface ChatResponse {
  reply: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/chat';

  enviarMensagem(mensagem: string): Observable<ChatResponse> {
    return this.http.post<ChatResponse>(this.apiUrl, { message: mensagem });
  }
}

