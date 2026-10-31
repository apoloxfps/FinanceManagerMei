package com.devandrade.financemanagemei.dominio;

import com.devandrade.financemanagemei.exception.DominioInvalidoException;
import com.devandrade.financemanagemei.util.DateUtil;
import com.devandrade.financemanagemei.util.StringUtil;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;

public abstract class Empresa {
    private final Endereco endereco;
    private final String razaoSocial;
    private char statusImposto = 'A';
    private Double[] faturamentos;
    private final LocalDate dataAbertura;

    public Empresa(String razaoSocial, Endereco endereco, LocalDate dataAbertura) {
        this.razaoSocial = validarRazaoSocial(razaoSocial);
        if (endereco == null) {
            throw new DominioInvalidoException("Endereço não foi informado");
        }
        this.endereco = endereco;
        this.dataAbertura = validarDataAbertura(dataAbertura);
    }

    private LocalDate calcularDataVencimentoImposto() {
        LocalDate dataVencimento = LocalDate.now().plusMonths(1).withDayOfMonth(20);
        return DateUtil.validarDiaUtil(dataVencimento);
    }

    protected final String formatarDataVencimentoImposto() {
        LocalDate dataVencimento = calcularDataVencimentoImposto();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dataVencimento.format(formatador);
    }

    private static LocalDate validarDataAbertura(LocalDate data) {
        if (data == null) {
            throw new DominioInvalidoException("Data de abertura da empresa não foi informada");
        }
        if (LocalDate.now().isBefore(data)) {
            throw new DominioInvalidoException("Data Invalida, a data precisa ser menor ou igual a de hoje");
        }
        return data;
    }

    public static LocalDate verificarDataAbertura(String data) {
        if (!data.contains("/")) {
            throw new DominioInvalidoException("Data Invalida, use '/' entre os números");
        }
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        return validarDataAbertura(LocalDate.parse(data, formatador));
    }

    private Period calcularTempoAberturaEmpresa() {
        LocalDate hoje = LocalDate.now();
        return Period.between(this.dataAbertura, hoje);
    }

    protected final String formatarTempoAberturaEmpresa() {
        Period tempo = calcularTempoAberturaEmpresa();
        if (tempo.isZero()) {
            return "A Empresa foi Aberta hoje.";
        }

        StringBuilder aberturaFormatada = new StringBuilder();
        if (tempo.getYears() > 0) {
            if (tempo.getYears() == 1) {
                aberturaFormatada.append(tempo.getYears()).append(" ano");
            } else {
                aberturaFormatada.append(tempo.getYears()).append(" anos");
            }
        }
        if (tempo.getMonths() > 0) {
            if (!aberturaFormatada.isEmpty()) {
                if (tempo.getDays() > 0) {
                    aberturaFormatada.append(", ");
                } else {
                    aberturaFormatada.append(" e ");
                }
            }
            if (tempo.getMonths() == 1) {
                aberturaFormatada.append(tempo.getMonths()).append(" mês");
            } else {
                aberturaFormatada.append(tempo.getMonths()).append(" meses");
            }
        }
        if (tempo.getDays() > 0) {
            if (!aberturaFormatada.isEmpty()) {
                aberturaFormatada.append(" e ");
            }
            if (tempo.getDays() == 1) {
                aberturaFormatada.append(tempo.getDays()).append(" dia");
            } else {
                aberturaFormatada.append(tempo.getDays()).append(" dias");
            }
        }
        return aberturaFormatada.toString();
    }

    public static String validarRazaoSocial(String razaoSocial) {
        if (razaoSocial == null) {
            throw new DominioInvalidoException("A Razão social não foi informada");
        }
        String razaoSocialFormatada = StringUtil.formatString(razaoSocial);
        if (razaoSocialFormatada.length() < 15) {
            throw new DominioInvalidoException("Razão social Invalida!");
        }
        return razaoSocialFormatada;
    }

    public abstract double calcularImpostoMensal();

    public final double calcularFaturamentoAnual() {
        double faturamentoAnual = 0;
        for (Double faturamento : this.faturamentos) {
            if (faturamento != null) {
                faturamentoAnual += faturamento;
            }
        }
        return faturamentoAnual;
    }

    protected final int calcularMesesInformados() {
        int mesesInformados = 0;
        for (Double faturamento : this.faturamentos) {
            if (faturamento != null) {
                mesesInformados += 1;
            }
        }
        return mesesInformados;
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
            if (faturamentos[i] == null) {
                faturamentosSb.append("\nMês: ").append(i + 1).append(" | ").append("Faturamento Pendente...");
                continue;
            }
            faturamentosSb.append("\nMês: ").append(i + 1).append(" | ").append(String.format("Faturamento: %s", StringUtil.formatMoeda(faturamentos[i])));
        }
        return faturamentosSb.toString();
    }

    public static void validarValorMensal(double valor, int mes) {
        if (valor < 0) {
            throw new DominioInvalidoException("Faturamento do Mês " + mes + " Não pode ser negativo");
        }
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new DominioInvalidoException("O valor fornecido no mês " + mes + " não pode ser infinito ou indefinido (NaN).");
        }
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public final void setFaturamentos(Double[] faturamentos) {
        if (faturamentos == null) {
            throw new DominioInvalidoException("Sem faturamentos");
        }
        if (faturamentos.length != 12) {
            throw new DominioInvalidoException("Quantidade de Meses Incompatível");
        }
        for (int i = 0; i < faturamentos.length; i++) {
            if (faturamentos[i] == null) {
                continue;
            }
            validarValorMensal(faturamentos[i], (i + 1));
        }
        this.faturamentos = Arrays.copyOf(faturamentos, faturamentos.length);
    }

    public char getStatusImposto() {
        return statusImposto;
    }
}
