package com.devandrade.financemanagemei.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtil {

    public static LocalDate validarDiaUtil(LocalDate data) {
        if (data.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return data.plusDays(2);
        }
        if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return data.plusDays(1);
        }
        return data;
    }
}
