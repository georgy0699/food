package com.example.foodrecipesapp.data.db.model

import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_steps",
    foreignKeys = [
        ForeignKey(
            entity = RecipeSW::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecipeStepsSW(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
    @ColumnInfo(name = "step_number")
    val stepNumber: Int,
    @ColumnInfo(name = "description")
    val description: String
)
