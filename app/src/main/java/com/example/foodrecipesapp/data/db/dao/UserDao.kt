package com.example.foodrecipesapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodrecipesapp.data.db.model.UserSW

@Dao
interface UserDao {
    @Insert
    suspend fun register(user: UserSW)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): UserSW?

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserInfoById(userId: Int): UserSW?
}