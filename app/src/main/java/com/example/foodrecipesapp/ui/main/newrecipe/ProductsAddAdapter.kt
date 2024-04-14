package com.example.foodrecipesapp.ui.main.newrecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipesapp.databinding.ItemProductAddBinding

class ProductsAddAdapter(
    private val onCheckChanged: (ProductUI) -> Unit,
    private val onWeightChanged: (Int, Int) -> Unit
) :
    ListAdapter<ProductUI, ProductsAddAdapter.ProductsViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding =
            ItemProductAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding, onCheckChanged, onWeightChanged)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = getItem(position)
        holder.setIsRecyclable(false)
        holder.bind(product)
    }

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ProductsViewHolder(
        private val binding: ItemProductAddBinding,
        private val onCheckChanged: (ProductUI) -> Unit,
        private val onWeightChanged: (Int, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) {
            Glide
                .with(binding.root)
                .load(product.imgUrl)
                .into(binding.ivImage)
            binding.tvName.text = product.name
            binding.etWeight.setOnClickListener(null)
            binding.cbCheck.setOnCheckedChangeListener(null)
            binding.etWeight.setText(product.gram.toString())
            binding.cbCheck.isChecked = product.isChecked
            binding.etWeight.isEnabled = product.isChecked
            binding.cbCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckChanged(
                    product
                )
            }

            if (binding.cbCheck.isChecked) {
                binding.etWeight.doAfterTextChanged {
                    if (it.isNullOrBlank()) {
                        onWeightChanged(0, product.id)
                    } else {
                        onWeightChanged(
                            it.toString().toInt(),
                            product.id,
                        )
                    }
                }
            }
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(
            oldItem: ProductUI,
            newItem: ProductUI
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductUI,
            newItem: ProductUI
        ): Boolean {
            return oldItem == newItem
        }
    }
}