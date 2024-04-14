package com.example.foodrecipesapp.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipesapp.domain.IUserRepository
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.domain.model.User
import com.example.foodrecipesapp.ui.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: IUserRepository) : ViewModel() {

    private val _userInfo: MutableLiveData<User> = MutableLiveData()
    val userInfo: LiveData<User> get() = _userInfo
    private val _isError: MutableLiveData<Event<String>> = MutableLiveData()
    val isError: LiveData<Event<String>> get() = _isError

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = repository.getUserInfoById(-1)) {
                    is Result.Error -> _isError.postValue(Event(result.message))
                    is Result.Success -> _userInfo.postValue(result.data!!)
                }
            } catch (_: Throwable) {
            }
        }
    }
}