package com.isep.shared.repository

import com.isep.shared.domain.ListingCatalog
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.Listing
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.UserAccount

/**
 * In-memory implementation: every operation is delegated
 * to a ListingCatalog instance (the TD2 domain).
 *
 * The business rules (non-empty title, price > 0) are checked here,
 * in :shared, so they can be unit-tested without going through the UI.
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
    ): Listing {
        require(title.isNotBlank()) { TITLE_RULE }
        require(price > 0) { PRICE_RULE }
        return catalog.postListing(title.trim(), description.trim(), price, category, author, photoUrl)
    }

    override fun updateListing(
        id: Int,
        title: String?,
        description: String?,
        price: Double?,
        category: Category?,
        photoUrl: String?
    ): Listing? {
        title?.let { require(it.isNotBlank()) { TITLE_RULE } }
        price?.let { require(it > 0) { PRICE_RULE } }
        return catalog.updateListing(id, title?.trim(), description?.trim(), price, category, photoUrl)
    }

    override fun changeStatus(id: Int, newStatus: ListingStatus): Listing? =
        catalog.changeStatus(id, newStatus)

    override fun removeListing(id: Int): Boolean = catalog.removeListing(id)

    private companion object {
        const val TITLE_RULE = "The title must not be empty."
        const val PRICE_RULE = "The price must be greater than 0."
    }
}
