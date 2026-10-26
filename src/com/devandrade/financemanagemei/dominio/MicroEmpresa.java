package com.devandrade.financemanagemei.dominio;


import com.devandrade.financemanagemei.util.StringUtil;

public final class MicroEmpresa extends Empresa {
    private static final double IMPOSTO_POR_CENTO = 0.06;
    private static int totalDeEmpresasCadastradas;


    public MicroEmpresa(String razaoSocial, Endereco endereco) {
        super(razaoSocial, endereco);
        MicroEmpresa.totalDeEmpresasCadastradas += 1;
    }

    @Override
    public double calcularImpostoMensal() {
        int mesesInformados = calcularMesesInformados();
        if (mesesInformados == 0) {
            return 0.0;
        }
        return (this.calcularFaturamentoAnual() * IMPOSTO_POR_CENTO) / mesesInformados;
    }

    @Override
    public String toString() {
        return """
                === RELATÓRIO ===
                Data de Emissão: %s
                Razão Social: %s
                Logradouro: %s
                Número: %d
                Cidade: %s
                Bairro: %s
                Imposto Simples Nacional: %s
                Status do Imposto: %s
                Faturamentos Mensais: %s
                Meses Informados: %d
                Faturamento Anual: %s
                """.formatted(StringUtil.emitirDataRelatorio(), this.getRazaoSocial(), this.getEndereco().getLogradouro(),
                this.getEndereco().getNumero(), this.getEndereco().getCidade(), this.getEndereco().getBairro(),
                StringUtil.formatMoeda(this.calcularImpostoMensal()), this.verificarStatusImposto(),
                this.formatarFaturamentoMensal(), this.calcularMesesInformados(),
                StringUtil.formatMoeda(this.calcularFaturamentoAnual()));
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

}
