package com.devandrade.financemanagemei.dominio;

public final class MicroEmpresa extends Empresa {
    private static final double IMPOSTO_SIMPLES_NACIONAL = 0.06;
    private static int totalDeEmpresasCadastradas;


    public MicroEmpresa(String razaoSocial, Endereco endereco) {
        super(razaoSocial, endereco);
        MicroEmpresa.totalDeEmpresasCadastradas += 1;
    }

    @Override
    public double calcularImpostoMensal() {
        return (this.calcularFaturamentoAnual() * IMPOSTO_SIMPLES_NACIONAL) / 12;
    }

    @Override
    public String toString() {
        return """
                === RELATÓRIO ===
                Razão Social: %s
                Imposto Simples Nacional: %.2f
                Status do Imposto: %s
                
                """.formatted(this.getRazaoSocial(), this.calcularImpostoMensal(), this.verificarStatusImposto());
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

}
