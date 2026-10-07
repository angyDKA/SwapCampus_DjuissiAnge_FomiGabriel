package com.isep.shared.domain.entities


enum class Category {
    BOOKS,
    COURSES,
    ELECTRONICS,
    SPORTS_LEISURE,
    FURNITURE,
    CLOTHING,
    OTHER
}

fun Category.label(): String = when (this) {
    Category.BOOKS -> "Books"
    Category.COURSES -> "Courses"
    Category.ELECTRONICS -> "Electronics"
    Category.SPORTS_LEISURE -> "Sports & leisure"
    Category.FURNITURE -> "Furniture"
    Category.CLOTHING -> "Clothing"
    Category.OTHER -> "Other"
}
