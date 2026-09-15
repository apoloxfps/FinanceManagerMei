package com.devandrade.financemanagemei.main;
import java.util.Scanner;

public class Main {
    private static final double LIMITE_ANUAL_MEI = 81000.00;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Finance Manager Mei ===\n[ 1 ] Imprimir Relatório.\n[ 2 ] Sair do Sistema\nDigite 1 ou 2");
            int opcao = Integer.parseInt(scanner.nextLine());

            if (opcao == 2) {
                System.out.println("Sistema Finalizado.");
                break;
            }
            if (opcao != 1) {
                System.out.println("Opção Invalida, Tente Novamente.");
                continue;
            }
            System.out.println("Nome Empresario: ");
            String nomeEmpresario = scanner.nextLine();

            System.out.println("Razão Social: (ex: nome completo + cnpj)");
            String razaoSocial = scanner.nextLine();

            System.out.println("Tipo de Atuação\n[ 1 ] Comercio \n[ 2 ] Industria \n[ 3 ] Prestação de Serviços\nDigite 1, 2 ou 3: ");
            int tipoAtuacao = Integer.parseInt(scanner.nextLine());
            String nomeAtuacao = switch (tipoAtuacao) {
                case 1 -> "Comércio.";
                case 2 -> "Industria.";
                case 3 -> "Prestação de Serviços.";
                default -> "Invalido.";
            };
            double valorDas = switch (tipoAtuacao) {
                case 1 -> 71.60;
                case 2 -> 72.60;
                case 3 -> 75.60;
                default -> 0.0;
            };

            double faturamentoAnual = 0.0;
            double[] faturamentos = new double[12];
            StringBuilder faturamentosSb = new StringBuilder();
            for (int i = 0; i < faturamentos.length; i++) {
                System.out.printf("Digite Seu Faturamento do Mês %d: ",(i+1));
                double faturamentoMensal = Double.parseDouble(scanner.nextLine());
                faturamentos[i] = faturamentoMensal;
                faturamentoAnual += faturamentos[i];
                faturamentosSb.append("\nMês: ").append(i+1).append(" | ").append(String.format("Faturamento: R$ %.2f",faturamentos[i]));
            }
            String faturamentosMensaisTexto = faturamentosSb.toString();

            int quantFuncionarios = 1;
            char statusImposto = 'A';
            String statusImpostoTexto = statusImposto == 'A' ? "Adimplente." : "Inadimplente.";
            double margemFaturamentoRestante = LIMITE_ANUAL_MEI - faturamentoAnual;
            boolean isUltrapassouLimite = faturamentoAnual > LIMITE_ANUAL_MEI;
            double valorZonaDePerigo = LIMITE_ANUAL_MEI * 0.8;
            String isUltrapassouLimiteTexto;
            String alertaFaturamento;
            if (isUltrapassouLimite) {
                isUltrapassouLimiteTexto = "Sim";
                alertaFaturamento = "ALERTA CRÍTICO: Limite estourado! Procure um contador " +
                        "para transição para Microempresa (ME).";
            } else if (faturamentoAnual >= valorZonaDePerigo) {
                isUltrapassouLimiteTexto = "Não";
                alertaFaturamento = "ATENÇÃO: Você atingiu 80% do teto anual. Monitore suas notas fiscais.";
            } else {
                isUltrapassouLimiteTexto = "Não";
                alertaFaturamento = "Status seguro: Faturamento dentro da margem operacional.";
            }
            boolean isEmpresaRegular = statusImposto == 'A' && !isUltrapassouLimite;
            String empresaRegularTexto;
            if (isEmpresaRegular) {
                empresaRegularTexto = "Empresa regulamentada.";
            } else {
                empresaRegularTexto = "Empresa desregulamentada Verifique seus status de imposto e seu faturamento anual.";
            }
            String relatorio = """
                    === RELATÓRIO ===
                    Nome da Empresa: %s
                    Razão Social: %s
                    Atuação: %s
                    Imposto Mensal: R$ %.2f
                    Status do Imposto : %s
                    Limite Anual: R$ %.2f
                    Quantidade de Funcionários: %d
                    Faturamento Anual: R$ %.2f
                    Ultrapassou o Limite: %s
                    Alerta de Faturamento: %s
                    Margem de Faturamento Restante: R$ %.2f
                    Empresa Regular: %s
                    Faturamento Mensais: %s
                    """.formatted(nomeEmpresario, razaoSocial, nomeAtuacao, valorDas, statusImpostoTexto, LIMITE_ANUAL_MEI,
                    quantFuncionarios, faturamentoAnual, isUltrapassouLimiteTexto, alertaFaturamento,
                    margemFaturamentoRestante, empresaRegularTexto, faturamentosMensaisTexto);
            System.out.println(relatorio);
        }
    }

}
