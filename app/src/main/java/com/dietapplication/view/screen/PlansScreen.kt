package com.dietapplication.view.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dietapplication.data.model.GroceryItem
import com.dietapplication.data.model.Recipe
import com.dietapplication.ui.feature.plans.PlansViewModel

/* ---- küçük UI yardımcıları (lokal) ---- */
@Composable
private fun PillCounter(text: String) {
    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.primaryContainer) {
        Text(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLarge)
    }
}
@Composable
private fun SectionCard(title: String? = null, content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(14.dp)) {
            if (title != null) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
            }
            content()
        }
    }
}
@Composable
private fun SelectableChip(label: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(onClick = onClick, label = { Text(label) },
        leadingIcon = { if (selected) Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp)) })
}

/* ---- ekran ---- */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlansScreen(nav: androidx.navigation.NavHostController, vm: PlansViewModel = viewModel()) {
    var tab by remember { mutableStateOf(0) } // 0=Tarifler, 1=Plan, 2=Market
    val state by vm.state.collectAsState()

    Column(Modifier.fillMaxSize()) {

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text("PLANLAR", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text("3 tarif seç → 1 dakikada haftalık menü", style = MaterialTheme.typography.bodyMedium)
            }
            AnimatedVisibility(visible = state.selected.isNotEmpty(), enter = expandVertically(), exit = shrinkVertically()) {
                PillCounter("${state.selected.size} seçili")
            }
            IconButton(onClick = { /* filtre dialog */ }) { Icon(Icons.Default.FilterList, contentDescription = "Filtre") }
        }

        TabRow(selectedTabIndex = tab) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Tarifler") })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Plan") })
            Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("Market") })
        }

        when (tab) {
            0 -> RecipesGridTab(vm)
            1 -> WeeklyPlanTab(vm)
            2 -> GroceryStickyList(vm)
        }
    }
}

/* ---------- TAB 0: TARİFLER ---------- */
@Composable
private fun RecipesGridTab(vm: PlansViewModel) {
    val state by vm.state.collectAsState()
    var filters by remember { mutableStateOf(setOf<String>()) }
    val allTags = remember(state.allRecipes) { state.allRecipes.flatMap { it.tags }.distinct() }
    val filtered = remember(state.allRecipes, filters) {
        if (filters.isEmpty()) state.allRecipes else state.allRecipes.filter { r -> r.tags.any { it in filters } }
    }

    LazyRow(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(allTags) { t -> SelectableChip(t, t in filters) {
            filters = filters.toMutableSet().apply { if (contains(t)) remove(t) else add(t) }
        } }
    }
    Spacer(Modifier.height(10.dp))

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 170.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(filtered, key = { it.id }) { recipe ->
            RecipeCardLarge(recipe, selected = recipe.id in state.selected) { vm.toggleSelect(recipe.id) }
        }
    }
}

@Composable
private fun RecipeCardLarge(recipe: Recipe, selected: Boolean, onToggle: () -> Unit) {
    ElevatedCard(onClick = onToggle) {
        AsyncImage(
            model = null, contentDescription = null,
            modifier = Modifier.height(110.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surface)
        )
        Column(Modifier.padding(12.dp)) {
            Text(recipe.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(6.dp))
            Text("${recipe.servings} porsiyon • ${if (recipe.isBudgetFriendly) "Bütçe dostu" else ""}",
                style = MaterialTheme.typography.bodySmall)
            if (recipe.tags.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                androidx.compose.foundation.layout.FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    recipe.tags.take(4).forEach { t -> AssistChip(onClick = {}, label = { Text(t) }) }
                }
            }
        }
        if (selected) LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth(), trackColor = MaterialTheme.colorScheme.primaryContainer)
    }
}

/* ---------- TAB 1: PLAN ---------- */
@Composable
private fun WeeklyPlanTab(vm: PlansViewModel) {
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionCard("Kişi sayısı (porsiyon)") {
            Slider(
                value = state.servingsTarget.toFloat(),
                onValueChange = { vm.changeServings(it.toInt()) },
                valueRange = 1f..8f, steps = 6
            )
            Text("${state.servingsTarget} kişi için planlanacak", style = MaterialTheme.typography.bodySmall)
        }
        SectionCard("Seçili Tarifler") {
            if (state.selected.isEmpty()) {
                Text("Henüz seçim yok. Tarifler sekmesinden en az 3 tarif seç.", style = MaterialTheme.typography.bodyMedium)
            } else {
                val map = state.allRecipes.associateBy { it.id }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.selected.mapNotNull { map[it] }.forEach { r -> ElevatedCard { Text(r.title, Modifier.padding(12.dp)) } }
                }
            }
        }
        Button(onClick = { vm.generateGrocery() }, enabled = state.selected.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
            Text("Market Listesi Oluştur")
        }
    }
}

/* ---------- TAB 2: MARKET (Sticky başlıklar) ---------- */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GroceryStickyList(vm: PlansViewModel) {
    val state by vm.state.collectAsState()
    val grouped = remember(state.grocery) { state.grocery.groupBy { it.category } }

    if (state.grocery.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Market listesi boş. Önce planı oluştur.") }
        return
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize().padding(bottom = 72.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
            grouped.forEach { (cat, items) ->
                stickyHeader {
                    Surface(color = MaterialTheme.colorScheme.background, tonalElevation = 6.dp, modifier = Modifier.fillMaxWidth()) {
                        Text(cat, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
                    }
                }
                items(items) { i -> GroceryRow(i) }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
        BottomShareBar(buildShareText(grouped), Modifier.align(Alignment.BottomCenter).fillMaxWidth())
    }
}

@Composable private fun GroceryRow(item: GroceryItem) {
    ListItem(headlineContent = { Text(item.name) }, supportingContent = { Text("${item.totalAmount} ${item.unit}") }, tonalElevation = 2.dp)
}

@Composable private fun BottomShareBar(text: String, modifier: Modifier = Modifier) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    Surface(tonalElevation = 8.dp, shadowElevation = 8.dp, modifier = modifier) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { shareText(ctx, text) }, modifier = Modifier.weight(1f)) { Text("Paylaş (Metin)") }
            FilledTonalButton(onClick = { shareText(ctx, text) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Share, contentDescription = null); Spacer(Modifier.width(6.dp)); Text("Dışa Aktar")
            }
        }
    }
}

private fun buildShareText(grouped: Map<String, List<GroceryItem>>): String {
    val sb = StringBuilder("MARKET LİSTESİ\n")
    grouped.forEach { (cat, items) ->
        sb.appendLine().appendLine("• $cat")
        items.forEach { i -> sb.appendLine("  - ${i.name}: ${i.totalAmount} ${i.unit}") }
    }
    return sb.toString()
}

private fun shareText(ctx: android.content.Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Market Listesini Paylaş")
    ctx.startActivity(shareIntent)
}
