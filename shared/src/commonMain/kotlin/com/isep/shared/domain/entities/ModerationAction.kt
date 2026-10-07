package com.isep.shared.domain.entities

import kotlinx.datetime.LocalDateTime

sealed class ModerationTarget
data class ListingTarget(val listing: Listing) : ModerationTarget()
data class AccountTarget(val account: UserAccount) : ModerationTarget()


data class ModerationAction(
    val id: Int,
    val target: ModerationTarget,
    val type: ModerationActionType,
    val reason: String,
    val administrator: UserAccount,
    val date: LocalDateTime
)
