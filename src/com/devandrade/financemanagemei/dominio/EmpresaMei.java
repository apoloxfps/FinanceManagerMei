package com.devandrade.financemanagemei.dominio;

public class EmpresaMei extends Empresa{
    private static final double LIMITE_ANUAL_MEI = 81000.00;
    private static int totalDeEmpresasCadastradas;
    private final String nomeEmpresario;
    private final int tipoAtuacao;
    private boolean possuiFuncionario;

    public EmpresaMei(String nomeEmpresario, String razaoSocial, int tipoAtuacao, Endereco endereco) {
        super(razaoSocial, endereco);
        this.nomeEmpresario = nomeEmpresario;
        this.tipoAtuacao = tipoAtuacao;
        EmpresaMei.totalDeEmpresasCadastradas += 1;
    }

    public String verificarPossuiFuncionario() {
        return this.possuiFuncionario ? "SIM" : "NÃO";
    }

    public double calcularValorDas() {
        return switch (this.tipoAtuacao) {
            case 1 -> 71.60;
            case 2 -> 72.60;
            case 3 -> 75.60;
            default -> 0.0;
        };
    }

    public String verificarTipoAtuacao() {
        return switch (this.tipoAtuacao) {
            case 1 -> "Comercio";
            case 2 -> "Industria";
            case 3 -> "Prestação de Serviços";
            default -> "Invalido";
        };
    }

    private boolean isUltrapassouLimite () {
        return this.calcularFaturamentoAnual() > LIMITE_ANUAL_MEI;
    }

    public String avaliarFaturamentoAnual () {
        double valorAlertaDePerigo = LIMITE_ANUAL_MEI * 0.8;
        if (this.isUltrapassouLimite()) {
            return "ALERTA CRÍTICO: A EMPRESA ULTRAPASSOU O LIMITE ANUAL! Procure um contador ";
        } else if (this.calcularFaturamentoAnual() >= valorAlertaDePerigo) {
            return "ATENÇÃO: Você atingiu 80% do LIMITE ANUAL. Monitore suas notas fiscais.";
        } else {
            return "Status seguro -> Faturamento dentro da margem operacional.";
        }
    }

    public double margemFaturamentoRestante () {
        return LIMITE_ANUAL_MEI - this.calcularFaturamentoAnual();
    }

    public String verificarEmpresaRegular () {
        boolean regraRegulamentacao = this.getStatusImposto() == 'A' && !this.isUltrapassouLimite();
        if (!regraRegulamentacao) {
            return "Empresa desregulamentada Verifique seus status de imposto e seu faturamento anual.";
        }
        return "Empresa regulamentada.";
    }

    public String getNomeEmpresario() {
        return nomeEmpresario;
    }

    public void setPossuiFuncionario(boolean possuiFuncionario) {
        this.possuiFuncionario = possuiFuncionario;
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

}