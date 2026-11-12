package com.devandrade.financemanagemei.util;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String formatString(String test) {
        return test.trim().toUpperCase().replaceAll("\\s+", " ");
    }

    public static String formatFaturamento(String faturamento) {
        String faturamentoFormatado = faturamento.trim();
        Pattern pattern = Pattern.compile("^\\d+(,\\d{1,2})?$");
        Matcher compararFaturamento = pattern.matcher(faturamentoFormatado);
        if (!compararFaturamento.matches()) {
            throw new DominioInvalidoException("Faturamento Invalido, Informe um valor valido (ex: 1500,50)");
        }
        return faturamentoFormatado.replace(",", ".");
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
