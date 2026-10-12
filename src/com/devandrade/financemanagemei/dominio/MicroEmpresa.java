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
        int mesesInformados = 0;
        for (Double faturamento : this.getFaturamentos()) {
            if (faturamento != null) {
                mesesInformados += 1;
            }
        }
        if (mesesInformados == 0) {
            return 0.0;
        }
        return (this.calcularFaturamentoAnual() * IMPOSTO_POR_CENTO) / mesesInformados;
    }

    @Override
    public String toString() {
        return """
                === RELATÓRIO ===
                Razão Social: %s
                Logradouro: %s
                Número: %d
                Cidade: %s
                Bairro: %s
                Imposto Simples Nacional: %.2f
                Status do Imposto: %s
                Faturamentos Mensais: %s
                Faturamento Anual: %.2f
                
                """.formatted(this.getRazaoSocial(), this.getEndereco().getLogradouro(), this.getEndereco().getNumero(),
                this.getEndereco().getCidade(), this.getEndereco().getBairro(), this.calcularImpostoMensal(),
                this.verificarStatusImposto(), this.formatarFaturamentoMensal(), this.calcularFaturamentoAnual());
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

}
