package com.molt.util;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyUtils {

    private MoneyUtils() {
    }

    public static String formatCents(long cents) {
        NumberFormat nf = NumberFormat.getIntegerInstance(Locale.FRANCE);
        return nf.format(cents / 100) + " €";
    }
}
