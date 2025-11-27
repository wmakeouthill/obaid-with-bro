// Polyfills para a aplicação
// Angular, por padrão, usa Zone.js para change detection quando
// usamos providers como `provideZoneChangeDetection()`.
// Importamos `zone.js` aqui para garantir que `Zone` esteja definido
// antes da inicialização da aplicação, evitando erros como NG0908.
import 'zone.js';
