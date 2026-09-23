package com.devandrade.financemanagemei.main;
import com.devandrade.financemanagemei.dominio.EmpresaMei;

import java.util.Scanner;

public class Main {
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

            /* TODO: validação de nome/razão social/atuação deveria viver em EmpresaMei,
                não em Main — duplicação temporária até chegar em exceções (aula 95+)*/
            System.out.println("Nome Empresario: ");
            String nomeEmpresario = scanner.nextLine();
            while (nomeEmpresario.length() < 3) {
                System.out.println("Nome Invalido");
                nomeEmpresario = scanner.nextLine();
            }


            System.out.println("Razão Social: (ex: nome completo + cnpj)");
            String razaoSocial = scanner.nextLine();
            while (razaoSocial.length() < 15) {
                System.out.println("Razão Social Invalida");
                razaoSocial = scanner.nextLine();
            }

            System.out.println("Tipo de Atuação\n[ 1 ] Comercio \n[ 2 ] Industria \n[ 3 ] Prestação de Serviços\nDigite 1, 2 ou 3: ");
            int tipoAtuacao = Integer.parseInt(scanner.nextLine());
            while (tipoAtuacao <= 0 || tipoAtuacao > 3) {
                System.out.println("Atuação Invalida.");
                tipoAtuacao = Integer.parseInt(scanner.nextLine());
            }

            EmpresaMei empresa = new EmpresaMei(nomeEmpresario, razaoSocial, tipoAtuacao);

            while (true) {
                System.out.println("Possui Funcionario?: Digite S ou N");
                String respostaFuncionario = scanner.nextLine();
                if (respostaFuncionario.equalsIgnoreCase("S") || respostaFuncionario.equalsIgnoreCase("SIM")) {
                    empresa.setPossuiFuncionario(true);
                    break;
                } else if (respostaFuncionario.equalsIgnoreCase("N") || respostaFuncionario.equalsIgnoreCase("NAO") || respostaFuncionario.equalsIgnoreCase("NÃO")) {
                    empresa.setPossuiFuncionario(false);
                    break;
                }
                System.out.println("Resposta Invalida.");

            }

            double[] faturamentosArray = new double[12];
            StringBuilder faturamentosSb = new StringBuilder();
            for (int i = 0; i < faturamentosArray.length; i++) {
                System.out.printf("Digite Seu Faturamento do Mês %d: ",(i+1));
                double faturamentoMensal = Double.parseDouble(scanner.nextLine());
                faturamentosArray[i] = faturamentoMensal;
                faturamentosSb.append("\nMês: ").append(i+1).append(" | ").append(String.format("Faturamento: R$ %.2f",faturamentosArray[i]));
            }
            String faturamentosMensaisTexto = faturamentosSb.toString();
            empresa.setFaturamentos(faturamentosArray);

            String relatorio = """
                    === RELATÓRIO ===
                    Nome da Empresa: %s
                    Razão Social: %s
                    Atuação: %s
                    Imposto DAS (Mensal): R$ %.2f
                    Status do Imposto : %s
                    Possui Funcionário: %s
                    Faturamento Anual: R$ %.2f
                    Alerta de Faturamento: %s
                    Margem de Faturamento Restante: R$ %.2f
                    Empresa Regular: %s
                    Faturamento Mensais: %s
                    """.formatted(empresa.getNomeEmpresario(), empresa.getRazaoSocial(), empresa.verificarTipoAtuacao(),
                    empresa.calcularValorDas(), empresa.verificarStatusImposto(), empresa.verificarPossuiFuncionario(),
                    empresa.calcularFaturamentoAnual(), empresa.avaliarFaturamentoAnual(),
                    empresa.margemFaturamentoRestante(), empresa.verificarEmpresaRegular(), faturamentosMensaisTexto);
            System.out.println(relatorio);
        }
    }

}
