package com.example.foodrecipesapp.ui.main.newrecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.domain.IRecipesRepository
import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.domain.model.Step
import com.example.foodrecipesapp.ui.Event
import com.example.foodrecipesapp.ui.toDomain
import com.example.foodrecipesapp.ui.toUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddRecipeViewModel(private val repository: IRecipesRepository) : ViewModel() {
    private val _steps: MutableLiveData<List<Step>> = MutableLiveData()
    val steps: LiveData<List<Step>> get() = _steps
    private val _products: MutableLiveData<List<ProductUI>> = MutableLiveData()
    val products: LiveData<List<ProductUI>> get() = _products
    private val _categories: MutableLiveData<List<Category>> = MutableLiveData()
    val categories: LiveData<List<Category>> get() = _categories
    private val _isError: MutableLiveData<Event<String>> = MutableLiveData()
    val isError: LiveData<Event<String>> get() = _isError
    private val _isSuccess: MutableLiveData<Event<String>> = MutableLiveData()
    val isSuccess: LiveData<Event<String>> get() = _isSuccess

    init {
        loadProducts()
        loadCategory()
    }

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.getProducts()) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _products.postValue(result.data!!.map { it.toUI() })
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }

    private fun loadCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.getAllCategories()) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _categories.postValue(result.data.orEmpty())
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }

    fun addProduct(productId: Int) {
        val products = _products.value!!
        products.find { it.id == productId }!!.isChecked = true
        _products.postValue(products)
        Log.i("product", "добавился $productId")
    }

    fun deleteProduct(productId: Int) {
        val products = _products.value!!
        products.find { it.id == productId }!!.isChecked = false
        _products.postValue(products)
        Log.i("product", "удалился $productId")
    }

    fun changeWeight(productId: Int, weight: Int) {
        val products = _products.value!!
        products.find { it.id == productId }!!.gram = weight
        _products.postValue(products)
        Log.i("weight", "Изменился $productId")
    }

    fun getCheckedProducts(): List<ProductUI> {
        val products = _products.value.orEmpty()
        return products.filter { it.isChecked }
    }

    fun removeStepByNumber(stepNumber: Int) {
        val currentSteps = _steps.value.orEmpty().toMutableList()
        val removedStep = currentSteps.find { it.stepNumber == stepNumber }
        removedStep?.let {
            currentSteps.remove(it)
            _steps.value = currentSteps
        }
    }

    fun addStep() {
        val currentSteps = _steps.value.orEmpty().toMutableList()
        currentSteps.add(
            Step(
                stepNumber = currentSteps.size + 1,
                description = ""
            )
        )
        _steps.value = currentSteps
    }

    fun updateStepTextByNumber(stepNumber: Int, newText: String) {
        val currentSteps = _steps.value.orEmpty().toMutableList()
        val updatedStep = currentSteps.find { it.stepNumber == stepNumber }
        updatedStep?.let {
            it.description = newText
            _steps.value = currentSteps
        }
    }

    fun uploadRecipe(recipe: Recipe, step: List<Step>, products: List<ProductUI>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result =
                    repository.uploadRecipe(recipe, step, products.map { it.toDomain() })) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isSuccess.postValue(Event(result.data))
                    }
                }
            } catch (ex: Throwable) {
                _isError.postValue(Event("Произошла ошибка: ${ex.message}"))
            }
        }
    }

}