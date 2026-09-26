package com.devandrade.financemanagemei.dominio;

public class EmpresaMei {
    private static final double LIMITE_ANUAL_MEI = 81000.00;
    private static int totalDeEmpresasCadastradas;
    private final String nomeEmpresario;
    private final String razaoSocial;
    private final int tipoAtuacao;
    private double[] faturamentos;
    private char statusImposto = 'A';
    private boolean possuiFuncionario;
    private final Endereco endereco;

    public EmpresaMei(String nomeEmpresario, String razaoSocial, int tipoAtuacao, Endereco endereco) {
        this.nomeEmpresario = nomeEmpresario;
        this.razaoSocial = razaoSocial;
        this.tipoAtuacao = tipoAtuacao;
        this.endereco = endereco;
        EmpresaMei.totalDeEmpresasCadastradas += 1;
    }

    public String verificarPossuiFuncionario() {
        return this.possuiFuncionario ? "SIM" : "NÃO";
    }
    public double calcularFaturamentoAnual() {
        double faturamentoAnual = 0;
        for (double faturamento : this.faturamentos) {
            faturamentoAnual += faturamento;
        }
        return faturamentoAnual;
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

    public String verificarStatusImposto () {
        if (this.statusImposto != 'A') {
            return "Inadimplente.";
        }
        return "Adimplente.";
    }

    public String verificarEmpresaRegular () {
        boolean regraRegulamentacao = this.statusImposto == 'A' && !this.isUltrapassouLimite();
        if (!regraRegulamentacao) {
            return "Empresa desregulamentada Verifique seus status de imposto e seu faturamento anual.";
        }
        return "Empresa regulamentada.";
    }

    public String getNomeEmpresario() {
        return nomeEmpresario;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setFaturamentos(double[] faturamentos) {
        if (faturamentos == null) {
            System.out.println("Sem faturamentos");
            return;
        }
        this.faturamentos = faturamentos;
    }

    public void setPossuiFuncionario(boolean possuiFuncionario) {
        this.possuiFuncionario = possuiFuncionario;
    }

    public static int getTotalDeEmpresasCadastradas() {
        return totalDeEmpresasCadastradas;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}