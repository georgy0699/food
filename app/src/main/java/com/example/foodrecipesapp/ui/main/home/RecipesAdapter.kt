package com.example.foodrecipesapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.ItemRecipeBinding
import com.example.foodrecipesapp.domain.model.Recipe

class RecipesAdapter(private val onClick: (Int) -> Unit, private val onFavClick: (Recipe) -> Unit) :
    ListAdapter<Recipe, RecipesAdapter.RecipesViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(binding, onClick, onFavClick)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipesViewHolder(
        private val binding: ItemRecipeBinding,
        private val onClick: (Int) -> Unit,
        private val onFavClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            Glide
                .with(binding.root)
                .load(recipe.imgUrl)
                .into(binding.ivImage)
            if (recipe.isFavourite) {
                binding.btToFavourite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.btToFavourite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            binding.tvTitle.text = recipe.title
            binding.tvInfo.text = binding.root.context.getString(
                R.string.recipe_details,
                recipe.category,
                recipe.duration
            )
            binding.root.setOnClickListener {
                onClick(recipe.id)
            }
            binding.btToFavourite.setOnClickListener {
                onFavClick(recipe)
            }
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Recipe,
            newItem: Recipe
        ): Boolean {
            return oldItem == newItem
        }
    }
}