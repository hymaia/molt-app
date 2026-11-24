package com.molt.util

import java.text.NumberFormat
import java.util.Locale

object MoneyUtils {

    fun formatCents(cents: Long): String {
        val nf = NumberFormat.getIntegerInstance(Locale.FRANCE)
        return nf.format(cents / 100) + " €"
    }
}
