import { Component, ChangeDetectionStrategy, ViewChild, ElementRef, AfterViewInit, AfterViewChecked } from '@angular/core';
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
export class AppComponent implements AfterViewInit, AfterViewChecked {
  readonly chat = useChat();

  @ViewChild('messagesContainer', { static: false }) messagesContainer?: ElementRef<HTMLDivElement>;
  
  private previousMessagesLength = 0;

  ngAfterViewInit(): void {
    this.previousMessagesLength = this.chat.messages().length;
    this.scrollToBottom();
  }

  ngAfterViewChecked(): void {
    const currentLength = this.chat.messages().length;
    if (currentLength !== this.previousMessagesLength || this.chat.isLoading()) {
      this.previousMessagesLength = currentLength;
      this.scrollToBottom();
    }
  }

  trackByMessage(index: number, message: Message): string {
    return `${message.from}-${index}-${message.timestamp.getTime()}`;
  }

  private scrollToBottom(): void {
    if (this.messagesContainer?.nativeElement) {
      requestAnimationFrame(() => {
        const element = this.messagesContainer!.nativeElement;
        element.scrollTop = element.scrollHeight;
      });
    }
  }

  onVideoEnded(): void {
    this.chat.showVideo.set(false);
  }
}
