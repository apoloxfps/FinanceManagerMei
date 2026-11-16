# 🏛️ Architecture & System Blueprint: FinanceManagerMei

> **Versão:** 1.1.0
> **Status:** Em desenvolvimento (Transição de CLI Monolítica para RESTful SaaS)
> **Target Runtime:** Java 21 (LTS)
> **Domínio de Negócio:** Gestão Financeira e Conformidade Fiscal para MEIs (Microempreendedores Individuais) e Microempresas (ME) no Brasil.

> **Legenda de status usada neste documento:** `[IMPLEMENTADO]` existe no código hoje (CLI em Java puro) · `[META]` arquitetura-alvo, ainda não implementada · `[EM ABERTO]` decisão ainda não tomada.

---

## 1. Visão Geral do Sistema

O **FinanceManagerMei** é um sistema SaaS (*Software as a Service*) backend projetado para resolver a dor de gestão financeira e compliance fiscal de Microempreendedores Individuais (MEI) e Microempresas (ME) no Brasil.

### Principais Funcionalidades Target

- **Cálculo da Regra Fiscal do MEI:** apuração do teto anual de faturamento (R$ 81.000,00) e cálculo da margem de segurança operacional.
- **Monitoramento da Zona de Perigo:** emissão de alertas automáticos quando o faturamento acumulado atinge 80% do limite legal (R$ 64.800,00).
- **Gestão de Imposto DAS:** identificação da alíquota mensal fixa de acordo com a categoria de atuação (Comércio, Indústria ou Prestação de Serviços).
- **Imposto simplificado da Microempresa:** 6% sobre a média mensal dos meses informados. É uma simplificação do cálculo real, não a regra tributária completa.
- **Auditoria de Regularidade:** verificação combinada do status do imposto (Adimplente/Inadimplente) e estouro de limite fiscal para alertas de migração para ME (Microempresa).
- **Lançamento Temporal de Faturamento:** registro e consolidação de receitas mensais via vetores/coleções para cálculo do Faturamento Anual Real.
- **Vencimento do imposto:** dia 20 do mês seguinte, ajustado para o próximo dia útil.

**Escopo de pessoa física (CLT):** `[EM ABERTO]`, ver seção 8.

---

## 2. Decisões Técnicas e Justificativas (ADR)

| Decisão Técnica | Escolha | Justificativa de Engenharia |
| :--- | :--- | :--- |
| **Linguagem / Runtime** | Java 21 (LTS) | Uso de recursos modernos (`Text Blocks`, `Switch Expressions`), estabilidade enterprise e alinhamento com os padrões atuais do mercado. |
| **Arquitetura Alvo** | RESTful API com Spring Boot | Desconexão da interface de usuário do backend, permitindo integração com apps web/mobile e escalabilidade horizontal. |
| **Armazenamento** | PostgreSQL | Banco de dados relacional robusto com forte suporte a transações ACID, fundamental para integridade de dados financeiros. |
| **Containers** | Docker & Docker Compose | Padronização do ambiente, isolando a aplicação e o banco de dados e eliminando o problema de "na minha máquina funciona". |
| **Deploy / Cloud** | AWS (EC2 / RDS) | Garantia de alta disponibilidade, IP público e vivência prática em ambiente real de produção Cloud. |

### 2.1 Decisões de domínio já tomadas `[IMPLEMENTADO]`

- **Hierarquia `Empresa` abstrata com polimorfismo:** `EmpresaMei` e `MicroEmpresa` herdam de `Empresa`; `ServicoContabilidade` opera sobre a abstração.
- **Invariantes no construtor:** quem garante a regra é a classe dona do dado. O domínio pode chamar util; util não conhece domínio.
- **`Cnpj` como objeto imutável (value object):** valida formato alfanumérico e dígitos verificadores no construtor; `Empresa` apenas checa nulo.
- **Entrada ambígua é rejeitada, nunca adivinhada** (ex.: "1.500" não vira 1,5).
- **`possuiFuncionario` como `boolean`:** MEI tem 0 ou 1 funcionário (LC 123/2006). Acompanhar o PLP 186/2026.
- **Faturamento zero é válido;** `null` representa mês pendente.
- **`DominioInvalidoException` única e unchecked,** lançada nos construtores e validadores.

---

