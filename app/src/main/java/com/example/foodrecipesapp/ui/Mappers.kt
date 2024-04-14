package com.example.foodrecipesapp.ui

import com.example.foodrecipesapp.domain.model.Product
import com.example.foodrecipesapp.ui.main.newrecipe.ProductUI

fun Product.toUI(): ProductUI {
    return ProductUI(
        id = this.id,
        imgUrl = this.imgUrl,
        name = this.name,
        gram = this.gram,
        isChecked = false
    )
}

fun ProductUI.toDomain(): Product {
    return Product(
        id = this.id,
        imgUrl = this.imgUrl,
        name = this.name,
        gram = this.gram,
        calories = 0,
        fats = 0,
        carbohydrates = 0,
        proteins = 0,
    )
}