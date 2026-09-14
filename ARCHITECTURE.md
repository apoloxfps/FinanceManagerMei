# 🏛️ Architecture & System Blueprint: FinanceManagerMei

> **Versão:** 1.0.0
> **Status:** Em desenvolvimento (Transição de CLI Monolítica para RESTful SaaS)
> **Target Runtime:** Java 21 (LTS)
> **Domínio de Negócio:** Gestão Financeira e Conformidade Fiscal para MEIs (Microempreendedores Individuais) no Brasil.

---

## 1. Visão Geral do Sistema

O **FinanceManagerMei** é um sistema SaaS (*Software as a Service*) backend projetado para resolver a dor de gestão financeira e compliance fiscal de Microempreendedores Individuais (MEI) no Brasil.

### Principais Funcionalidades Target

- **Cálculo da Regra Fiscal do MEI:** apuração do teto anual de faturamento (R$ 81.000,00) e cálculo da margem de segurança operacional.
- **Monitoramento da Zona de Perigo:** emissão de alertas automáticos quando o faturamento acumulado atinge 80% do limite legal (R$ 64.800,00).
- **Gestão de Imposto DAS:** identificação da alíquota mensal fixa de acordo com a categoria de atuação (Comércio, Indústria ou Prestação de Serviços).
- **Auditoria de Regularidade:** verificação combinada do status do imposto (Adimplente/Inadimplente) e estouro de limite fiscal para alertas de migração para ME (Microempresa).
- **Lançamento Temporal de Faturamento:** registro e consolidação de receitas mensais via vetores/coleções para cálculo do Faturamento Anual Real.

---

## 2. Decisões Técnicas e Justificativas (ADR)

| Decisão Técnica | Escolha | Justificativa de Engenharia |
| :--- | :--- | :--- |
| **Linguagem / Runtime** | Java 21 (LTS) | Uso de recursos modernos (`Text Blocks`, `Switch Expressions`), estabilidade enterprise e alinhamento com os padrões atuais do mercado. |
| **Arquitetura Alvo** | RESTful API com Spring Boot | Desconexão da interface de usuário do backend, permitindo integração com apps web/mobile e escalabilidade horizontal. |
| **Armazenamento** | PostgreSQL | Banco de dados relacional robusto com forte suporte a transações ACID, fundamental para integridade de dados financeiros. |
| **Containers** | Docker & Docker Compose | Padronização do ambiente, isolando a aplicação e o banco de dados e eliminando o problema de "na minha máquina funciona". |
| **Deploy / Cloud** | AWS (EC2 / RDS) | Garantia de alta disponibilidade, IP público e vivência prática em ambiente real de produção Cloud. |

---

## 3. Estrutura de Camadas (Layered Architecture)

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

---

## 5. Modelagem de Dados

### 5.1 `EmpresaMei` (Aggregate Root)

| Campo | Tipo | Observações |
| :--- | :--- | :--- |
| `id` | `Long` | PK |
| `nomeEmpresario` | `String` | Not Null |
| `razaoSocial` | `String` | Not Null, Unique |
| `cnpj` | `String` | Not Null, Unique |
| `tipoAtuacao` | `Enum` | `COMERCIO`, `INDUSTRIA`, `SERVICOS` |
| `statusImposto` | `Enum` | `ADIMPLENTE`, `INADIMPLENTE` |
| `faturamentos` | `List<FaturamentoMensal>` | Relacionamento One-To-Many |

### 5.2 `FaturamentoMensal`

| Campo | Tipo | Observações |
| :--- | :--- | :--- |
| `id` | `Long` | PK |
| `mes` | `Integer` | 1 a 12 |
| `ano` | `Integer` | — |
| `valor` | `BigDecimal` | — |
| `empresa` | `EmpresaMei` | Relacionamento Many-To-One |

---

## 6. Padrões e Convenções de Código

### 6.1 Nomenclatura

- **Classes:** `PascalCase` (ex: `FaturamentoService`)
- **Métodos/Atributos:** `camelCase` (ex: `calcularLimiteAnual()`)
- **Constantes:** `SNAKE_CASE` em maiúsculo com modificador `final` (ex: `LIMITE_ANUAL_MEI`)

### 6.2 Clean Code

- Uso de **Cláusulas de Guarda** (*Guard Clauses*) no topo de métodos e loops para inverter condicionais `if` e reduzir a indentação e complexidade do fluxo principal.
- **DTO Pattern:** utilização de `records` do Java 21 para imutabilidade e código mais limpo na camada de transporte.

### 6.3 Tratamento de Exceções

- Uso de exceções personalizadas de negócio (ex: `LimiteEstouradoException`).
- Captura centralizada via `@RestControllerAdvice` e padronização das mensagens de erro (formato *Problem Details* — RFC 7807).
- É **estritamente proibido** o uso de `System.out.println` para logs ou tratamento genérico vazio (`catch` vazio).

---

## 7. Fluxo de uma Requisição Típica

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
Validar a transição dos tipos `double` atuais para `BigDecimal` na fase Spring Boot, para anular inconsistências de arredondamento em cálculos financeiros críticos.

**Gestão de Parâmetros Fiscais**
Definir se o teto de R$ 81.000,00 e os valores do DAS ficarão armazenados no banco de dados, via variáveis de ambiente ou injetados por arquivo `.properties`, permitindo mudança ágil conforme leis anuais sem necessidade de recompilar o código.
