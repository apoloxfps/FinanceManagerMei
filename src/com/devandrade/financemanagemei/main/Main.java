package com.devandrade.financemanagemei.main;

public class Main {
    public static void main(String[] args) {
        String nomeEmpresario = "DevAndrade.";
        String razaoSocial = "Nicolas Cauã da Silva Andrade 12345678900";
        double faturamentoMensal = 5000.00;
        double limiteAnual = 81000.00;
        byte quantFuncionarios = 1;
        char statusImposto = 'A';
        double faturamentoAnual = faturamentoMensal * 12;
        double margemFaturamentoRestante = limiteAnual - faturamentoAnual;
        boolean isUltrapassouLimite = faturamentoAnual > limiteAnual;
        boolean isEmpresaRegular = statusImposto == 'A' && !isUltrapassouLimite;
        String relatorio = """
                === RELATÓRIO ===
                Nome da Empresa: %s
                Razão Social: %s
                Faturamento Mensal: R$ %.2f
                Limite Anual: R$ %.2f
                Quantidade de Funcionários: %d
                Faturamento Anual: R$ %.2f
                Ultrapassou o Limite: %b
                Margem de Faturamento Restante: R$ %.2f
                Empresa Regular: %b
                """.formatted(nomeEmpresario,razaoSocial, faturamentoMensal, limiteAnual,
                quantFuncionarios, faturamentoAnual, isUltrapassouLimite,
                margemFaturamentoRestante, isEmpresaRegular);
        System.out.println(relatorio);
    }
}

