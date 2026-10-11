package com.devandrade.financemanagemei.dominio;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;

import java.util.Arrays;

public abstract class Empresa {
    private final Endereco endereco;
    private final String razaoSocial;
    private char statusImposto = 'A';
    private double[] faturamentos;

    public Empresa(String razaoSocial, Endereco endereco) {
        this.razaoSocial = validarRazaoSocial(razaoSocial);
        if (endereco == null) {
            throw new DominioInvalidoException("Endereço não foi informado");
        }
        this.endereco = endereco;
    }

    public static String validarRazaoSocial(String razaoSocial) {
        if (razaoSocial == null || razaoSocial.trim().length() < 15) {
            throw new DominioInvalidoException("Razão social Invalida!");
        }
        return razaoSocial;
    }

    public static void validarValorMensal(double valor, int mes) {
        if (valor < 0) {
            throw new DominioInvalidoException("Faturamento do Més " + mes + " Não pode ser negativo");
        }
    }

    public abstract double calcularImpostoMensal();

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

    public final void setFaturamentos(double[] faturamentos) {
        if (faturamentos == null) {
            throw new DominioInvalidoException("Sem faturamentos");
        }
        if (faturamentos.length != 12) {
            throw new DominioInvalidoException("Quantidade de Meses Incompatível");
        }
        for (int i = 0; i < faturamentos.length; i++) {
            validarValorMensal(faturamentos[i], (i + 1));
        }
        this.faturamentos = Arrays.copyOf(faturamentos, faturamentos.length);
    }

    public char getStatusImposto() {
        return statusImposto;
    }
}
