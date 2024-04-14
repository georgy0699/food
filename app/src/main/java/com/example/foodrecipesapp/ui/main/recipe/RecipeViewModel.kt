package com.example.foodrecipesapp.ui.main.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.domain.IRecipesRepository
import com.example.foodrecipesapp.domain.IUserRepository
import com.example.foodrecipesapp.domain.model.Product
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.domain.model.Review
import com.example.foodrecipesapp.domain.model.Step
import com.example.foodrecipesapp.domain.model.User
import com.example.foodrecipesapp.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(private val repositoryRecipes: IRecipesRepository, private val repositoryUser: IUserRepository) : ViewModel() {
    private val _recipe: MutableLiveData<Recipe> = MutableLiveData()
    val recipe: LiveData<Recipe> get() = _recipe
    private val _author: MutableLiveData<User> = MutableLiveData()
    val author: LiveData<User> get() = _author
    private val _reviews: MutableLiveData<List<Review>> = MutableLiveData()
    val reviews: LiveData<List<Review>> get() = _reviews
    private val _products: MutableLiveData<List<Product>> = MutableLiveData()
    val products: LiveData<List<Product>> get() = _products
    private val _steps: MutableLiveData<List<Step>> = MutableLiveData()
    val steps: LiveData<List<Step>> get() = _steps
    private val _isError: MutableLiveData<Event<String>> = MutableLiveData()
    val isError: LiveData<Event<String>> get() = _isError
    private val _isReviewAdded: MutableLiveData<Event<String>> = MutableLiveData()
    val isReviewAdded: LiveData<Event<String>> get() = _isReviewAdded
    private val _isFavSuccess: MutableLiveData<Event<String>> = MutableLiveData()
    val isFavSuccess: LiveData<Event<String>> get() = _isFavSuccess
    private val _isRemoveFavSuccess: MutableLiveData<Event<String>> = MutableLiveData()
    val isRemoveFavSuccess: LiveData<Event<String>> get() = _isRemoveFavSuccess

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.getRecipeById(recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _recipe.postValue(result.data!!)
                        loadAuthor(result.data.userId)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }
    fun loadProducts(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.getRecipeProducts(recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _products.postValue(result.data!!)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }
    fun loadSteps(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.getRecipeSteps(recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _steps.postValue(result.data!!)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }
    private fun loadAuthor(authorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryUser.getUserInfoById(authorId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _author.postValue(result.data!!)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }
    fun loadReviews(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.getRecipeReviews(recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _reviews.postValue(result.data!!)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }

    fun sendReview(review: Review, recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.addReview(review, recipeId)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isReviewAdded.postValue(Event(result.data))
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }

    fun addToFav() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.addToFavourite(recipe.value!!.id)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isFavSuccess.postValue(Event(result.data))
                        val mov = _recipe.value!!
                        mov.isFavourite = true
                        _recipe.postValue(mov)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Ошибка добавления в избранное"))
            }
        }
    }

    fun removeFromFav() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repositoryRecipes.removeFromFavourite(recipe.value!!.id)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isRemoveFavSuccess.postValue(Event(result.data))
                        val mov = _recipe.value!!
                        mov.isFavourite = false
                        _recipe.postValue(mov)
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Ошибка добавления в избранное"))
            }
        }
    }
}