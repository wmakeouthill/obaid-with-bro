# 🎯 Diretrizes de Desenvolvimento - Sistema Snackbar

## ⚠️ PRINCÍPIOS INEGOCIÁVEIS

Estas diretrizes são **OBRIGATÓRIAS** e devem ser seguidas em **TODAS** as implementações.

---

## 1️⃣ STACK TECNOLÓGICA PADRÃO

### Frontend (Angular 17+)

- **Angular**: 17.3.0 ou superior
- **TypeScript**: 5.4.2 ou superior
- **RxJS**: 7.8.0
- **Sintaxe Moderna OBRIGATÓRIA**: `inject()`, `signal()`, `computed()`, `input()`, `output()`, `effect()`
- **HTML Moderno**: Nova sintaxe `@if`, `@for`, `@switch`, `@defer`
- **Standalone Components**: SEMPRE usar componentes standalone
- **Change Detection**: OnPush quando possível

### Backend (Java 17)

- **Java**: 17 (exatamente esta versão)
- **Spring Boot**: 3.2.3 ou superior
- **Liquibase**: 4.25.0 para migrações de banco
- **Maven**: 3.8+ como build tool
- **MySQL**: 8.0+ como banco de dados

---

## 2️⃣ CLEAN CODE - REGRAS OBRIGATÓRIAS

### 2.1 Código Enxuto - MANTENHA CLASSES PEQUENAS

- ✅ **SEMPRE** mantenha código enxuto e classes pequenas
- ✅ Máximo **300 linhas** por classe (exceto casos muito especiais justificados)
- ✅ Se passar de 300 linhas, **OBRIGATÓRIO** refatorar dividindo em classes menores
- ✅ **SEMPRE** aplique Single Responsibility Principle (SRP) rigorosamente
- ✅ **SEMPRE** extraia métodos/composables quando possível ao invés de aumentar a classe
- ✅ Prefira composição sobre herança quando necessário dividir responsabilidades
- ✅ Quebre classes grandes em módulos/composables menores
- ❌ **NUNCA** crie classes "God Classes" com múltiplas responsabilidades
- ❌ **NUNCA** deixe classes crescerem sem controle - refatore continuamente
- ❌ **NUNCA** justifique classes grandes com "está tudo relacionado" - divida mesmo assim

**Benefícios de classes enxutas:**

- ✅ Mais fácil de entender e manter
- ✅ Mais fácil de testar
- ✅ Melhor reutilização
- ✅ Menos bugs
- ✅ Código mais limpo e profissional

### 2.2 Nomenclatura

- ✅ **SEMPRE** use nomes descritivos e autoexplicativos
- ✅ Classes: PascalCase (ex: `ProdutoService`, `CriarProdutoUseCase`)
- ✅ Métodos: camelCase verbos (ex: `buscarProdutoPorId`, `salvarProduto`)
- ✅ Variáveis: camelCase (ex: `produtoSelecionado`, `listaCategorias`)
- ✅ Constantes: UPPER_SNAKE_CASE (ex: `MAX_TAMANHO_IMAGEM`, `API_BASE_URL`)
- ❌ **NUNCA** use abreviações (ex: `prod`, `cat`, `svc`)
- ❌ **NUNCA** use nomes genéricos (ex: `data`, `info`, `util`, `helper` sem contexto)

### 2.3 Tamanho de Funções/Métodos

- ✅ Máximo **20 linhas** por método (exceto casos especiais justificados)
- ✅ Se passar de 20 linhas, **OBRIGATÓRIO** extrair para métodos privados
- ✅ Métodos devem fazer **UMA ÚNICA** coisa bem feita

### 2.4 Responsabilidade Única

- ✅ Uma classe = uma responsabilidade
- ✅ Um método = uma ação específica
- ❌ **NUNCA** misture lógica de negócio com apresentação
- ❌ **NUNCA** misture acesso a dados com validação

### 2.5 Comentários

