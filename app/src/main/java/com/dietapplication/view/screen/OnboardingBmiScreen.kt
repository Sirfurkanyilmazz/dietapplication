package com.dietapplication.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dietapplication.view.components.*
import com.dietapplication.domain.usecase.BmiEvaluator
import kotlin.math.roundToInt

@Composable
fun OnboardingBmiScreen(
    onNext: (age: Int, heightCm: Double, weightKg: Double) -> Unit
) {
    var age by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(0) } // 0=metrik, 1=imperial
    var height by remember { mutableStateOf("") } // cm | in
    var weight by remember { mutableStateOf("") } // kg | lb

    val ageInt = age.toIntOrNull() ?: 0
    val rawH = height.replace(",", ".").toDoubleOrNull() ?: 0.0
    val rawW = weight.replace(",", ".").toDoubleOrNull() ?: 0.0

    // birim dönüşümleri
    val heightCm = if (unit == 0) rawH else rawH * 2.54
    val weightKg = if (unit == 0) rawW else rawW * 0.45359237

    val result = remember(ageInt, heightCm, weightKg) {
        if (ageInt in 1..17) BmiEvaluator.under18Info()
        else BmiEvaluator.evaluateAdult(weightKg, heightCm)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Seni Tanıyalım", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        Text("Bu bilgilerle günlük hedeflerini netleştireceğiz.", color = MaterialTheme.colorScheme.onSurfaceVariant)

        // Birim seçimi
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Birim", style = MaterialTheme.typography.titleMedium)
            SegmentedControl(options = listOf("kg / cm", "lb / in"), selectedIndex = unit, onSelect = { unit = it })
        }

        // Giriş alanları
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            LabeledNumberField("Yaş", age, { age = it.filter { ch -> ch.isDigit() } }, "yıl", Modifier.weight(1f))
            LabeledNumberField(if (unit==0) "Boy" else "Boy", height, { height = it }, if (unit==0) "cm" else "in", Modifier.weight(1f))
            LabeledNumberField(if (unit==0) "Kilo" else "Kilo", weight, { weight = it }, if (unit==0) "kg" else "lb", Modifier.weight(1f))
        }

        // VKE kartı
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Vücut Kitle Endeksi (VKE)", style = MaterialTheme.typography.titleMedium)
                if (ageInt in 1..17) {
                    Suggest(label = result.label, color = MaterialTheme.colorScheme.secondary)
                    Text(result.advice, style = MaterialTheme.typography.bodySmall)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Suggest(label = result.label, color = when (result.category) {
                            BmiEvaluator.Category.UNDER -> MaterialTheme.colorScheme.tertiary
                            BmiEvaluator.Category.NORMAL -> MaterialTheme.colorScheme.primary
                            BmiEvaluator.Category.OVER -> MaterialTheme.colorScheme.secondary
                            BmiEvaluator.Category.OBESE_I, BmiEvaluator.Category.OBESE_II, BmiEvaluator.Category.OBESE_III ->
                                MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.outline
                        })
                        Text("VKE: ${if (result.bmi>0) result.bmi else 0.0}")
                    }
                    LinearProgressIndicator(
                        progress = normalizeBmi(result.bmi).toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(result.advice, style = MaterialTheme.typography.bodySmall)
                    Text("Referans: DSÖ (WHO) yetişkin sınıfları", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        GradientButton(
            text = "Devam Et",
            enabled = ageInt >= 1 && heightCm > 0 && weightKg > 0,
            onClick = { onNext(ageInt, (heightCm * 10).roundToInt() / 10.0, (weightKg * 10).roundToInt() / 10.0) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Suggest(label: String, color: androidx.compose.ui.graphics.Color) {
    AssistChip(
        onClick = {},
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.18f),
            labelColor = color
        )
    )
}

private fun normalizeBmi(bmi: Double): Double {
    // 15–40 aralığını 0..1'e normalize (görsel geri bildirim için)
    val clamped = bmi.coerceIn(15.0, 40.0)
    return (clamped - 15.0) / (25.0)
}
