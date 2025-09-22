package com.dietapplication.ui.feature.plans

import androidx.lifecycle.ViewModel
import com.dietapplication.data.repository.InMemoryPlansRepository
import com.dietapplication.data.model.GroceryItem
import com.dietapplication.data.model.Recipe
import com.dietapplication.domain.usecase.GroceryCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class PlansUiState(
    val allRecipes: List<Recipe> = emptyList(),
    val selected: Set<String> = emptySet(),
    val servingsTarget: Int = 2,
    val grocery: List<GroceryItem> = emptyList()
)

class PlansViewModel(
    private val repo: InMemoryPlansRepository = InMemoryPlansRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PlansUiState())
    val state: StateFlow<PlansUiState> = _state

    init { _state.update { it.copy(allRecipes = repo.getAllRecipes()) } }

    fun toggleSelect(id: String) {
        _state.update { s ->
            val n = s.selected.toMutableSet().apply { if (contains(id)) remove(id) else add(id) }
            s.copy(selected = n)
        }
    }

    fun changeServings(target: Int) {
        _state.update { it.copy(servingsTarget = target.coerceIn(1, 8)) }
    }

    fun generateGrocery() {
        val s = _state.value
        val chosen = s.allRecipes.filter { it.id in s.selected }
        val list = GroceryCalculator.buildGroceryList(chosen, s.servingsTarget)
        _state.update { it.copy(grocery = list) }
    }
}
