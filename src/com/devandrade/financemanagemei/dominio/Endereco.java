package com.devandrade.financemanagemei.dominio;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;

public class Endereco {
    private final String logradouro;
    private final int numero;
    private final String cidade;
    private final String bairro;

    public Endereco(String logradouro, int numero, String cidade, String bairro) {
        this.logradouro = validarString(logradouro, "Logradouro Invalido");
        this.cidade = validarString(cidade, "Cidade Invalida");
        this.bairro = validarString(bairro, "Bairro Invalido");

        if (numero <= 0) {
            throw new DominioInvalidoException("Número Invalido.");
        }
        this.numero = numero;
    }

    private String validarString(String valor, String msgError) {
        if (valor == null || valor.trim().length() < 2) {
            throw new DominioInvalidoException(msgError);
        }
        return valor;
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
