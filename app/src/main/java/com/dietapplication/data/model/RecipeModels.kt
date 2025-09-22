package com.dietapplication.data.model

import java.util.UUID

/**
 * Market/plan hesapları için basit veri modelleri.
 * Tüm adlar proje genelinde bu isimlerle kullanılacak:
 *  - Ingredient: name, unit, amount, category
 *  - GroceryItem: name, unit, totalAmount, category
 *  - Recipe: id, title, servings, isBudgetFriendly, tags, ingredients
 */

data class Ingredient(
    val name: String,
    val unit: String,      // "g", "ml", "adet" vb.
    val amount: Float,     // tarifteki tek porsiyon/servings başına değil, RECIPE bazında (Recipe.servings için toplam)
    val category: String   // "Kuru Gıda", "Taze Ürünler", "Baharat/Sos" vb.
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val servings: Int,                 // bu tarif toplam kaç porsiyon çıkıyor
    val isBudgetFriendly: Boolean = false,
    val tags: List<String> = emptyList(),
    val ingredients: List<Ingredient> = emptyList()
)

data class GroceryItem(
    val name: String,
    val unit: String,
    val totalAmount: Float,
    val category: String
)
