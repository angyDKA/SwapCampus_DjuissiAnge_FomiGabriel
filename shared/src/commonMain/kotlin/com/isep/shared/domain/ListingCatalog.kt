package com.isep.shared.domain

import com.isep.shared.domain.entities.Available
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.UserAccount
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ListingCatalog {

    private val listings = mutableListOf<Listing>()
    private var nextId = 1

    fun allListings(): List<Listing> = listings

    fun postListing(
        title: String,
        description: String,
        price: Double,
        category: Category,
        author: UserAccount,
        photoUrl: String? = null
    ): Listing {
        val newListing = Listing(
            id = nextId,
            title = title,
            description = description,
            price = price,
            category = category,
            photoUrl = photoUrl,
            author = author,
            status = Available,
            creationDate = today()
        )

        return newListing.also {
            listings.add(it)
            nextId++
            println("Listing posted: ${it.title} (id=${it.id})")
        }
    }

    fun searchListings(
        keyword: String? = null,
        category: Category? = null
    ): List<Listing> = listings
        .available()
        .filter { listing ->
            (keyword == null ||
                listing.title.contains(keyword, ignoreCase = true) ||
                listing.description.contains(keyword, ignoreCase = true)) &&
                (category == null || listing.category == category)
        }

    fun updateListing(
        id: Int,
        title: String? = null,
        description: String? = null,
        price: Double? = null,
        category: Category? = null,
        photoUrl: String? = null
    ): Listing? {
        val index = listings.indexOfFirst { it.id == id }
        if (index == -1) return null
        val existing = listings[index]
        val updated = existing.copy(
            title = title ?: existing.title,
            description = description ?: existing.description,
            price = price ?: existing.price,
            category = category ?: existing.category,
            photoUrl = photoUrl ?: existing.photoUrl
        )
        listings[index] = updated
        return updated
    }

    fun changeStatus(id: Int, newStatus: ListingStatus): Listing? {
        val index = listings.indexOfFirst { it.id == id }
        if (index == -1) return null
        return listings[index].copy(status = newStatus).also { listings[index] = it }
    }

    fun removeListing(id: Int): Boolean = listings.removeAll { it.id == id }
}

/** Today's date, as a `kotlinx.datetime.LocalDate` (portable Android/iOS). */
private fun today() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
