package com.devandrade.financemanagemei.dominio;

public final class MicroEmpresa extends Empresa {
    private static final double IMPOSTO_POR_CENTO = 0.06;
    private static int totalDeEmpresasCadastradas;


    public MicroEmpresa(String razaoSocial, Endereco endereco) {
        super(razaoSocial, endereco);
        MicroEmpresa.totalDeEmpresasCadastradas += 1;
    }

    @Override
    public double calcularImpostoMensal() {
        return (this.calcularFaturamentoAnual() * IMPOSTO_POR_CENTO) / 12;
    }

    @Override
    public String toString() {
        return """
                === RELATÓRIO ===
                Razão Social: %s
                Imposto Simples Nacional: %.2f
                Status do Imposto: %s
                Faturamentos Mensais: %s
                Faturamento Anual: %.2f
                
                """.formatted(this.getRazaoSocial(), this.calcularImpostoMensal(),
                this.verificarStatusImposto(), this.formatarFaturamentoMensal(), this.calcularFaturamentoAnual());
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

}
