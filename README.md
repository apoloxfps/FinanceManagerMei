# FinanceManagerMei

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![Licença](https://img.shields.io/badge/licença-MIT-blue)

Sistema backend para **gestão financeira e conformidade fiscal de Microempreendedores Individuais (MEI)** no Brasil. Automatiza o cálculo de alíquota DAS, o monitoramento do teto anual de faturamento e a consolidação de receitas mensais — regras que hoje são feitas manualmente pela maioria dos MEIs, com alto risco de erro.

> Projeto em desenvolvimento ativo, evoluindo em etapas alinhadas ao meu plano de estudos de transição para backend Java. A arquitetura final está documentada em [`ARCHITECTURE.md`](./ARCHITECTURE.md); este README descreve o que **já existe** hoje.

---

## Sobre o projeto

Antes de existir em Java, este projeto nasceu de uma necessidade real: acompanhar entradas, despesas e lucro líquido do meu próprio negócio (MEI) de forma confiável, mês a mês, sem depender de planilha ou cálculo manual sujeito a esquecimento. A primeira versão foi construída em Python ([FinanceManager 2.0](https://github.com/apoloxfps/FinanceManager-2.0)); esta é a reescrita em Java, com arquitetura pensada para evoluir até uma API REST completa.

## Estágio atual da implementação

| Item | Status |
|---|---|
| Regras de negócio do domínio MEI | ✅ Implementadas |
| Interface | Console (`Scanner`) |
| Modelagem em classes (POO) | ⏳ Próxima etapa do plano de estudos |
| API REST (Spring Boot) | 📋 Planejada — ver `ARCHITECTURE.md` |
| Persistência (PostgreSQL) | 📋 Planejada |
| Containerização (Docker) | 📋 Planejada |
| Deploy (AWS) | 📋 Planejada |

### O que já funciona

- Cálculo da alíquota mensal do **DAS** de acordo com o tipo de atuação (Comércio, Indústria ou Prestação de Serviços).
- Consolidação do faturamento mensal e cálculo do **faturamento anual**.
- Verificação da **zona de perigo** (80% do teto legal de R$ 81.000,00) com alerta automático.
- Verificação de estouro do teto anual, com aviso para transição a Microempresa (ME).
- Cruzamento entre status de imposto e faturamento para indicar **regularidade fiscal** da empresa.
- Geração de relatório consolidado com todos os dados apurados.

## Tecnologias utilizadas hoje

- **Java 21** (LTS) — uso de `switch expressions` e `text blocks`.

## Tecnologias planejadas

Documentadas com justificativa técnica em [`ARCHITECTURE.md`](./ARCHITECTURE.md): Spring Boot 3 (API REST), PostgreSQL, Spring Data JPA, Docker/Docker Compose, AWS (EC2/RDS), Spring Security + JWT.

## Como executar (versão atual)

Pré-requisito: JDK 21 instalado.

```bash
git clone https://github.com/apoloxfps/FinanceManagerMei.git
cd FinanceManagerMei
javac -d out src/com/devandrade/financemanagemei/main/Main.java
java -cp out com.devandrade.financemanagemei.main.Main
```

> Comando testado para a estrutura atual do projeto (sem Maven/Gradle). Caso adicione um gerenciador de build futuramente, atualize esta seção.

## Roadmap

- [ ] Modelar o domínio em classes (`EmpresaMei`, `FaturamentoMensal`) com POO
- [ ] Adicionar tratamento de erros e validação de entrada
- [ ] Migrar `double` para `BigDecimal` nos cálculos financeiros
- [ ] Implementar camada Controller/Service/Repository com Spring Boot
- [ ] Persistência em PostgreSQL via Spring Data JPA
- [ ] Testes automatizados (unitários e de integração)
- [ ] Containerização com Docker
- [ ] Deploy na AWS

## Arquitetura

O planejamento técnico completo — decisões de arquitetura (ADR), modelagem de dados, estrutura de pacotes e fluxo de requisição — está documentado em [`ARCHITECTURE.md`](./ARCHITECTURE.md), com o diagrama de arquitetura em [`arquiteturaFinanceManagerMei.puml`](./arquiteturaFinanceManagerMei.puml) (PlantUML).

## Autor

**Nicolas Andrade**
[LinkedIn](https://www.linkedin.com/in/nicolas-andrade-a559b0219) · [GitHub](https://github.com/apoloxfps) · nicleicaio2@gmail.com
