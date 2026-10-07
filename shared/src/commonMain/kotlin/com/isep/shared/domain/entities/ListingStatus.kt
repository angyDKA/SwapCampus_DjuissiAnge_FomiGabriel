package com.isep.shared.domain.entities

import kotlinx.datetime.LocalDate

sealed class ListingStatus

data object Available : ListingStatus()
data class Reserved(val buyer: String) : ListingStatus()
data class Sold(val saleDate: LocalDate) : ListingStatus()

fun ListingStatus.label(): String = when (this) {
    is Available -> "Available"
    is Reserved -> "Reserved by ${buyer}"
    is Sold -> "Sold on ${saleDate}"
}
