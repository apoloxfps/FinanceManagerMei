package com.devandrade.financemanagemei.util;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public static String formatMoeda(double valor) {
        Locale local = new Locale("pt", "BR");
        NumberFormat valorFormato = NumberFormat.getCurrencyInstance(local);
        return valorFormato.format(valor);
    }

    public static String emitirDataRelatorio() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formatador);
    }

}
