package com.devandrade.financemanagemei.dominio;

public class Empresa {
    private final Endereco endereco;
    private final String razaoSocial;
    private char statusImposto = 'A';
    private double[] faturamentos;

    public Empresa(String razaoSocial, Endereco endereco) {
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
    }

    public double calcularFaturamentoAnual() {
        double faturamentoAnual = 0;
        for (double faturamento : this.faturamentos) {
            faturamentoAnual += faturamento;
        }
        return faturamentoAnual;
    }

    public String verificarStatusImposto () {
        if (this.statusImposto != 'A') {
            return "Inadimplente.";
        }
        return "Adimplente.";
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
