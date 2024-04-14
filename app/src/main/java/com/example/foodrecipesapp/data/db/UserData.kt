package com.example.foodrecipesapp.data.db

import android.content.SharedPreferences

/**
 * Класс для управления данными пользователя, сохраненными в SharedPreferences.
 *
 * @param sharedPreferences Объект [SharedPreferences] для работы с хранилищем данных.
 */
class UserData(private val sharedPreferences: SharedPreferences) {

    /**
     * Сохранение идентификатора пользователя в хранилище данных.
     *
     * @param id Идентификатор пользователя.
     */
    fun saveUser(id: String, name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_id", id)
        editor.putString("user_name", name)
        editor.apply()
    }

    /**
     * Получение идентификатора пользователя из хранилища данных.
     *
     * @return Идентификатор пользователя в виде строки.
     */
    fun getUserId(): String {
        return sharedPreferences.getString("user_id", null)!!
    }

    fun getUserName(): String {
        return sharedPreferences.getString("user_name", null)!!
    }
    /**
     * Очистка данных пользователя в хранилище.
     */
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("user_id")
        editor.remove("user_name")
        editor.apply()
    }
}
