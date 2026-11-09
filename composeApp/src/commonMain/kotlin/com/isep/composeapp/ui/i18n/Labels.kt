package com.isep.composeapp.ui.i18n

import androidx.compose.runtime.Composable
import com.isep.composeapp.resources.*
import com.isep.shared.domain.entities.Available
import com.isep.shared.domain.entities.Category
import com.isep.shared.domain.entities.ListingStatus
import com.isep.shared.domain.entities.Reserved
import com.isep.shared.domain.entities.Sold
import org.jetbrains.compose.resources.stringResource


@Composable
fun Category.text(): String = stringResource(
    when (this) {
        Category.BOOKS -> Res.string.category_books
        Category.COURSES -> Res.string.category_courses
        Category.ELECTRONICS -> Res.string.category_electronics
        Category.SPORTS_LEISURE -> Res.string.category_sports_leisure
        Category.FURNITURE -> Res.string.category_furniture
        Category.CLOTHING -> Res.string.category_clothing
        Category.OTHER -> Res.string.category_other
    }
)


@Composable
fun ListingStatus.text(): String = when (this) {
    is Available -> stringResource(Res.string.status_available)
    is Reserved -> stringResource(Res.string.status_reserved, buyer)
    is Sold -> stringResource(Res.string.status_sold, formatDate(saleDate, currentLanguageTag()))
}