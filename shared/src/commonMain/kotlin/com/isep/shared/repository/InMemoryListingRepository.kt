package com.isep.shared.repository

import com.isep.shared.domain.ListingCatalog
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.UserAccount

/**
 * In-memory implementation: every operation is delegated
 * to a ListingCatalog instance (the TD2 domain).
 */
class InMemoryListingRepository(
    private val catalog: ListingCatalog = ListingCatalog()
) : ListingRepository {

    override fun allListings(): List<Listing> = catalog.allListings()

    override fun searchListings(
        keyword: String?,
        category: Category?
    ): List<Listing> = catalog.searchListings(keyword, category)

    override fun postListing(
        title: String,
        description: String,
        price: Double,
        category: Category,
        author: UserAccount,
        photoUrl: String?
    ): Listing = catalog.postListing(title, description, price, category, author, photoUrl)

    override fun updateListing(
        id: Int,
        title: String?,
        description: String?,
        price: Double?,
        category: Category?,
        photoUrl: String?
    ): Listing? = catalog.updateListing(id, title, description, price, category, photoUrl)

    override fun changeStatus(id: Int, newStatus: ListingStatus): Listing? =
        catalog.changeStatus(id, newStatus)

    override fun removeListing(id: Int): Boolean = catalog.removeListing(id)
}
