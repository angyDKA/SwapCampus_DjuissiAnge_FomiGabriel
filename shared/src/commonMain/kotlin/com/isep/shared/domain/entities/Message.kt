package com.isep.shared.domain.entities

import kotlinx.datetime.LocalDateTime


data class Message(
    val id: Int,
    val listing: Listing,
    val sender: UserAccount,
    val recipient: UserAccount,
    val content: String,
    val sentDate: LocalDateTime
)
