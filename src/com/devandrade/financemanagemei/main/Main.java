package com.devandrade.financemanagemei.main;

import com.devandrade.financemanagemei.dominio.*;
import com.devandrade.financemanagemei.exception.DominioInvalidoException;
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

            String razaoSocial;
            while (true) {
                try {
                    System.out.println("Razão Social: (ex: nome completo + cnpj)");
                    razaoSocial = scanner.nextLine();
                    Empresa.validarRazaoSocial(razaoSocial);
                    break;
                } catch (DominioInvalidoException e) {
                    System.out.println("Houve um erro: " + e.getMessage());
                }
            }

            Endereco endereco;
            while (true) {
                try {
                    System.out.print("Logradouro da Empresa: ");
                    String logradouro = scanner.nextLine();

                    System.out.print("Número da Empresa: ");
                    int numeroEmpresa = Integer.parseInt(scanner.nextLine());

                    System.out.print("Cidade da Empresa: ");
                    String cidadeEmpresa = scanner.nextLine();

                    System.out.print("Bairro da Empresa: ");
                    String bairroEmpresa = scanner.nextLine();

                    endereco = new Endereco(logradouro, numeroEmpresa, cidadeEmpresa, bairroEmpresa);
                    break;
                } catch (DominioInvalidoException e) {
                    System.out.println("Endereço Invalido -> " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Número do endereço precisa ser numérico");
                }
            }

            Empresa empresaCadastrada = null;
            if (opcao == 2) {
                empresaCadastrada = new MicroEmpresa(razaoSocial, endereco);
            }

            if (opcao == 1) {
                EmpresaMei empresaMei;
                while (true) {
                    try {
                        System.out.println("Nome Empresario: ");
                        String nomeEmpresario = scanner.nextLine();

                        System.out.println("Tipo de Atuação\n[ 1 ] Comercio \n[ 2 ] Industria \n[ 3 ] Prestação de Serviços\nDigite 1, 2 ou 3: ");
                        int tipoAtuacao = Integer.parseInt(scanner.nextLine());

                        TipoAtuacao atuacao = TipoAtuacao.fromCodigo(tipoAtuacao);
                        empresaMei = new EmpresaMei(nomeEmpresario, razaoSocial, atuacao, endereco);
                        break;
                    } catch (DominioInvalidoException e) {
                        System.out.println("Houve um erro: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Valor precisa ser numérico");
                    }
                }

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

            double[] faturamentosArray = new double[12];
            for (int i = 0; i < faturamentosArray.length; i++) {
                try {
                    System.out.printf("Digite Seu Faturamento do Mês %d: ", (i + 1));
                    double faturamentoMensal = Double.parseDouble(scanner.nextLine());
                    Empresa.validarValorMensal(faturamentoMensal, (i + 1));
                    faturamentosArray[i] = faturamentoMensal;
                } catch (DominioInvalidoException e) {
                    System.out.println("Erro:" + e.getMessage());
                    i--;
                } catch (NumberFormatException e) {
                    System.out.println("Faturamento do Mês " + (i + 1) + " precisa ser numérico");
                    i--;
                }
            }
            empresaCadastrada.setFaturamentos(faturamentosArray);

            System.out.println(empresaCadastrada);
            ServicoContabilidade.gerarRelatorioTributario(empresaCadastrada);
        }
    }

}
