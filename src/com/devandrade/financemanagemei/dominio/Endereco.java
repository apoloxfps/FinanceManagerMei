package com.devandrade.financemanagemei.dominio;

public class Endereco {
    private final String logradouro;
    private final int numero;
    private final String cidade;
    private final String bairro;

    public Endereco(String logradouro, int numero, String cidade, String bairro) {
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.numero = numero;
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBairro() {
        return bairro;
    }
}