- ✅ **APENAS** comentários que explicam **PORQUÊ**, não **O QUÊ**
- ✅ Documentação JavaDoc para APIs públicas
- ❌ **NUNCA** comente código óbvio ou autoexplicativo
- ✅ Se precisa comentar, refatore para tornar o código mais claro

### 2.6 Tratamento de Erros

- ✅ Use exceções específicas (não genéricas)
- ✅ Mensagens de erro claras e acionáveis
- ✅ **SEMPRE** trate exceções, nunca deixe `catch` vazio
- ✅ Use try-with-resources no Java quando apropriado

---

## 3️⃣ CLEAN ARCHITECTURE - ESTRUTURA OBRIGATÓRIA

### 3.1 Camadas (Backend Java)

```estrutura
Domain (Núcleo)
  └─ Entidades, Value Objects, Regras de Negócio
  └─ ❌ NUNCA depende de frameworks

Application (Casos de Uso)
  └─ Use Cases, DTOs, Ports (Interfaces)
  └─ ✅ Depende apenas de Domain

Infrastructure (Adaptadores)
  └─ Repositories, Mappers, Configurações
  └─ ✅ Implementa interfaces de Application
  └─ ✅ Pode usar Spring, JPA, etc.
```

### 3.2 Regra de Dependência

```estrutura
Infrastructure → Application → Domain
```

❌ **NUNCA** inverta essa ordem!

### 3.3 Estrutura de Pacotes (Java)

```estrutura
com.snackbar.{modulo}
├── domain/
│   ├── entities/
│   ├── valueobjects/
│   └── ports/
├── application/
│   ├── usecases/
│   ├── dtos/
│   └── ports/
└── infrastructure/
    ├── persistence/
    ├── mappers/
    └── config/
```

### 3.4 Estrutura de Pastas (Angular)

```estrutura
src/app/
├── components/
│   └── {feature}/
│       ├── {feature}.component.ts
│       ├── {feature}.component.html
│       └── {feature}.component.css
├── services/
├── models/
├── utils/
└── composables/ (para lógica reutilizável)
```

---

## 4️⃣ DRY (Don't Repeat Yourself) - OBRIGATÓRIO

### 4.1 Mappers - SINGLETON OBRIGATÓRIO

- ✅ **SEMPRE** use `MapperUtils.getInstance()` do `kernel-compartilhado` para conversões simples
- ✅ **SEMPRE** verifique se já existe um mapper antes de criar novo
- ✅ Use `MapperRegistry.getInstance()` para mappers complexos
- ❌ **NUNCA** crie mappers duplicados
- ❌ **NUNCA** use `new MapperUtils()` - sempre use `.getInstance()`

**Exemplo CORRETO:**

```java
private final MapperUtils mapper = MapperUtils.getInstance();

public ProdutoDTO toDTO(Produto produto) {
    return mapper.map(produto, ProdutoDTO.class);
}
```

**Exemplo INCORRETO:**

```java
// ❌ ERRADO - não cria nova instância
private final MapperUtils mapper = new MapperUtils();

// ❌ ERRADO - não cria mapper duplicado quando já existe
public class MeuMapper implements Mapper<A, B> { ... }
```

### 4.2 Utilitários Comuns

- ✅ **SEMPRE** verifique `kernel-compartilhado` antes de criar utilitários
- ✅ **SEMPRE** verifique `frontend/src/app/utils/` antes de criar utilitários no frontend
- ✅ Use composables no Angular para lógica reutilizável
- ❌ **NUNCA** duplique código existente

### 4.3 Exemplos de Reutilização

- ✅ Validações comuns → `ValidationUtils`
- ✅ Formatação → `FormatoUtil` (frontend) ou utilitários compartilhados
- ✅ Paginação → `PaginacaoUtil`
- ✅ Upload de imagens → componentes/composables reutilizáveis

---

## 5️⃣ ANGULAR 17+ - PRÁTICAS MODERNAS OBRIGATÓRIAS

