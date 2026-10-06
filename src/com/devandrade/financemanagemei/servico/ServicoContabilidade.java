package com.devandrade.financemanagemei.servico;

import com.devandrade.financemanagemei.dominio.Empresa;

public class ServicoContabilidade {

    public static void gerarRelatorioTributario(Empresa empresa) {
        System.out.println(empresa.getRazaoSocial());
        System.out.printf("Imposto Mensal: %.2f%n", empresa.calcularImpostoMensal());
    }
}
