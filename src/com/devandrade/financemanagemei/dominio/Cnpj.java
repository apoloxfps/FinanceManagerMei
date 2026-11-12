package com.devandrade.financemanagemei.dominio;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;
import com.devandrade.financemanagemei.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Cnpj {
    private static final String CNPJ_FORMATO = "^[0-9A-Z]{2}\\.[0-9A-Z]{3}\\.[0-9A-Z]{3}/[0-9A-Z]{4}-\\d{2}$";
    private static final String APENAS_ALFANUMERICOS = "(\\w)\\1{13}";
    private static final int[] PESOS_PRIMEIRO_DIGITO = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_SEGUNDO_DIGITO = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private final String cnpj;

    public Cnpj(String cnpj) {
        this.cnpj = validarCnpj(cnpj);
    }

    public static String validarCnpj(String cnpjInformado) {
        if (cnpjInformado == null) {
            throw new DominioInvalidoException("CNPJ não foi informado.");
        }

        String cnpjFormatado = StringUtil.formatString(cnpjInformado);
        Pattern pattern = Pattern.compile(CNPJ_FORMATO);
        Matcher comparator = pattern.matcher(cnpjFormatado);
        if (!comparator.matches()) {
            throw new DominioInvalidoException("CNPJ Invalido, Tente Novamente");
        }
        String cnpjLimpo = cnpjFormatado.replaceAll("\\W", "");
        if (cnpjLimpo.matches(APENAS_ALFANUMERICOS)) {
            throw new DominioInvalidoException("CNPJ Invalido, digite um CNPJ valido");
        }


        int primeiroDigito = calcularDigitoCnpj(cnpjLimpo, PESOS_PRIMEIRO_DIGITO);
        int segundoDigito = calcularDigitoCnpj(cnpjLimpo, PESOS_SEGUNDO_DIGITO);

        if (cnpjLimpo.charAt(12) - 48 != primeiroDigito || cnpjLimpo.charAt(13) - 48 != segundoDigito) {
            throw new DominioInvalidoException("Error, esse CNPJ não existe");
        }
        return cnpjFormatado;
    }

    private static int calcularDigitoCnpj(String cnpjLimpo, int... pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            int valor = cnpjLimpo.charAt(i) - 48;
            soma += valor * pesos[i];
        }
        int resto = soma % 11;
        if (resto < 2) {
            return 0;
        } else {
            return 11 - resto;
        }
    }

    public String getCnpj() {
        return cnpj;
    }
}