### 5.1 Injeção de Dependência

- ✅ **SEMPRE** use `inject()` ao invés de constructor injection
- ❌ **NUNCA** use constructor injection quando `inject()` é possível

**Exemplo CORRETO:**

```typescript
export class ProdutoComponent {
  private readonly produtoService = inject(ProdutoService);
  private readonly router = inject(Router);
}
```

**Exemplo INCORRETO:**

```typescript
// ❌ ERRADO - forma antiga
export class ProdutoComponent {
  constructor(
    private produtoService: ProdutoService,
    private router: Router
  ) {}
}
```

### 5.2 Signals - OBRIGATÓRIO

- ✅ **SEMPRE** use `signal()` para estado reativo
- ✅ **SEMPRE** use `computed()` para valores derivados
- ✅ **SEMPRE** use `effect()` para efeitos colaterais (no injection context)
- ❌ **NUNCA** use `BehaviorSubject` ou `Observable` para estado simples
- ❌ **NUNCA** use `ChangeDetectorRef.detectChanges()` manualmente se usar signals corretamente

**Exemplo CORRETO:**

```typescript
readonly produtos = signal<Produto[]>([]);
readonly produtosFiltrados = computed(() => 
  this.produtos().filter(p => p.disponivel)
);
```

### 5.3 Input/Output - Nova Sintaxe

- ✅ **SEMPRE** use `input()` e `output()` ao invés de `@Input()` e `@Output()`
- ✅ Use `input.required()` para inputs obrigatórios
- ✅ Use `input()` com valor padrão quando opcional

**Exemplo CORRETO:**

```typescript
readonly produto = input.required<Produto>();
readonly aberto = input<boolean>(false);
readonly onFechar = output<void>();
readonly onSalvar = output<Produto>();
```

**Exemplo INCORRETO:**

```typescript
// ❌ ERRADO - sintaxe antiga
@Input() produto!: Produto;
@Input() aberto = false;
@Output() onFechar = new EventEmitter<void>();
```

### 5.4 HTML - Nova Sintaxe de Controle

- ✅ **SEMPRE** use `@if`, `@for`, `@switch`, `@defer` ao invés de `*ngIf`, `*ngFor`, `[ngSwitch]`
- ❌ **NUNCA** use diretivas estruturais antigas

**Exemplo CORRETO:**

```html
@if (produto()) {
  <div>{{ produto().nome }}</div>
}

@for (item of itens(); track item.id) {
  <div>{{ item.nome }}</div>
}
```

**Exemplo INCORRETO:**

```html
<!-- ❌ ERRADO - sintaxe antiga -->
<div *ngIf="produto">{{ produto.nome }}</div>
<div *ngFor="let item of itens">{{ item.nome }}</div>
```

### 5.5 Standalone Components

- ✅ **SEMPRE** use `standalone: true`
- ✅ **SEMPRE** importe apenas o que precisa no array `imports`
- ❌ **NUNCA** crie módulos Angular (exceto para casos muito específicos)

### 5.6 Change Detection

- ✅ **SEMPRE** use `ChangeDetectionStrategy.OnPush` em componentes com signals
- ✅ **SEMPRE** use signals para estado reativo
- ❌ **NUNCA** manipule `ChangeDetectorRef` manualmente sem necessidade

### 5.7 Composables (Lógica Reutilizável)

- ✅ **SEMPRE** crie composables para lógica reutilizável
- ✅ Use padrão `use{Nome}` (ex: `useProdutos`, `useFormulario`)
- ✅ Composables devem usar `inject()` internamente

**Exemplo:**

```typescript
export function useProdutos() {
  const produtoService = inject(ProdutoService);
  const produtos = signal<Produto[]>([]);
  // ... lógica reutilizável
  return { produtos, carregar, salvar };
}
```

---

## 6️⃣ JAVA 17 - PRÁTICAS OBRIGATÓRIAS

### 6.1 Recursos do Java 17

