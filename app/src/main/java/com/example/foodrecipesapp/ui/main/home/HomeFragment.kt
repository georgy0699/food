package com.example.foodrecipesapp.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodrecipesapp.App
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.FragmentHomeBinding
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.ui.HomeViewModelFactory
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter = RecipesAdapter (
        {navigateToRecipeFragment(it)},
        {recipe -> addToFavourite(recipe)}
    )

    private val model: HomeViewModel by viewModels{ HomeViewModelFactory(App.recipesRepository) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.loadRecipes()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initRecyclerView()
        searchViewListener()
        binding.tvHelloFirstName.text = binding.root.context.getString(R.string.welcome, App.userManger.getUserName())
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_add_recipe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        model.recipes.observe(viewLifecycleOwner) { recipe ->
            if (recipe.isEmpty()) {
                adapter.submitList(emptyList())
            } else {
                adapter.submitList(recipe)

            }
        }
        model.isError.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        model.isSuccess.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                initChipGroup()
                setupChipGroupListener()
            }
        }
        model.isFavSuccess.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        model.isRemoveFavSuccess.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvRecipes.adapter = adapter
        binding.rvRecipes.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun searchViewListener() {
        binding.searchView.isSubmitButtonEnabled = true
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                model.searchRecipes(newText)
                return true
            }
        })
    }

    private fun initChipGroup() {
        binding.chipGroupView.isHorizontalScrollBarEnabled = false
        binding.chipGroup.removeAllViews()
        val categories = getCategories()
        for (category in categories) {
            val chip = Chip(binding.chipGroup.context)
            chip.text = category
            chip.isClickable = true
            chip.isCheckable = true
            binding.chipGroup.addView(chip)
        }
    }

    private fun setupChipGroupListener() {
        binding.chipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
            if (checkedId == View.NO_ID) {
                showAllElements()
            } else {
                val selectedChip = chipGroup.findViewById<Chip>(checkedId)
                model.filterRecipesByCategory(selectedChip.text.toString())
            }
        }
    }

    private fun observeNoChipSelected() {
        model.filterRecipesByCategory(null)
    }

    private fun navigateToRecipeFragment(recipeId: Int){
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationRecipe(recipeId)
        findNavController().navigate(action)
    }

    private fun showAllElements() {
        binding.chipGroup.clearCheck()
        observeNoChipSelected()
    }

    private fun getCategories(): List<String> {
        val genresList: List<String> = model.allRecipes.flatMap { it.category.split(", ") }.map { it.trim() }
        return genresList.distinct()
    }

    private fun addToFavourite(recipe: Recipe) {
        if (recipe.isFavourite){
            model.removeFromFav(recipe.id)
            adapter.notifyDataSetChanged()
        } else {
            model.addToFav(recipe.id)
            adapter.notifyDataSetChanged()
        }
    }
}