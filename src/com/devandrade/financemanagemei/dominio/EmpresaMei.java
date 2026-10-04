package com.devandrade.financemanagemei.dominio;

public final class EmpresaMei extends Empresa {
    private static final double LIMITE_ANUAL_MEI = 81000.00;
    private static int totalDeEmpresasCadastradas;
    private final String nomeEmpresario;
    private final TipoAtuacao tipoAtuacao;
    private boolean possuiFuncionario;

    public EmpresaMei(String nomeEmpresario, String razaoSocial, TipoAtuacao tipoAtuacao, Endereco endereco) {
        super(razaoSocial, endereco);
        this.nomeEmpresario = nomeEmpresario;
        this.tipoAtuacao = tipoAtuacao;
        EmpresaMei.totalDeEmpresasCadastradas += 1;
    }

    private String verificarPossuiFuncionario() {
        return this.possuiFuncionario ? "SIM" : "NÃO";
    }

    private boolean isUltrapassouLimite() {
        return this.calcularFaturamentoAnual() > LIMITE_ANUAL_MEI;
    }

    private String avaliarFaturamentoAnual() {
        double valorAlertaDePerigo = LIMITE_ANUAL_MEI * 0.8;
        if (this.isUltrapassouLimite()) {
            return "ALERTA CRÍTICO: A EMPRESA ULTRAPASSOU O LIMITE ANUAL! Procure um contador ";
        } else if (this.calcularFaturamentoAnual() >= valorAlertaDePerigo) {
            return "ATENÇÃO: Você atingiu 80% do LIMITE ANUAL. Monitore suas notas fiscais.";
        } else {
            return "Status seguro -> Faturamento dentro da margem operacional.";
        }
    }

    private double margemFaturamentoRestante() {
        return LIMITE_ANUAL_MEI - this.calcularFaturamentoAnual();
    }

    private String verificarEmpresaRegular() {
        boolean regraRegulamentacao = this.getStatusImposto() == 'A' && !this.isUltrapassouLimite();
        if (!regraRegulamentacao) {
            return "Empresa desregulamentada Verifique seus status de imposto e seu faturamento anual.";
        }
        return "Empresa regulamentada.";
    }

    @Override
    public double calcularImpostoMensal() {
        return this.tipoAtuacao.getValorTaxaDAS();
    }

    @Override
    public String toString() {
        return """
                === RELATÓRIO ===
                Nome da Empresa: %s
                Razão Social: %s
                Atuação: %s
                Logradouro: %s
                Número: %d
                Cidade: %s
                Bairro: %s
                Imposto DAS (Mensal): R$ %.2f
                Status do Imposto : %s
                Possui Funcionário: %s
                Faturamento Anual: R$ %.2f
                Alerta de Faturamento: %s
                Margem de Faturamento Restante: R$ %.2f
                Empresa Regular: %s
                Faturamento Mensais: %s
                """.formatted(this.nomeEmpresario, this.getRazaoSocial(), this.tipoAtuacao.getNomeAtuacao(),
                this.getEndereco().getLogradouro(), this.getEndereco().getNumero(),
                this.getEndereco().getCidade(), this.getEndereco().getBairro(),
                this.calcularImpostoMensal(), this.verificarStatusImposto(), this.verificarPossuiFuncionario(),
                this.calcularFaturamentoAnual(), this.avaliarFaturamentoAnual(),
                this.margemFaturamentoRestante(), this.verificarEmpresaRegular(), this.formatarFaturamentoMensal());
    }

    public void setPossuiFuncionario(boolean possuiFuncionario) {
        this.possuiFuncionario = possuiFuncionario;
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

}