package com.isep.composeapp.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.datetime.LocalDate
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Currency
import java.util.Locale

@Composable
actual fun currentLanguageTag(): String =
    LocalConfiguration.current.locales[0].toLanguageTag()

actual fun formatPrice(price: Double, languageTag: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag(languageTag))
    format.currency = Currency.getInstance("EUR")
    return format.format(price)
}

actual fun formatDate(date: LocalDate, languageTag: String): String {
    val calendar = Calendar.getInstance().apply {
        clear()
        set(date.year, date.month.ordinal, date.day) // Calendar compte les mois à partir de 0
    }
    val format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.forLanguageTag(languageTag))
    return format.format(calendar.time)
}