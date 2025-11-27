import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

type From = 'user' | 'diabo';
type Message = { from: From; text: string };

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  private readonly http = inject(HttpClient);

  readonly messages = signal<Message[]>([]);
  readonly input = signal('');
  readonly canSend = computed(() => this.input().trim().length > 0);

  send() {
    const text = this.input().trim();
    if (!text) return;
    this.messages.update(arr => [...arr, { from: 'user', text }]);
    this.input.set('');

    // chama backend
    this.http.post<{ reply: string }>('/api/chat', { message: text })
      .subscribe({
        next: res => this.messages.update(arr => [...arr, { from: 'diabo', text: res.reply }]),
        error: () => this.messages.update(arr => [...arr, { from: 'diabo', text: 'Erro ao comunicar com IA.' }])
      });
  }
}
