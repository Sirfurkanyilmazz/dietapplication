package com.dietapplication.data.repository

import com.dietapplication.data.model.*

interface PlansRepository {
    fun getAllRecipes(): List<Recipe>
    fun getRecipeById(id: String): Recipe?
}

class InMemoryPlansRepository : PlansRepository {
    private val recipes = listOf(
        Recipe(
            id = "r1",
            title = "Tavuklu Bulgur Pilavı",
            servings = 2,
            isBudgetFriendly = true,
            tags = listOf("öğle","akşam","ucuz"),
            ingredients = listOf(
                Ingredient("Tavuk göğüs", "g", 300f, "Et/Balık"),
                Ingredient("Bulgur", "g", 200f, "Kuru Gıda"),
                Ingredient("Soğan", "adet", 1f, "Taze Ürünler"),
                Ingredient("Biber", "adet", 1f, "Taze Ürünler"),
                Ingredient("Domates salçası", "yemek kaşığı", 1f, "Baharat/Sos"),
                Ingredient("Zeytinyağı", "yemek kaşığı", 1f, "Süt/Şarküteri"),
                Ingredient("Tuz", "çay kaşığı", .5f, "Baharat/Sos")
            )
        ),
        Recipe(
            id = "r2",
            title = "Yulaflı Yoğurt Kasesi",
            servings = 1,
            isBudgetFriendly = true,
            tags = listOf("kahvaltı","atıştırmalık","ucuz"),
            ingredients = listOf(
                Ingredient("Yulaf", "g", 60f, "Kuru Gıda"),
                Ingredient("Yoğurt", "g", 150f, "Süt/Şarküteri"),
                Ingredient("Muz", "adet", 1f, "Taze Ürünler"),
                Ingredient("Bal", "tatlı kaşığı", 1f, "Baharat/Sos")
            )
        ),
        Recipe(
            id = "r3",
            title = "Mercimek Çorbası",
            servings = 4,
            isBudgetFriendly = true,
            tags = listOf("öğle","akşam","ucuz","vegan"),
            ingredients = listOf(
                Ingredient("Kırmızı mercimek", "g", 250f, "Kuru Gıda"),
                Ingredient("Soğan", "adet", 1f, "Taze Ürünler"),
                Ingredient("Havuç", "adet", 1f, "Taze Ürünler"),
                Ingredient("Zeytinyağı", "yemek kaşığı", 1f, "Süt/Şarküteri"),
                Ingredient("Tuz", "çay kaşığı", .5f, "Baharat/Sos"),
                Ingredient("Kimyon", "çay kaşığı", .5f, "Baharat/Sos")
            )
        )
    )

    override fun getAllRecipes(): List<Recipe> = recipes
    override fun getRecipeById(id: String): Recipe? = recipes.find { it.id == id }
}
