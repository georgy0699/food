package com.example.foodrecipesapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.domain.IUserRepository
import com.example.foodrecipesapp.domain.model.UserNew
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: IUserRepository) : ViewModel() {
    private val _isUserRegistered: MutableLiveData<Event<String>> = MutableLiveData()
    val isUserRegistered: LiveData<Event<String>> get() = _isUserRegistered
    private val _isError: MutableLiveData<Event<String>> = MutableLiveData()
    val isError: LiveData<Event<String>> get() = _isError

    fun register(newUser: UserNew) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.register(newUser)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isUserRegistered.postValue(Event(result.data))
                    }
                }
            } catch (_: Throwable) {
            }
        }
    }
}