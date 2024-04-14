package com.example.foodrecipesapp.ui.main.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.domain.IRecipesRepository
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repository: IRecipesRepository) : ViewModel() {
    private val _recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val recipes: LiveData<List<Recipe>> get() = _recipes
    private val _isError: MutableLiveData<Event<String>> = MutableLiveData()
    val isError: LiveData<Event<String>> get() = _isError
    private val _isFavSuccess: MutableLiveData<Event<String>> = MutableLiveData()
    val isFavSuccess: LiveData<Event<String>> get() = _isFavSuccess
    private val _isRemoveFavSuccess: MutableLiveData<Event<String>> = MutableLiveData()
    val isRemoveFavSuccess: LiveData<Event<String>> get() = _isRemoveFavSuccess

    var allRecipes: List<Recipe> = emptyList()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.getUserFavouriteRecipes()) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _recipes.postValue(result.data!!)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }

    fun addToFav(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.addToFavourite(recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isFavSuccess.postValue(Event(result.data))
                        val mov = _recipes.value!!
                        mov.find { it.id == recipeId }!!.isFavourite = true
                        _recipes.postValue(mov)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Ошибка добавления в избранное"))
            }
        }
    }

    fun removeFromFav(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.removeFromFavourite(recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isRemoveFavSuccess.postValue(Event(result.data))
                        var mov = _recipes.value.orEmpty()
                        mov = mov - mov.find { it.id == recipeId }!!
                        _recipes.postValue(mov)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Ошибка добавления в избранное"))
            }
        }
    }

    fun searchRecipes(query: String) {
        val filteredRecipes = if (query.isBlank()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                recipe.title.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
        _recipes.value = filteredRecipes
    }
}