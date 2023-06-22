package com.example.mybaseandroid.extension

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class NumberExt {
    fun Int.format(digits: Int): String {
        return String.format(" %0$ { digits } d ", this)
    }

    fun Int.twoDigits() = format(2)

    fun Number.format(
        pattern: String,
        locale: Locale = Locale.getDefault(),
        roundMode: RoundingMode? = null
    ): String {
        return DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale)).apply {
            if (roundMode != null) roundingMode = roundMode
        }.format(this)
    }

    fun Number.decimalTwoDigits(): String {
        // max 2 digits after comma symbol
        return format("#, ##0.##", Locale.JAPAN)
    }

    fun Number.decimalOneDigits(): String {
        // max 1 digits after comma symbol
        return format("###, ###.#")
    }

    fun Number.decimalFourDigits(locale: Locale = Locale.getDefault()): String {
        // max 1 digits after comma symbol
        return format("###, ###.####", locale)
    }

    fun Number.decimalForceTwoDigits(locale: Locale = Locale.getDefault()): String {
        // max 2 digits after comma symbol
        return format("###, ##0.00", locale)
    }

    fun String.formatDecimal(): String {
        val number = this.filter { it.isDigit() }.toLongOrNull()
        val formatNumber = number?.format("#, ##0", Locale.JAPAN) // Sample: 10,000,123.15
        return if (formatNumber != null) this.replace(number.toString(), formatNumber) else this
    }

    fun String.formatDecimalKeepSign(pattern: String): String? {
        val formattedText = this.toDoubleOrNull()?.format(pattern, Locale.JAPAN) ?: return null
        return if (this.startsWith(" + ")) " + ".plus(formattedText) else formattedText
    }

    fun Number.decimalForceTwoDigits(): String {
        // max 2 digits after comma symbol
        return format("#, ##0.00", Locale.JAPAN)
    }

    fun Number.decimalForceThreeDigitsTotal(): String {
        return format("###0.000", Locale.JAPAN)
    }

    fun Number.decimalForceTwoDigitsTotal(): String {
        // max 2 digits after comma symbol
        return format("###0.00", Locale.JAPAN)
    }


    fun String.formatPlusValuePercent(): String {
        return if (this.isNotEmpty() && this != " - " && !this.contains(" % "))
            this.plus(" % ") else this
    }

    fun Number.formatZeroDigit(): Number {
        return if ((this.toDouble() % 1) == (0.0)) return this.toInt() else this
    }

    fun Number?.formatByPattern(pattern: String): String {
        if (this == null) {
            return "-"
        }
        val symbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
        val decimalFormat = DecimalFormat(pattern, symbols)
        return decimalFormat.format(this)
    }
}