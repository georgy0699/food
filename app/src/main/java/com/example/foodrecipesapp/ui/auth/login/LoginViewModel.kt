package com.example.foodrecipesapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.domain.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.ui.Event

class LoginViewModel(private val repository: IUserRepository) : ViewModel() {
    private val _isUserLoggedIn: MutableLiveData<Event<Unit>> = MutableLiveData()
    val isUserLoggedIn: LiveData<Event<Unit>> get() = _isUserLoggedIn
    private val _isLoginError: MutableLiveData<Event<String>> = MutableLiveData()
    val isLoginError: LiveData<Event<String>> get() = _isLoginError

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.login(password = password, email = email)) {
                    is Result.Error -> _isLoginError.postValue(Event(result.message))
                    is Result.Success -> {
                        _isUserLoggedIn.postValue(Event(Unit))
                    }
                }
            } catch (_: Throwable) {
                _isLoginError.postValue(Event("Произошла ошибка"))
            }
        }
    }
}