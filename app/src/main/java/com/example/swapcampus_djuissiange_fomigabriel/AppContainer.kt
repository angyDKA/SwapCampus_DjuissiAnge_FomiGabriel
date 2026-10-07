package com.example.swapcampus_djuissiange_fomigabriel

import com.isep.shared.domain.UserRole
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.UserAccount
import com.isep.shared.repository.InMemoryListingRepository
import com.isep.shared.repository.ListingRepository
import kotlinx.datetime.LocalDate

/**
 * Single place where the app creates its repositories.
 * Every ViewModel takes its repository from here, so they all share
 * the same in-memory data. The ViewModels only see the interface.
 */
object AppContainer {

    val listingRepository: ListingRepository =
        InMemoryListingRepository().also { seedDemoListings(it) }
}

/**
 * TEMPORARY demo data so the list is not empty at startup.
 * To be replaced by the mock accounts of AuthRepository (exercise 2).
 */
private fun seedDemoListings(repository: ListingRepository) {
    val alice = UserAccount(
        id = 1,
        name = "Alice Martin",
        email = "alice@isep.fr",
        passwordHash = "alice123",
        creationDate = LocalDate(2026, 9, 1),
        role = UserRole.STUDENT
    )
    val bob = UserAccount(
        id = 2,
        name = "Bob Durand",
        email = "bob@isep.fr",
        passwordHash = "bob123",
        creationDate = LocalDate(2026, 9, 1),
        role = UserRole.STUDENT
    )

    repository.postListing(
        title = "Kotlin in Action",
        description = "Second edition, almost new",
        price = 25.0,
        category = Category.BOOKS,
        author = alice
    )
    repository.postListing(
        title = "Scientific calculator",
        description = "Casio, works perfectly",
        price = 15.0,
        category = Category.ELECTRONICS,
        author = bob
    )
    repository.postListing(
        title = "Desk lamp",
        description = "LED lamp for a student desk",
        price = 10.0,
        category = Category.FURNITURE,
        author = alice
    )
    repository.postListing(
        title = "Algorithms course notes",
        description = "Complete handwritten notes, first semester",
        price = 5.0,
        category = Category.COURSES,
        author = bob
    )
}
