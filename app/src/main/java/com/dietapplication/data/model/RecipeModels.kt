package com.dietapplication.data.model

data class Ingredient(
    val name: String,
    val unit: String,
    val amount: Float,
    val category: String = ""
)

data class Recipe(
    val id: String,
    val title: String,
    val servings: Int,
    val ingredients: List<Any>,
    val isBudgetFriendly: Boolean = true,
    val tags: List<String> = emptyList()
)

data class WeeklyPlan(
    val weekOf: String,
    val selectedRecipeIds: List<String>
)

data class GroceryItem(
    val name: String,
    val unit: String,
    val totalAmount: Float,
    val category: String
)