## 3. Estrutura de Camadas (Layered Architecture) `[META]`

O sistema adota o padrão de arquitetura em camadas desacopladas (*Separation of Concerns*). Cada camada tem responsabilidade estrita e se comunica apenas com a camada imediatamente inferior.

```text
┌───────────────────────────────────────────────────────────┐
│                    Controller Layer                       │
│        (HTTP Endpoints / DTO Validation / Mapping)        │
└─────────────────────────────┬─────────────────────────────┘
                              │
                              ▼
┌───────────────────────────────────────────────────────────┐
│                     Service Layer                         │
│   (Business Rules / DAS Rules / Tax Limits / Thresholds)  │
└─────────────────────────────┬─────────────────────────────┘
                              │
                              ▼
┌───────────────────────────────────────────────────────────┐
│                    Repository Layer                       │
│             (Spring Data JPA / SQL Queries)               │
└─────────────────────────────┬─────────────────────────────┘
                              │
                              ▼
┌───────────────────────────────────────────────────────────┐
│                     Database Layer                        │
│                   (PostgreSQL Storage)                    │
└───────────────────────────────────────────────────────────┘
```

### 3.1 Responsabilidades de Cada Camada

1. **Controllers** — Expõem endpoints REST, recebem requisições HTTP, delegam validações sintáticas e retornam o `ResponseEntity` com status HTTP adequado.
2. **DTOs (Data Transfer Objects)** — Objetos de transporte. Evitam a exposição direta das Entidades do banco e filtram dados de Request e Response.
3. **Services** — O coração da aplicação. Contêm as regras de negócio puras (cálculos de impostos, validação de limites, auditoria de status).
4. **Repositories** — Interfaces do Spring Data JPA que abstraem a comunicação SQL com o banco de dados.
5. **Entities (Models)** — Classes mapeadas (ORM) que representam as tabelas físicas do banco de dados.

---

## 4. Estrutura de Pastas e Pacotes

### 4.1 Estrutura-alvo `[META]`

```text
com.devandrade.financemanagemei/
├── config/                  # Configurações globais (Security, CORS, Swagger)
├── controller/              # REST Controllers (Endpoints)
├── dto/                     # Classes de Request e Response
│   ├── request/             # Payloads de entrada
│   └── response/            # Payloads de saída
├── exception/               # Handlers globais de erro (@ControllerAdvice)
├── model/                   # Entidades JPA (Domain)
│   └── enums/               # Enums (TipoAtuacao, StatusImposto)
├── repository/              # Interfaces de persistência
├── service/                 # Regras de Negócio
└── FinanceManagerMeiApplication.java  # Classe Main do Spring Boot
```

### 4.2 Estrutura atual `[IMPLEMENTADO]`

Pacotes `main` (CLI procedural), `dominio`, `util`, `exception` e `servico`, em `com.devandrade.financemanagemei`. Sem Maven/Gradle por enquanto (a ferramenta de build entra junto com o Spring Boot). A renomeação e a divisão em camadas serão decididas quando a etapa Spring Boot chegar.

---

## 5. Modelagem de Dados

### 5.1 Modelo atual `[IMPLEMENTADO]`

**`Empresa` (abstract)**

| Campo | Tipo | Observações |
| :--- | :--- | :--- |
| `razaoSocial` | `String` | Mínimo de 3 caracteres após formatação |
| `endereco` | `Endereco` | Campos em maiúsculas |
| `dataAbertura` | `LocalDate` | Não nula e não futura (validada no construtor) |
| `cnpj` | `Cnpj` | Objeto imutável |
| `faturamentos` | `Double[]` | Exatamente 12 posições; `null` = mês pendente; rejeita negativo, NaN e Infinity; cópia defensiva |
| `statusImposto` | `char` | — |

**`EmpresaMei` (final, extends `Empresa`):** `nomeEmpresario` (String), `tipoAtuacao` (`TipoAtuacao`, enum com valor do DAS), `possuiFuncionario` (boolean). Limite anual de R$ 81.000,00.

**`MicroEmpresa` (final, extends `Empresa`):** imposto simplificado de 6% sobre a média dos meses informados (constante `IMPOSTO_POR_CENTO`).

