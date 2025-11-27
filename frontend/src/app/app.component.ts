import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { useChat } from './composables/use-chat';
import { Message } from './models/message.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent {
  readonly chat = useChat();

  trackByMessage(index: number, message: Message): string {
    return `${message.from}-${index}-${message.timestamp.getTime()}`;
  }
}
