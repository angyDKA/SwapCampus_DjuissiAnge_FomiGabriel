package com.isep.composeapp.ui.i18n


import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import platform.Foundation.*

@Composable
actual fun currentLanguageTag(): String =
    NSLocale.currentLocale.localeIdentifier

actual fun formatPrice(price: Double, languageTag: String): String {
    val formatter = NSNumberFormatter()
    formatter.numberStyle = NSNumberFormatterCurrencyStyle
    formatter.currencyCode = "EUR"
    formatter.locale = NSLocale(localeIdentifier = languageTag)
    return formatter.stringFromNumber(NSNumber(double = price)) ?: "$price €"
}

actual fun formatDate(date: LocalDate, languageTag: String): String {
    val components = NSDateComponents()
    components.year = date.year.toLong()            // NSInteger = Long sur iOS 64 bits
    components.month = (date.month.ordinal + 1).toLong()
    components.day = date.day.toLong()
    val nsDate = NSCalendar.currentCalendar.dateFromComponents(components)
        ?: return date.toString()
    val formatter = NSDateFormatter()
    formatter.dateStyle = NSDateFormatterMediumStyle
    formatter.locale = NSLocale(localeIdentifier = languageTag)
    return formatter.stringFromDate(nsDate)
}