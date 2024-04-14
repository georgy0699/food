package com.example.foodrecipesapp.domain

import com.example.foodrecipesapp.domain.model.User
import com.example.foodrecipesapp.domain.model.UserNew
import com.example.foodrecipesapp.domain.model.Result

interface IUserRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(newUser: UserNew): Result<String>
    suspend fun getUserInfoById(userId: Int): Result<User>
}