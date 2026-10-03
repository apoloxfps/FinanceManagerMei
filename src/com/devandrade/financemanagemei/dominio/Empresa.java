package com.devandrade.financemanagemei.dominio;

public abstract class Empresa {
    private final Endereco endereco;
    private final String razaoSocial;
    private char statusImposto = 'A';
    private double[] faturamentos;

    public Empresa(String razaoSocial, Endereco endereco) {
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
    }

    public final double calcularFaturamentoAnual() {
        double faturamentoAnual = 0;
        for (double faturamento : this.faturamentos) {
            faturamentoAnual += faturamento;
        }
        return faturamentoAnual;
    }

    public final String verificarStatusImposto() {
        if (this.statusImposto != 'A') {
            return "Inadimplente.";
        }
        return "Adimplente.";
    }

    protected final String formatarFaturamentoMensal() {
        if (faturamentos == null) {
            return "Sem faturamentos";
        }
        StringBuilder faturamentosSb = new StringBuilder();
        for (int i = 0; i < faturamentos.length; i++) {
            faturamentosSb.append("\nMês: ").append(i + 1).append(" | ").append(String.format("Faturamento: R$ %.2f", faturamentos[i]));
        }
        return faturamentosSb.toString();
    }

    public Endereco getEndereco() {
        return endereco;
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

    public char getStatusImposto() {
        return statusImposto;
    }
}
