package com.devandrade.financemanagemei.util;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;

public class StringUtil {
    public static String formatString(String test) {
        return test.trim().toUpperCase().replaceAll("\\s+", " ");
    }
    public static String formatFaturamento(String faturamento) {
        if (faturamento.contains(".")) {
            throw new DominioInvalidoException("Não separe o milhar e Use virgula para centavos!");
        }
        return faturamento.trim().replaceAll("\\s+", "").replace(",", ".");
    }
}
