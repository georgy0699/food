package com.example.foodrecipesapp.ui.main.newrecipe

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.ItemStepAddBinding
import com.example.foodrecipesapp.domain.model.Step

class NewStepsAdapter(
    private val onTextChanged: (String, Int) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
) :
    ListAdapter<Step, NewStepsAdapter.StepsAddViewHolder>(
        NewsDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsAddViewHolder {
        val binding =
            ItemStepAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepsAddViewHolder(binding, onTextChanged, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: StepsAddViewHolder, position: Int) {
        val step = getItem(position)
        holder.bind(step, position)
    }

    inner class StepsAddViewHolder(
        private val binding: ItemStepAddBinding,
        private val onTextChanged: (String, Int) -> Unit,
        private val onDeleteClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(step: Step, position: Int) {
            when (position) {
                itemCount - 1 -> binding.ivDelete.visibility = View.VISIBLE
                else -> binding.ivDelete.visibility = View.GONE
            }
            binding.tvStep.text =
                binding.root.context.getString(R.string.step_number, step.stepNumber)
            binding.etStepText.doAfterTextChanged {
                onTextChanged(it.toString(), step.stepNumber)
                Log.d("123", it.toString())
            }
            binding.ivDelete.setOnClickListener {
                onDeleteClicked(step.stepNumber)
            }
        }
    }

    private class NewsDiffCallback :
        DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(
            oldItem: Step,
            newItem: Step
        ): Boolean {
            return oldItem.stepNumber == newItem.stepNumber
        }

        override fun areContentsTheSame(
            oldItem: Step,
            newItem: Step
        ): Boolean {
            return oldItem == newItem
        }
    }
}