- ✅ Use `records` para DTOs imutáveis
- ✅ Use `sealed classes` para hierarquias controladas (quando apropriado)
- ✅ Use `pattern matching` com `instanceof` (Java 16+)
- ✅ Use `text blocks` (`"""`) para strings multilinha
- ✅ Use `var` para tipos óbvios (quando melhora legibilidade)

### 6.2 Lombok - OBRIGATÓRIO PARA CONSTRUTORES E BOILERPLATE

- ✅ **SEMPRE** use `@RequiredArgsConstructor` ao invés de construtores manuais
- ✅ **SEMPRE** prefira Lombok quando possível para reduzir boilerplate e manter código enxuto
- ✅ **SEMPRE** use `@RequiredArgsConstructor` para campos `final` ou `@NonNull`
- ✅ Use `@AllArgsConstructor` apenas quando necessário criar todos os campos
- ✅ Use `@NoArgsConstructor` apenas quando realmente necessário (ex: JPA)
- ✅ Use `@Builder` para entidades complexas e objetos com muitos campos
- ✅ Use `@Data` ou `@Value` quando apropriado (cuidado com `@Data` - gera setters)
- ✅ Use `@Getter` e `@Setter` individualmente quando não precisar de tudo
- ❌ **NUNCA** crie construtores manuais quando Lombok pode fazer
- ❌ **NUNCA** abuse de Lombok - código deve permanecer legível e compreensível
- ❌ **NUNCA** use `@Data` em entidades JPA com relacionamentos bidirecionais (evita loops infinitos)

**Exemplo CORRETO:**

```java
@Service
@RequiredArgsConstructor  // ✅ OBRIGATÓRIO - cria construtor com campos final
public class CriarProdutoUseCase {
    private final ProdutoRepositoryPort repository;  // final = injetado via construtor
    private final ValidacaoProdutoService validacaoService;
    private final MapperUtils mapper = MapperUtils.getInstance();
    
    // Lombok gera automaticamente:
    // public CriarProdutoUseCase(ProdutoRepositoryPort repository, 
    //                           ValidacaoProdutoService validacaoService) {
    //     this.repository = repository;
    //     this.validacaoService = validacaoService;
    // }
}
```

**Exemplo INCORRETO:**

```java
// ❌ ERRADO - construtor manual desnecessário
@Service
public class CriarProdutoUseCase {
    private final ProdutoRepositoryPort repository;
    private final ValidacaoProdutoService validacaoService;
    
    // ❌ ERRADO - não precisa criar manualmente
    public CriarProdutoUseCase(ProdutoRepositoryPort repository,
                              ValidacaoProdutoService validacaoService) {
        this.repository = repository;
        this.validacaoService = validacaoService;
    }
}
```

### 6.3 Optional

- ✅ **SEMPRE** use `Optional` para valores que podem ser nulos
- ✅ **SEMPRE** trate `Optional` adequadamente (não apenas `.get()`)
- ❌ **NUNCA** retorne `null` quando pode retornar `Optional.empty()`

### 6.4 Streams API

- ✅ Use Streams para transformações de coleções
- ✅ Use `Collectors` para agregações
- ❌ **NUNCA** use loops tradicionais quando Streams são mais claros
- ✅ Prefira `stream().toList()` (Java 16+) ao invés de `collect(Collectors.toList())`

### 6.5 Try-With-Resources

- ✅ **SEMPRE** use try-with-resources para recursos que implementam `AutoCloseable`
- ❌ **NUNCA** deixe recursos abertos sem gerenciamento adequado

---

## 7️⃣ LIQUIBASE - MIGRAÇÕES DE BANCO

### 7.1 Estrutura de Arquivos

- ✅ **SEMPRE** crie changelogs no formato YAML ou XML
- ✅ **SEMPRE** use nomenclatura: `{YYYYMMDDHHmmss}_{descricao}.yml`
- ✅ **SEMPRE** inclua rollback quando possível
- ✅ Organize changelogs por módulo/funcionalidade

