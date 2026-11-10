package com.isep.composeapp.ui.navigation

import kotlinx.serialization.Serializable

// Typed routes: one object per screen without argument,
// one data class when the screen needs an argument (the listing id).
// @Serializable lets the navigation library turn them into text and back.

@Serializable object ListingsRoute
@Serializable object MyListingsRoute
@Serializable object ProfileRoute
@Serializable object LoginRoute
@Serializable object PostRoute
@Serializable data class EditRoute(val id: Int)