**`Cnpj` (final, imutável):** formato alfanumérico, converte para maiúscula, rejeita 14 caracteres iguais e confere os dígitos verificadores (módulo 11, valor do caractere = ASCII − 48).

### 5.2 Modelo-alvo (persistência) `[META]`

#### `EmpresaMei` (Aggregate Root)

| Campo | Tipo | Observações |
| :--- | :--- | :--- |
| `id` | `Long` | PK |
| `nomeEmpresario` | `String` | Not Null |
| `razaoSocial` | `String` | Not Null, Unique |
| `cnpj` | `String` | Not Null, Unique |
| `tipoAtuacao` | `Enum` | `COMERCIO`, `INDUSTRIA`, `SERVICOS` |
| `statusImposto` | `Enum` | `ADIMPLENTE`, `INADIMPLENTE` |
| `faturamentos` | `List<FaturamentoMensal>` | Relacionamento One-To-Many |

A estratégia de mapeamento da hierarquia (`Empresa` com `EmpresaMei` e `MicroEmpresa`) para o banco é `[EM ABERTO]` e será decidida na etapa de Banco de Dados.

#### `FaturamentoMensal`

| Campo | Tipo | Observações |
| :--- | :--- | :--- |
| `id` | `Long` | PK |
| `mes` | `Integer` | 1 a 12 |
| `ano` | `Integer` | — |
| `valor` | `BigDecimal` | — |
| `empresa` | `EmpresaMei` | Relacionamento Many-To-One |

### 5.3 Divergências conhecidas entre código e meta

| Ponto | Código atual | Meta | Resolução |
| :--- | :--- | :--- | :--- |
| Faturamentos | `Double[]` de 12 posições, sem ano | `List<FaturamentoMensal>` com mês, ano e `BigDecimal` | **Código converge.** Primeiro trocar o tipo, depois a estrutura (commits separados). |
| Tipo monetário | `double` | `BigDecimal` | **Código converge.** Refactor decidido antes da etapa de Banco de Dados. |
| `statusImposto` | `char` | Enum `StatusImposto` | **Código converge** (virar enum). |
| Hierarquia | `Empresa` abstrata + `EmpresaMei` + `MicroEmpresa` | Só `EmpresaMei` no modelo-alvo | **Documento atualizado** (esta versão). |
| CNPJ | Objeto `Cnpj` | Coluna `String` | `[EM ABERTO]` Mapeamento do value object decidido na etapa de Banco de Dados. |

---

## 6. Padrões e Convenções de Código

### 6.1 Nomenclatura

- **Classes:** `PascalCase` (ex: `FaturamentoService`)
- **Métodos/Atributos:** `camelCase` (ex: `calcularLimiteAnual()`)
- **Constantes:** `SNAKE_CASE` em maiúsculo com modificador `final` (ex: `LIMITE_ANUAL_MEI`)

### 6.2 Clean Code

- Uso de **Cláusulas de Guarda** (*Guard Clauses*) no topo de métodos e loops para inverter condicionais `if` e reduzir a indentação e complexidade do fluxo principal.
- **DTO Pattern:** utilização de `records` do Java 21 para imutabilidade e código mais limpo na camada de transporte.
- **Encapsulamento:** nunca devolver referência mutável (cópia defensiva); os métodos de regra do `EmpresaMei` são `private` por enquanto. `[IMPLEMENTADO]`

### 6.3 Tratamento de Exceções

- Uso de exceções personalizadas de negócio (ex: `LimiteEstouradoException`).
- Captura centralizada via `@RestControllerAdvice` e padronização das mensagens de erro (formato *Problem Details* — RFC 7807).
- É **estritamente proibido** o uso de `System.out.println` para logs ou tratamento genérico vazio (`catch` vazio).

**Estado atual** `[IMPLEMENTADO]`: `DominioInvalidoException` única e unchecked; catch sempre específico, nunca `RuntimeException` genérico. A relação entre a exceção única atual e as exceções específicas acima será revisada na etapa Spring Boot.

---

## 7. Fluxo de uma Requisição Típica `[META]`

Caminho dos dados ao registrar um faturamento mensal:

