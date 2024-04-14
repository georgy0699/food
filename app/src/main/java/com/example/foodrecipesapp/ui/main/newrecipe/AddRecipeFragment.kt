package com.example.foodrecipesapp.ui.main.newrecipe

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.foodrecipesapp.App
import com.example.foodrecipesapp.databinding.FragmentAddRecipeBinding
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.ui.AddRecipeViewModelFactory

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private val model: AddRecipeViewModel by viewModels { AddRecipeViewModelFactory(App.recipesRepository) }
    private val stepsAdapter = NewStepsAdapter(
        { number, text -> model.updateStepTextByNumber(text, number) },
        { model.removeStepByNumber(it) }
    )

    private val productsAdapter = ProductsAddAdapter(
        { product -> addProduct(product) },
        { weight, id -> changeWeight(id, weight) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initRecyclerViews()
        btClickListeners()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        model.steps.observe(viewLifecycleOwner) {
            stepsAdapter.submitList(it)
        }
        model.products.observe(viewLifecycleOwner) {
            productsAdapter.submitList(it)
        }
        model.categories.observe(viewLifecycleOwner) {
            initSpinners()
        }
        model.isError.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        model.isSuccess.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun initRecyclerViews() {
        binding.rvSteps.adapter = stepsAdapter
        binding.rvSteps.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = productsAdapter
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initSpinners() {
        ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            getCategories()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sCategory.adapter = adapter
        }
    }

    private fun btClickListeners() {
        binding.btAddRecipe.setOnClickListener {
            addNewRecipe()
        }
        binding.ivBackArrow.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.btAddStep.setOnClickListener {
            model.addStep()
            stepsAdapter.notifyDataSetChanged()
        }
    }

    private fun addNewRecipe() {
        val name = binding.etTitle.text.toString()
        val duration = binding.etTime.text.toString() + "мин"
        val category = binding.sCategory.selectedItem.toString()
        val products = model.getCheckedProducts()
        val steps = model.steps.value.orEmpty()
        val imgUrl = binding.etUrl.text.toString()
        val desc = binding.etDescriptionText.text.toString()
        if (name.isBlank() || duration.isBlank() || category.isBlank() || imgUrl.isBlank() || desc.isBlank()) {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_LONG).show()
        } else if (products.size < 2 || steps.size < 2) {
            Toast.makeText(
                requireContext(),
                "Шагов и продуктов должно быть больше 2",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val recipe = Recipe(
                id = 0,
                category = category,
                duration = duration,
                imgUrl = imgUrl,
                description = desc,
                rating = "0",
                userId = 0,
                isFavourite = false,
                title = name
            )
            model.uploadRecipe(recipe, steps, products)
        }
    }

    private fun addProduct(product: ProductUI) {
        if (product.isChecked) {
            model.deleteProduct(product.id)
            binding.rvProducts.post() {
                productsAdapter.notifyDataSetChanged()
            }
        } else {
            model.addProduct(product.id)
            binding.rvProducts.post() {
                productsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun changeWeight(id: Int, weight: Int) {
        if (!binding.rvProducts.isComputingLayout || binding.rvProducts.scrollState == SCROLL_STATE_IDLE) {
            model.changeWeight(id, weight)
            binding.rvProducts.post() {
                productsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getCategories(): List<String> {
        val categories: List<String> =
            model.categories.value!!.flatMap { it.name.split(", ") }.map { it.trim() }
        return categories.distinct()
    }
}