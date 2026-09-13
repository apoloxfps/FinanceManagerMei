package com.devandrade.financemanagemei.main;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Finance Manager Mei ===\n[ 1 ] Imprimir Relatório.\n[ 2 ] Sair do Sistema\nDigite 1 ou 2!");
            byte opcao = Byte.parseByte(scanner.nextLine());

            if (opcao == 1){
                System.out.println("Nome Empresario: ");
                String nomeEmpresario = scanner.nextLine();

                System.out.println("Razão Social: (ex: nome completo + cnpj)");
                String razaoSocial = scanner.nextLine();

                System.out.println("Tipo de Atuação\n[ 1 ] Comercio \n[ 2 ] Industria \n[ 3 ] Prestação de Serviços\nDigite 1, 2 ou 3: ");
                byte tipoAtuacao = Byte.parseByte(scanner.nextLine());
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

                System.out.println("Digite Seu Fatamento Mensal: ");
                double faturamentoMensal = Double.parseDouble(scanner.nextLine());

                double limiteAnual = 81000.00;
                byte quantFuncionarios = 1;
                char statusImposto = 'A';
                String statusImpostoTexto = statusImposto == 'A' ? "Adimplente." : "Inadimplente.";
                double faturamentoAnual = faturamentoMensal * 12;
                double margemFaturamentoRestante = limiteAnual - faturamentoAnual;
                boolean isUltrapassouLimite = faturamentoAnual > limiteAnual;
                double valorZonaDePerigo = limiteAnual * 0.8;
                String mensagemAlertaFaturamento;
                if (isUltrapassouLimite) {
                    mensagemAlertaFaturamento = "ALERTA CRÍTICO: Limite estourado! Procure um contador " +
                            "para transição para Microempresa (ME).";
                } else if (faturamentoAnual >= valorZonaDePerigo) {
                    mensagemAlertaFaturamento = "ATENÇÃO: Você atingiu 80% do teto anual. Monitore suas notas fiscais.";
                } else {
                    mensagemAlertaFaturamento = "Status seguro: Faturamento dentro da margem operacional.";
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
                        Faturamento Mensal: R$ %.2f
                        Limite Anual: R$ %.2f
                        Quantidade de Funcionários: %d
                        Faturamento Anual: R$ %.2f
                        Ultrapassou o Limite: %s
                        Margem de Faturamento Restante: R$ %.2f
                        Empresa Regular: %s
                        """.formatted(nomeEmpresario, razaoSocial, nomeAtuacao, valorDas, statusImpostoTexto, faturamentoMensal, limiteAnual,
                        quantFuncionarios, faturamentoAnual, mensagemAlertaFaturamento,
                        margemFaturamentoRestante, empresaRegularTexto);
                System.out.println(relatorio);
            } else if (opcao == 2) {
                System.out.println("Sistema Finalizado.");
                break;
            } else {
                System.out.println("Opção Invalida, Tente Novamente.");
            }
        }

    }
}