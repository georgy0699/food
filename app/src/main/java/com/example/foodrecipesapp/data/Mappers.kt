package com.example.foodrecipesapp.data

import com.example.foodrecipesapp.data.db.model.CategorySW
import com.example.foodrecipesapp.data.db.model.ProductSW
import com.example.foodrecipesapp.data.db.model.RecipeProductsSW
import com.example.foodrecipesapp.data.db.model.RecipeSW
import com.example.foodrecipesapp.data.db.model.RecipeStepsSW
import com.example.foodrecipesapp.data.db.model.ReviewSW
import com.example.foodrecipesapp.data.db.model.UserSW
import com.example.foodrecipesapp.domain.model.Category
import com.example.foodrecipesapp.domain.model.Product
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.domain.model.Review
import com.example.foodrecipesapp.domain.model.Step
import com.example.foodrecipesapp.domain.model.User
import com.example.foodrecipesapp.domain.model.UserNew

fun UserNew.toSW(): UserSW {
    return UserSW(
        id = this.id,
        firstName = this.firstName,
        email = this.email,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber,
        password = this.password
    )
}

fun UserSW.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber
    )
}

fun RecipeSW.toDomain(rating: Double, category: String, isFavourite: Boolean): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        description = this.description,
        userId = this.userId,
        duration = this.duration,
        imgUrl = this.imgUrl,
        rating = rating.toString(),
        category = category,
        isFavourite = isFavourite
    )
}

fun ProductSW.toDomain(gram: Int): Product {
    return Product(
        id = id,
        name = name,
        calories = calories,
        proteins = proteins,
        fats = fats,
        carbohydrates = carbohydrates,
        imgUrl = imgUrl,
        gram = gram
    )
}

fun RecipeStepsSW.toDomain(): Step {
    return Step(
        stepNumber = this.stepNumber,
        description = this.description,
    )
}
fun Review.toSW(recipeId: Int, userId: Int): ReviewSW {
    return ReviewSW(
        id = 0,
        userId = userId,
        recipeId = recipeId,
        rating = this.rating,
        comment = this.comment
    )
}
fun ReviewSW.toDomain(userName: String, userLastname: String): Review {
    return Review(
        userId = 0,
        rating = this.rating,
        comment = this.comment,
        userName =  userName,
        userLastname = userLastname
    )
}

fun CategorySW.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name
    )
}

fun Recipe.toSW(userId: Int, categoryId: Int): RecipeSW {
    return RecipeSW(
        id = 0,
        userId = userId,
        imgUrl = this.imgUrl,
        title = this.title,
        categoryId = categoryId,
        description = this.description,
        duration = this.duration
    )
}

fun Step.toSW(recipeId: Int): RecipeStepsSW {
    return RecipeStepsSW(
        id = 0,
        recipeId = recipeId,
        stepNumber = this.stepNumber,
        description = this.description
    )
}

fun Product.toSW(recipeId: Int): RecipeProductsSW {
    return RecipeProductsSW(
        id = 0,
        productId = this.id,
        gram = this.gram,
        recipeId = recipeId
    )
}