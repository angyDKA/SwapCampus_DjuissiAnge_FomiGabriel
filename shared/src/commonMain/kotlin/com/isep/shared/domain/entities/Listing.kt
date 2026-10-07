package com.isep.shared.domain.entities

import kotlinx.datetime.LocalDate

data class Listing(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: Category,
    val photoUrl: String? = null,
    val author: UserAccount,
    val status: ListingStatus = Available,
    val creationDate: LocalDate
)
