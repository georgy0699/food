package com.example.foodrecipesapp.data

import com.example.foodrecipesapp.data.db.RecipesDatabase
import com.example.foodrecipesapp.data.db.UserData
import com.example.foodrecipesapp.domain.IUserRepository
import com.example.foodrecipesapp.domain.model.Result
import com.example.foodrecipesapp.domain.model.User
import com.example.foodrecipesapp.domain.model.UserNew

class UserRepositoryImpl(private val db: RecipesDatabase, private val userData: UserData) :
    IUserRepository {
    /**
     * Регистрация нового пользователя в приложении.
     *
     * @param user Объект [UserNew], содержащий данные нового пользователя.
     * @return [Result] с сообщением об успешной регистрации или ошибкой.
     */
    override suspend fun register(newUser: UserNew): Result<String> {
        return try {
            val result = db.userDao().register(newUser.toSW())
            Result.Success("Регистрация прошла успешно")
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }

    /**
     * Вход пользователя в приложение.
     *
     * @param email Электронная почта пользователя.
     * @param password Пароль пользователя.
     * @return [Result] с идентификатором пользователя в случае успешного входа или ошибкой.
     */
    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = db.userDao().login(email, password)
            if (result != null) {
                userData.saveUser(result.id.toString(), result.firstName)
                Result.Success("Вход выполнен успешно")
            } else {
                Result.Error("Не верно введены данные!")
            }
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }

    /**
     * Получение информации о текущем пользователе.
     *
     * @return [Result] с объектом [User] в случае успеха или ошибкой.
     */
    override suspend fun getUserInfoById(userId: Int): Result<User> {
        return try {
            if (userId == -1) {
                val result = db.userDao().getUserInfoById(userData.getUserId().toInt())
                if (result != null) {
                    Result.Success(result.toDomain())
                } else {
                    Result.Error("Не удалось получить данные...")
                }
            } else {
                val result = db.userDao().getUserInfoById(userId)
                if (result != null) {
                    Result.Success(result.toDomain())
                } else {
                    Result.Error("Не удалось получить данные...")
                }
            }
        } catch (ex: Exception) {
            Result.Error("Произошла ошибка: $ex")
        }
    }
}