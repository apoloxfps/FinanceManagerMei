package com.devandrade.financemanagemei.dominio;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;

public enum TipoAtuacao {
    COMERCIO(1, 71.60, "Comercio"),
    INDUSTRIA(2, 72.60, "Industria"),
    PRESTACAO_SERVICOS(3, 75.60, "Prestação de Serviços");

    private final int codigo;
    private final double valorTaxaDAS;
    private final String nomeAtuacao;

    TipoAtuacao(int codigo, double valorTaxaDAS, String nomeAtuacao) {
        this.codigo = codigo;
        this.valorTaxaDAS = valorTaxaDAS;
        this.nomeAtuacao = nomeAtuacao;
    }

    public static TipoAtuacao fromCodigo(int codigo) {
        for (TipoAtuacao tipoAtuacao : TipoAtuacao.values()) {
            if (tipoAtuacao.getCodigo() == codigo) {
                return tipoAtuacao;
            }
        }
        throw new DominioInvalidoException("Tipo de Atuação Invalida");
    }

    public int getCodigo() {
        return codigo;
    }

    public double getValorTaxaDAS() {
        return valorTaxaDAS;
    }

    public String getNomeAtuacao() {
        return nomeAtuacao;
    }
}