1. **Cliente** — Faz requisição HTTP `POST` para `/api/v1/empresas/{id}/faturamentos` com JSON no corpo.
2. **Controller** — Recebe a requisição via `RequestDTO` e realiza validações de borda (ex: valor não pode ser negativo).
3. **Service**:
   - Recupera a `EmpresaMei` via Repository.
   - Adiciona o novo faturamento.
   - Executa a regra de negócio: recalcula o faturamento anual e verifica se a empresa entrou na zona de perigo (>80%) ou se estourou o teto.
4. **Repository** — Atualiza as tabelas no PostgreSQL (salva a transação).
5. **Controller** — Converte a Entidade atualizada em um `ResponseDTO` e retorna para o Cliente com HTTP Status `201 Created`.

---

## 8. Decisões em Aberto (Roadmap)

**Autenticação / Autorização**
Definir implementação de Spring Security + JWT para proteção dos endpoints, garantindo que o MEI acesse apenas os próprios dados.

**Precisão Numérica**
Migrar os tipos `double` atuais para `BigDecimal`, para anular inconsistências de arredondamento em cálculos financeiros críticos. **Decidido: será feito antes da etapa de Banco de Dados**, com estudo pela documentação oficial. PENDENTE.

**Gestão de Parâmetros Fiscais**
Definir se o teto de R$ 81.000,00 e os valores do DAS ficarão armazenados no banco de dados, via variáveis de ambiente ou injetados por arquivo `.properties`, permitindo mudança ágil conforme leis anuais sem necessidade de recompilar o código.

**Pessoa física (CLT)** `[EM ABERTO]`
Receitas e despesas pessoais. É um item futuro, sem implementação nem interface por enquanto. Fica fora da hierarquia de `Empresa`. A decisão sobre o escopo será tomada depois.

**Pendências do domínio atual**
- Feriados no vencimento do imposto (hoje só o fim de semana é ajustado): `Set<MonthDay>` e renomear `validarDiaUtil` para `proximoDiaUtil`.
- Deixar explícito no código e nos relatórios que os 6% da ME são simplificação.
- `toString` do relatório depende de `LocalDate.now()`; rever ao introduzir testes.
- `equals` e `hashCode` do `Cnpj`.

### 8.1 Qualidade, entrega e operação `[META]`

Itens que o projeto precisa conter para ser considerado completo. Cada um entra na etapa indicada do plano de estudos, e nenhum deve ser antecipado antes do pré-requisito. Os conteúdos de teste ainda não foram estudados.

| Item | Etapa em que entra | Pré-requisito | Observações |
| :--- | :--- | :--- | :--- |
| **README.md** | Git/GitHub (já existe) | — | Manter atualizado a cada etapa concluída: descrição, como compilar e rodar, link para este documento. |
| **Testes automatizados (JUnit 5)** | Java (aulas 280-281) | Maven (aula 255) | Os casos rodados à mão e registrados no `documentação.md` viram os primeiros testes. |
| **Testes de Service (Mockito)** | Spring Boot | Camada Service pronta | Regras de negócio isoladas do banco. |
| **Testes de integração** | Banco de Dados / Spring Boot | Banco e Controllers prontos | Controllers e Repositories contra PostgreSQL real em container (Testcontainers). |
| **Migração de schema (Flyway)** | Banco de Dados | Decisão do mapeamento da hierarquia `Empresa` | Schema versionado em vez de criado automaticamente pelo ORM. |
| **Validação de entrada (Bean Validation)** | Spring Boot | DTOs prontos | Validação de borda nos DTOs, complementar às invariantes do domínio. |
| **Logging (SLF4J)** | Spring Boot | — | Substitui o `System.out.println` fora do CLI. |
| **Documentação da API (OpenAPI/Swagger)** | API REST / Spring Boot | Controllers prontos | springdoc-openapi. |
| **Autenticação (Spring Security + JWT)** | Spring Boot | API funcionando sem autenticação | Ver "Autenticação / Autorização" acima. |
| **Containerização (Dockerfile + Compose)** | Docker | Aplicação e banco funcionando | Aplicação e PostgreSQL isolados. |
| **CI/CD (GitHub Actions)** | Assim que existirem Maven e ao menos um teste | Testes JUnit | Começa só compilando e rodando testes a cada push; depois evolui para build de imagem e deploy. |
| **Deploy (AWS EC2/RDS)** | AWS | Docker e CI/CD | Segredos e credenciais por variáveis de ambiente, nunca no repositório. |