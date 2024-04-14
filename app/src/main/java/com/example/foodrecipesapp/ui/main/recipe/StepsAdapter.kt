package com.example.foodrecipesapp.ui.main.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.ItemStepBinding
import com.example.foodrecipesapp.domain.model.Step


class StepsAdapter() :
    ListAdapter<Step, StepsAdapter.StepViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding =
            ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = getItem(position)
        holder.bind(step)
    }

    inner class StepViewHolder(
        private val binding: ItemStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step: Step) {
            binding.tvStep.text =
                binding.root.context.getString(R.string.step_number, step.stepNumber)
            binding.tvStepText.text = step.description
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(
            oldItem: Step,
            newItem: Step
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: Step,
            newItem: Step
        ): Boolean {
            return oldItem == newItem
        }
    }
}