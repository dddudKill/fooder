package com.example.kotlinTeam.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kotlinTeam.common.data.dataSource.model.recipe.RecipeOo
import com.example.kotlinTeam.feed.domain.useCase.FeedUseCases
import com.example.kotlinTeam.storage.common.StorageStatuses
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val useCases: FeedUseCases
) : ViewModel() {

    private val _feedRecipes: MutableStateFlow<PagingData<RecipeOo>> =
        MutableStateFlow(PagingData.empty())
    val feedRecipes: StateFlow<PagingData<RecipeOo>> = _feedRecipes

    private val _currentRecipeState: MutableStateFlow<CurrentRecipeState> = MutableStateFlow(
        CurrentRecipeState()
    )
    val currentRecipeState: StateFlow<CurrentRecipeState> = _currentRecipeState

    private val _selectedProductsState: MutableStateFlow<SelectedProductsState> = MutableStateFlow(
        SelectedProductsState()
    )
    private val selectedProductsState: StateFlow<SelectedProductsState> = _selectedProductsState

    init {
        viewModelScope.launch {
            useCases.getSelectedProductsUseCase().collect { result ->
                _selectedProductsState.value =
                    when (result) {
                        is StorageStatuses.Success -> {
                            selectedProductsState.value.copy(
                                isLoading = false,
                                selectedProducts = result.data ?: emptyList()
                            )
                        }

                        is StorageStatuses.Error -> {
                            selectedProductsState.value.copy(
                                isLoading = false,
                                selectedProducts = emptyList(),
                                error = result.message
                            )
                        }

                        is StorageStatuses.Loading -> {
                            selectedProductsState.value.copy(
                                isLoading = true,
                                selectedProducts = emptyList()
                            )
                        }
                    }
                getRecipes()
            }
        }
    }

    private fun getRecipes() {
        viewModelScope.launch {
            selectedProductsState.value.let {
                if (!it.isLoading) {
                    try {
                        useCases.getFeedUseCase(selectedProductsState.value.selectedProducts)
                            .cachedIn(viewModelScope).collect { recipes ->
                                _feedRecipes.value = recipes
                            }
                    } catch (e: Exception) {
                        _feedRecipes.value = PagingData.empty()
                    }
                }
            }
        }
    }

    fun setCurrentRecipeById(recipeId: String) {
        viewModelScope.launch {
            val recipe = runBlocking {  useCases.getRecipeByIdUseCase(recipeId) }
            _currentRecipeState.value = currentRecipeState.value.copy(currentRecipe = recipe)
        }
    }

    fun setCurrentRecipe(recipe: RecipeOo?) {
        _currentRecipeState.value = currentRecipeState.value.copy(currentRecipe = recipe)
    }

    fun changeFlag() {
        _currentRecipeState.value =
            currentRecipeState.value.copy(
                isMoreInfoButtonClicked = !currentRecipeState.value.isMoreInfoButtonClicked
            )
    }

    fun resetFlag() {
        _currentRecipeState.value =
            currentRecipeState.value.copy(
                isMoreInfoButtonClicked = false
            )
    }

    fun changeManagerTopPosition(diff: Int) {
        _currentRecipeState.value = currentRecipeState.value.copy(
            topPosition = currentRecipeState.value.topPosition + diff
        )
    }

    override fun onCleared() {
        super.onCleared()
        setCurrentRecipe(null)
    }
}
