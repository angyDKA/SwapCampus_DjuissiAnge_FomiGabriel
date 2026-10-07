package com.isep.shared.repository

import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.UserAccount

/**
 * Contract used by the ViewModels to work with listings.
 * The UI only knows this interface, never ListingCatalog directly,
 * so the implementation can be replaced later (Room, Firestore...)
 * without touching the ViewModels.
 */
interface ListingRepository {

    fun allListings(): List<Listing>

    fun searchListings(
        keyword: String? = null,
        category: Category? = null
    ): List<Listing>

    fun postListing(
        title: String,
        description: String,
        price: Double,
        category: Category,
        author: UserAccount,
        photoUrl: String? = null
    ): Listing

    fun updateListing(
        id: Int,
        title: String? = null,
        description: String? = null,
        price: Double? = null,
        category: Category? = null,
        photoUrl: String? = null
    ): Listing?

    fun changeStatus(id: Int, newStatus: ListingStatus): Listing?

    fun removeListing(id: Int): Boolean
}