### 7.2 Boas Práticas

- ✅ **SEMPRE** teste rollback antes de aplicar em produção
- ✅ **SEMPRE** use constraints apropriadas (NOT NULL, UNIQUE, FOREIGN KEY)
- ✅ **SEMPRE** crie índices para campos de busca frequente
- ❌ **NUNCA** altere changelogs já aplicados (crie novos)
- ❌ **NUNCA** deixe migrações sem rollback

### 7.3 Estrutura Recomendada

```estrutura
src/main/resources/db/changelog/
├── db.changelog-master.yml
├── 001-initial-schema.yml
├── 002-produto-table.yml
└── ...
```

---

## 8️⃣ VALIDAÇÕES E SEGURANÇA

### 8.1 Backend (Java)

- ✅ **SEMPRE** valide entradas nos Use Cases
- ✅ Use `@Valid` e Bean Validation (JSR-303) em DTOs
- ✅ **SEMPRE** sanitize entradas de usuário
- ✅ Use prepared statements (JPA automaticamente, mas cuidado com queries nativas)

### 8.2 Frontend (Angular)

- ✅ **SEMPRE** valide formulários com `Validators` do Angular
- ✅ **SEMPRE** valide no frontend E no backend
- ✅ Use `FormControl`, `FormGroup`, `FormArray` com validações apropriadas
- ✅ Exiba mensagens de erro claras ao usuário

---

## 9️⃣ TESTES

### 9.1 Princípios

- ✅ **SEMPRE** escreva testes unitários para lógica de negócio
- ✅ **SEMPRE** teste casos de erro, não apenas sucesso
- ✅ Use nomes descritivos: `deveLancarExcecaoQuandoProdutoNaoExiste()`
- ✅ Mantenha testes simples e focados

### 9.2 Estrutura AAA

- ✅ Arrange (preparar)
- ✅ Act (executar)
- ✅ Assert (verificar)

---

## 🔟 NUNCA FAÇA (ANTI-PATTERNS)

### ❌ Proibições Absolutas

1. ❌ **NUNCA** crie mappers duplicados - sempre use singleton
2. ❌ **NUNCA** use sintaxe antiga do Angular quando existe sintaxe moderna
3. ❌ **NUNCA** misture responsabilidades (SRP violado)
4. ❌ **NUNCA** duplique código existente - sempre verifique reutilização
5. ❌ **NUNCA** crie utilitários sem verificar se já existem
6. ❌ **NUNCA** use `null` quando pode usar `Optional` (Java)
7. ❌ **NUNCA** deixe código comentado no repositório
8. ❌ **NUNCA** crie métodos com mais de 20 linhas sem justificativa
9. ❌ **NUNCA** viole a Clean Architecture (dependências invertidas)
10. ❌ **NUNCA** use variáveis com nomes genéricos ou abreviados
11. ❌ **NUNCA** crie classes com mais de 300 linhas sem justificativa
12. ❌ **NUNCA** crie construtores manuais quando `@RequiredArgsConstructor` pode fazer
13. ❌ **NUNCA** crie "God Classes" - sempre mantenha código enxuto

---

## 1️⃣1️⃣ CHECKLIST ANTES DE COMMITAR

Antes de finalizar qualquer implementação, verifique:

- [ ] ✅ Código segue princípios de Clean Code?
- [ ] ✅ Arquitetura respeita Clean Architecture?
- [ ] ✅ Não há duplicação de código (DRY)?
- [ ] ✅ Angular usa sintaxe moderna (`inject`, `signal`, `computed`, `input`, `output`, `@if`, `@for`)?
- [ ] ✅ Java usa recursos do Java 17?
- [ ] ✅ Mappers usam singleton (`MapperUtils.getInstance()` ou `MapperRegistry`)?
- [ ] ✅ Migrações Liquibase estão corretas e com rollback?
- [ ] ✅ Testes cobrem lógica de negócio?
- [ ] ✅ Validações no frontend e backend?
- [ ] ✅ Nomes descritivos e autoexplicativos?
- [ ] ✅ Métodos com no máximo 20 linhas?
- [ ] ✅ Classes com no máximo 300 linhas (código enxuto)?
- [ ] ✅ Usou `@RequiredArgsConstructor` ao invés de construtor manual?
- [ ] ✅ Não criou classes "God Classes" ou com múltiplas responsabilidades?

