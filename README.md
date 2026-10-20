# FinanceManagerMei

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![Licença](https://img.shields.io/badge/licença-MIT-blue)

Sistema backend para **gestão financeira e conformidade fiscal de Microempreendedores Individuais (MEI) e Microempresas (ME)** no Brasil. Automatiza o cálculo do DAS, o monitoramento do teto anual de faturamento e a consolidação de receitas mensais — regras que hoje a maioria dos MEIs acompanha manualmente, com alto risco de erro.

> Projeto em desenvolvimento ativo, evoluindo em etapas alinhadas ao meu plano de estudos de transição para backend Java. A arquitetura-alvo está documentada em [`ARCHITECTURE.md`](./ARCHITECTURE.md); este README descreve o que **já existe** hoje.

---

## Sobre o projeto

Este projeto nasceu de uma necessidade real: acompanhar entradas, despesas e lucro líquido do meu próprio negócio de forma confiável, sem depender de cálculo manual sujeito a esquecimento. A primeira versão foi construída em Python ([FinanceManager 2.0](https://github.com/apoloxfps/FinanceManager-2.0)); esta é a reescrita em Java, com arquitetura pensada para evoluir até uma API REST completa.

## Estágio atual da implementação

| Item | Status |
|---|---|
| Regras de negócio do domínio (MEI e ME) | ✅ Implementadas |
| Modelo orientado a objetos (classe abstrata, herança, associação, enum) | ✅ Implementado |
| Validação de domínio com exceção customizada | ✅ Implementada |
| Interface | Console (`Scanner`) |
| Persistência (PostgreSQL) | 📋 Planejada |
| Testes automatizados | 📋 Planejados |
| API REST (Spring Boot) | 📋 Planejada — ver `ARCHITECTURE.md` |
| Containerização (Docker) e deploy (AWS) | 📋 Planejados |

### O que já funciona

- Cadastro de **Empresa MEI** ou **Microempresa (ME)**, com endereço validado.
- **MEI:** valor do DAS conforme o tipo de atuação (Comércio, Indústria ou Prestação de Serviços), verificação do teto anual de R$ 81.000,00, alerta ao atingir 80% do teto, margem de faturamento restante e indicação de regularidade.
- **ME:** imposto mensal estimado a partir do faturamento informado (cálculo simplificado — ver limitações).
- Lançamento de faturamento mensal com suporte a **mês pendente** (campo em branco), consolidando faturamento anual e quantidade de meses informados.
- Relatório consolidado por empresa e relatório tributário gerado por classe de serviço.
- Validação de entrada com exceção de domínio (`DominioInvalidoException`) e nova tentativa no console quando o dado é inválido.

### Limitações conhecidas

- O **status do imposto** (adimplente/inadimplente) ainda é fixo como adimplente — não é informado pelo usuário.
- O imposto da **ME** usa um percentual fixo (6%) como simplificação; não reproduz as faixas do Simples Nacional.
- Valores monetários em `double`; a migração para `BigDecimal` está no roadmap.
- Sem persistência: os dados existem apenas durante a execução.

## Conceitos aplicados

- **Herança, classe abstrata e polimorfismo:** `Empresa` (abstrata, com `calcularImpostoMensal()` abstrato) e as subclasses finais `EmpresaMei` e `MicroEmpresa`. O código de cadastro e de relatório trabalha com a referência `Empresa`, e o cálculo e o relatório específicos de cada tipo são resolvidos em tempo de execução (polimorfismo).
- **Encapsulamento:** atributos `final`, validação no construtor, métodos de regra de negócio não expostos e cópia defensiva do array de faturamentos.
- **Associação:** `Empresa` conhece `Endereco` (unidirecional).
- **Enum com atributos:** `TipoAtuacao` (código, valor do DAS e nome) com busca por código.
- **Exceção customizada** (unchecked) para regras de domínio.
- **Wrappers:** `Double[]` com `null` representando mês ainda não informado.
- **Classe utilitária** (`StringUtil`) e membros `static` (contador de empresas cadastradas na sessão).

## Tecnologias utilizadas hoje

- **Java 21** (LTS) — com uso de enums e text blocks.

## Tecnologias planejadas

Documentadas com justificativa técnica em [`ARCHITECTURE.md`](./ARCHITECTURE.md): Spring Boot 3 (API REST), PostgreSQL, Spring Data JPA, Docker/Docker Compose, AWS (EC2/RDS), Spring Security + JWT.

## Estrutura do projeto

```text
src/com/devandrade/financemanagemei/
├── main/        # Main (menu e entrada de dados no console)
├── dominio/     # Empresa, EmpresaMei, MicroEmpresa, Endereco, TipoAtuacao
├── exception/   # DominioInvalidoException
├── servico/     # ServicoContabilidade (relatório tributário)
└── util/        # StringUtil
```

## Como executar (versão atual)

Pré-requisito: JDK 21 instalado.

```bash
git clone https://github.com/apoloxfps/FinanceManagerMei.git
cd FinanceManagerMei
javac -d out -sourcepath src src/com/devandrade/financemanagemei/main/Main.java
java -cp out com.devandrade.financemanagemei.main.Main
```

> Comando para a estrutura atual do projeto (sem Maven/Gradle). Caso adicione um gerenciador de build, atualize esta seção.

## Roadmap

- [x] Modelar o domínio em classes com POO (hierarquia `Empresa` / `EmpresaMei` / `MicroEmpresa`)
- [x] Validação de entrada com exceção customizada
- [ ] Tornar o status do imposto informável (hoje fixo)
- [ ] Migrar `double` para `BigDecimal` nos cálculos financeiros
- [ ] Persistência em PostgreSQL
- [ ] Testes automatizados (unitários e de integração)
- [ ] Camada Controller/Service/Repository com Spring Boot (API REST)
- [ ] Containerização com Docker
- [ ] Deploy na AWS

## Arquitetura

O planejamento técnico completo — decisões de arquitetura (ADR), modelagem de dados, estrutura de pacotes e fluxo de requisição — está documentado em [`ARCHITECTURE.md`](./ARCHITECTURE.md), com o diagrama em [`arquiteturaFinanceManagerMei.puml`](./arquiteturaFinanceManagerMei.puml) (PlantUML).

## Autor

**Nicolas Andrade**
[LinkedIn](https://www.linkedin.com/in/dev-nicolas-andrade) · [GitHub](https://github.com/apoloxfps) · nicleicaio2@gmail.com
