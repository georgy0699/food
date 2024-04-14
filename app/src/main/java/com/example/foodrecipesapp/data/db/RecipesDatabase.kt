package com.example.foodrecipesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodrecipesapp.data.db.dao.RecipesDao
import com.example.foodrecipesapp.data.db.dao.UserDao
import com.example.foodrecipesapp.data.db.model.CategorySW
import com.example.foodrecipesapp.data.db.model.FavouriteSW
import com.example.foodrecipesapp.data.db.model.ProductSW
import com.example.foodrecipesapp.data.db.model.RecipeProductsSW
import com.example.foodrecipesapp.data.db.model.RecipeSW
import com.example.foodrecipesapp.data.db.model.RecipeStepsSW
import com.example.foodrecipesapp.data.db.model.ReviewSW
import com.example.foodrecipesapp.data.db.model.UserSW

@Database(
    version = 2,
    entities = [
        UserSW::class,
        CategorySW::class,
        ProductSW::class,
        RecipeSW::class,
        FavouriteSW::class,
        RecipeProductsSW::class,
        RecipeStepsSW::class,
        ReviewSW::class,
    ]
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
    abstract fun userDao(): UserDao
}