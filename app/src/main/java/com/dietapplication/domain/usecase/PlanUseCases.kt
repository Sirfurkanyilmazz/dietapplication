package com.dietapplication.domain.usecase

import com.dietapplication.data.model.*

object GroceryCalculator {

    fun buildGroceryList(
        recipes: List<Recipe>,
        servingsTarget: Int
    ): List<GroceryItem> {
        val all = recipes.flatMap { recipe ->
            val scale = servingsTarget / recipe.servings.toFloat().coerceAtLeast(1f)
            recipe.ingredients.map { ing -> ing.copy(amount = (ing.amount * scale)) }
        }
        return mergeIngredients(all)
    }

    private fun mergeIngredients(ingredients: List<Ingredient>): List<GroceryItem> {
        return ingredients
            .groupBy { Triple(it.name.lowercase().trim(), it.unit.lowercase().trim(), it.category) }
            .map { (key, list) ->
                val total = list.sumOf { it.amount.toDouble() }.toFloat()
                GroceryItem(
                    name = key.first.replaceFirstChar { it.uppercase() },
                    unit = key.second,
                    totalAmount = roundSmart(total),
                    category = key.third.ifEmpty { "Diğer" }
                )
            }
            .sortedWith(compareBy<GroceryItem> { it.category }.thenBy { it.name })
    }

    private fun roundSmart(v: Float): Float = (v * 20f).toInt() / 20f
}
