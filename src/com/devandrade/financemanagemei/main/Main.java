package com.devandrade.financemanagemei.main;

import com.devandrade.financemanagemei.dominio.*;
import com.devandrade.financemanagemei.servico.ServicoContabilidade;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("=== Finance Manager Mei ===\n[ 1 ] Cadastrar Empresa MEI\n[ 2 ] Cadastrar MicroEmpresa (ME)\n[ 3 ] Sair do Sistema\nDigite 1, 2 ou 3");
            int opcao = Integer.parseInt(scanner.nextLine());

            if (opcao == 3) {
                System.out.printf("Total de Empresas MEI cadastradas nesta sessão: %d%n", EmpresaMei.getTotalDeEmpresasCadastradas());
                System.out.printf("Total de Micro Empresa (ME) cadastradas nesta sessão: %d%n", MicroEmpresa.getTotalDeEmpresasCadastradas());
                System.out.println("Sistema Finalizado.");
                break;
            }
            if (opcao != 1 && opcao != 2) {
                System.out.println("Opção Invalida, Tente Novamente.");
                continue;
            }

            /* TODO: validação de nome/razão social/atuação/logradouro/numero/cidade/bairro
                deveria viver em suas Classes de dominio, não em Main —
                duplicação temporária até chegar em exceções (aula 95+)
                TipoAtuacao tambem precisa de ter exceções*/

            System.out.println("Razão Social: (ex: nome completo + cnpj)");
            String razaoSocial = scanner.nextLine();
            while (razaoSocial.length() < 15) {
                System.out.println("Razão Social Invalida");
                razaoSocial = scanner.nextLine();
            }

            System.out.print("Logradouro da Empresa: ");
            String logradouro = scanner.nextLine();
            while (logradouro.length() < 4) {
                System.out.println("Logradouro Invalido");
                logradouro = scanner.nextLine();
            }

            System.out.print("Número da Empresa: ");
            int numeroEmpresa = Integer.parseInt(scanner.nextLine());
            while (numeroEmpresa <= 0) {
                System.out.println("Número Invalido");
                numeroEmpresa = Integer.parseInt(scanner.nextLine());
            }

            System.out.print("Cidade da Empresa: ");
            String cidadeEmpresa = scanner.nextLine();
            while (cidadeEmpresa.length() < 4) {
                System.out.println("Cidade Invalida");
                cidadeEmpresa = scanner.nextLine();
            }

            System.out.print("Bairro da Empresa: ");
            String bairroEmpresa = scanner.nextLine();
            while (bairroEmpresa.length() < 4) {
                System.out.println("Bairro Invalido");
                bairroEmpresa = scanner.nextLine();
            }

            Endereco endereco = new Endereco(logradouro, numeroEmpresa, cidadeEmpresa, bairroEmpresa);
            Empresa empresaCadastrada = null;

            if (opcao == 1) {
                System.out.println("Nome Empresario: ");
                String nomeEmpresario = scanner.nextLine();
                while (nomeEmpresario.length() < 3) {
                    System.out.println("Nome Invalido");
                    nomeEmpresario = scanner.nextLine();
                }

                System.out.println("Tipo de Atuação\n[ 1 ] Comercio \n[ 2 ] Industria \n[ 3 ] Prestação de Serviços\nDigite 1, 2 ou 3: ");
                int tipoAtuacao = Integer.parseInt(scanner.nextLine());
                while (tipoAtuacao <= 0 || tipoAtuacao > 3) {
                    System.out.println("Atuação Invalida.");
                    tipoAtuacao = Integer.parseInt(scanner.nextLine());
                }

                TipoAtuacao atuacao = TipoAtuacao.fromCodigo(tipoAtuacao);
                EmpresaMei empresaMei = new EmpresaMei(nomeEmpresario, razaoSocial, atuacao, endereco);

                while (true) {
                    System.out.println("Possui Funcionário?: Digite S ou N");
                    String respostaFuncionario = scanner.nextLine();
                    if (respostaFuncionario.equalsIgnoreCase("S") || respostaFuncionario.equalsIgnoreCase("SIM")) {
                        empresaMei.setPossuiFuncionario(true);
                        break;
                    } else if (respostaFuncionario.equalsIgnoreCase("N") || respostaFuncionario.equalsIgnoreCase("NAO") || respostaFuncionario.equalsIgnoreCase("NÃO")) {
                        empresaMei.setPossuiFuncionario(false);
                        break;
                    }
                    System.out.println("Resposta Invalida.");
                }
                empresaCadastrada = empresaMei;
            }

            if (opcao == 2) {
                empresaCadastrada = new MicroEmpresa(razaoSocial, endereco);
            }

            double[] faturamentosArray = new double[12];
            for (int i = 0; i < faturamentosArray.length; i++) {
                System.out.printf("Digite Seu Faturamento do Mês %d: ", (i + 1));
                double faturamentoMensal = Double.parseDouble(scanner.nextLine());
                faturamentosArray[i] = faturamentoMensal;
            }
            empresaCadastrada.setFaturamentos(faturamentosArray);

            System.out.println(empresaCadastrada);
            ServicoContabilidade.gerarRelatorioTributario(empresaCadastrada);
        }
    }

}
