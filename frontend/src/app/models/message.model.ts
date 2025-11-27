export type MessageFrom = 'user' | 'diabo';

export interface Message {
  from: MessageFrom;
  text: string;
  timestamp: Date;
}

