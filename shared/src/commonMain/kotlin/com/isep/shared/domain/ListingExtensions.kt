package com.isep.shared.domain

import com.isep.shared.domain.entities.Available
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.label


fun List<Listing>.available(): List<Listing> =
    filter { it.status is Available }

fun Listing.displayedUrlOrDefault(): String =
    photoUrl ?: "https://swapcampus.isep.fr/img/placeholder.png"

fun Listing.describePhoto(): String =
    photoUrl?.let { url -> "Photo available: $url" } ?: "No photo"

fun Listing.summary(): String = StringBuilder().apply {
    append(title)
    append(" — ")
    append(price)
    append(" € (")
    append(status.label())
    append(")")
}.toString()
