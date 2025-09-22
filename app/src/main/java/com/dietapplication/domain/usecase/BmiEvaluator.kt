package com.dietapplication.domain.usecase

import kotlin.math.pow
import kotlin.math.roundToInt

object BmiEvaluator {
    data class BmiResult(
        val bmi: Double,
        val category: Category,
        val label: String,
        val advice: String
    )
    enum class Category { UNDER, NORMAL, OVER, OBESE_I, OBESE_II, OBESE_III, NA }

    fun evaluateAdult(weightKg: Double, heightCm: Double): BmiResult {
        if (weightKg <= 0 || heightCm <= 0) return BmiResult(0.0, Category.NA, "-", "Geçersiz değer.")
        val h = heightCm / 100.0
        val bmiRaw = weightKg / h.pow(2.0)
        val bmi = (bmiRaw * 10).roundToInt() / 10.0
        val (cat, label, advice) = when {
            bmiRaw < 18.5 -> Triple(Category.UNDER, "Zayıf", "Dengeli kilo artışı için uzman önerilir.")
            bmiRaw < 25.0 -> Triple(Category.NORMAL, "Normal", "Mevcut kiloyu koru: dengeli beslenme + hareket.")
            bmiRaw < 30.0 -> Triple(Category.OVER, "Fazla kilolu", "Kademeli kalori açığı + aktivite planı iyi olur.")
            bmiRaw < 35.0 -> Triple(Category.OBESE_I, "Obezite Sınıf I", "Kişiselleştirilmiş plan ve takip önerilir.")
            bmiRaw < 40.0 -> Triple(Category.OBESE_II, "Obezite Sınıf II", "Tıbbi değerlendirme + yakın takip gerekebilir.")
            else -> Triple(Category.OBESE_III, "Obezite Sınıf III", "Hekim kontrolünde kapsamlı kilo yönetimi gerekir.")
        }
        return BmiResult(bmi, cat, label, advice)
    }

    fun under18Info(): BmiResult =
        BmiResult(0.0, Category.NA, "Yaşa göre değerlendirilir",
            "18 yaş altı için BMI persentilleri (yaş/cinsiyet) kullanılmalıdır.")
}
