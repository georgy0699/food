package com.example.foodrecipesapp.ui.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.ItemProductsBinding
import com.example.foodrecipesapp.domain.model.Product

class ProductsAdapter() :
    ListAdapter<Product, ProductsAdapter.ProductsViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductsViewHolder(
        private val binding: ItemProductsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            Glide
                .with(binding.root)
                .load(product.imgUrl)
                .into(binding.ivImage)
            binding.tvName.text = product.name
            binding.tvWeight.text =
                binding.root.context.getString(R.string.product_weight, product.gram)
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}