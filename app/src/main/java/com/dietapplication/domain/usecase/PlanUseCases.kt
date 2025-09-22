package com.dietapplication.domain.usecase

import com.dietapplication.data.model.GroceryItem
import com.dietapplication.data.model.Ingredient
import com.dietapplication.data.model.Recipe
import kotlin.math.max

object GroceryCalculator {

    /**
     * Seçilen tarifleri hedef porsiyona ölçekler ve birleşik market listesini döndürür.
     * @param recipes  Planlanan tarifler
     * @param servingsTarget  Hedef kişi/porsiyon sayısı (>=1)
     */
    fun buildGroceryList(
        recipes: List<Recipe>,
        servingsTarget: Int
    ): List<GroceryItem> {
        val target = max(1, servingsTarget)
        // Tüm malzemeleri hedef porsiyona göre ölçekle
        val all: List<Ingredient> = recipes.flatMap { recipe ->
            val base = max(1, recipe.servings)
            val scale = target.toFloat() / base.toFloat()

            recipe.ingredients.map { ing ->
                // data class copy kullanmak yerine güvenli: yeni nesne oluştur
                Ingredient(
                    name = ing.name,
                    unit = ing.unit,
                    amount = ing.amount * scale,
                    category = ing.category
                )
            }
        }
        return mergeIngredients(all)
    }

    /**
     * Aynı isim+birim+kategoriye sahip malzemeleri birleştirir, miktarları toplar.
     */
    private fun mergeIngredients(ingredients: List<Ingredient>): List<GroceryItem> =
        ingredients
            .groupBy { Triple( // anahtar: isim+birim+kategori
                ingredientsKeyName(it.name),
                ingredientsKeyUnit(it.unit),
                it.category.orEmpty()
            ) }
            .map { (key, list) ->
                val total = list.sumOf { it.amount.toDouble() }.toFloat()
                GroceryItem(
                    name = displayNameFromKey(key.first),
                    unit = key.second,
                    totalAmount = roundSmart(total),
                    category = key.third.ifEmpty { "Diğer" }
                )
            }
            .sortedWith(compareBy<GroceryItem> { it.category }.thenBy { it.name })

    // Key normalize yardımcıları
    private fun ingredientsKeyName(name: String) = name.trim().lowercase()
    private fun ingredientsKeyUnit(unit: String) = unit.trim().lowercase()
    private fun displayNameFromKey(keyName: String) =
        keyName.replaceFirstChar { c -> c.titlecase() }

    // 0.05 hassasiyet (ör. 123.47 -> 123.45)
    private fun roundSmart(v: Float): Float = (v * 20f).toInt() / 20f
}