---

## 1️⃣2️⃣ EXEMPLOS DE REFERÊNCIA

### Angular - Componente Completo (Correto)

```typescript
import { Component, inject, input, output, signal, computed } from '@angular/core';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-produto-lista',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './produto-lista.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProdutoListaComponent {
  private readonly produtoService = inject(ProdutoService);
  
  readonly categoriaFiltro = input<string | null>(null);
  readonly onProdutoSelecionado = output<Produto>();
  
  readonly produtos = signal<Produto[]>([]);
  readonly produtosFiltrados = computed(() => {
    const filtro = this.categoriaFiltro();
    if (!filtro) return this.produtos();
    return this.produtos().filter(p => p.categoria === filtro);
  });
}
```

### Java - Use Case (Correto)

```java
@Service
@RequiredArgsConstructor  // ✅ OBRIGATÓRIO - Lombok gera construtor automaticamente
public class CriarProdutoUseCase {
    private final ProdutoRepositoryPort repository;  // final = injetado via construtor
    private final ValidacaoProdutoService validacaoService;
    private final MapperUtils mapper = MapperUtils.getInstance();
    
    public ProdutoDTO executar(CriarProdutoRequest request) {
        validarRequest(request);
        Produto produto = criarProduto(request);
        Produto salvo = repository.salvar(produto);
        return mapper.map(salvo, ProdutoDTO.class);
    }
    
    private void validarRequest(CriarProdutoRequest request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
    }
    
    private Produto criarProduto(CriarProdutoRequest request) {
        return Produto.criar(
            request.nome(),
            request.descricao(),
            Preco.of(request.preco()),
            request.categoria()
        );
    }
}
// ✅ Classe enxuta: ~25 linhas (bem abaixo do limite de 300)
```

### Java - Repository Adapter (Correto)

```java
@Component
@RequiredArgsConstructor  // ✅ OBRIGATÓRIO - não crie construtor manual
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {
    private final ProdutoJpaRepository jpaRepository;  // final = injetado
    private final ProdutoMapper mapper;
    private final MapperUtils mapperUtils = MapperUtils.getInstance();
    
    @Override
    public Produto salvar(Produto produto) {
        ProdutoEntity entity = mapper.paraEntity(produto);
        ProdutoEntity salvo = jpaRepository.save(entity);
        return mapper.paraDomain(salvo);
    }
    
    @Override
    public Optional<Produto> buscarPorId(String id) {
        return jpaRepository.findById(id)
            .map(mapper::paraDomain);
    }
    
    @Override
    public List<Produto> buscarTodos() {
        return jpaRepository.findAll().stream()
            .map(mapper::paraDomain)
            .toList();
    }
}
// ✅ Classe enxuta e focada em uma única responsabilidade
```

---

## 📚 RECURSOS DO REPOSITÓRIO

### Kernel Compartilhado

- `MapperUtils.getInstance()` - Mapeamento genérico (singleton)
- `MapperRegistry.getInstance()` - Registro de mappers complexos
- Utilitários comuns para reutilização

### Frontend Utils

- `frontend/src/app/utils/` - Utilitários do frontend
- Composables em `components/{feature}/composables/`

### Documentação

- `DOCUMENTACAO_ARQUITETURA_SISTEMA.md` - Arquitetura completa
- `EXEMPLO_USO_MAPPER_UTILS.md` - Exemplos de mappers

---

**🚨 LEMBRE-SE: Estas diretrizes são INEGOCIÁVEIS. Sempre siga todas as regras acima antes de implementar qualquer funcionalidade!**