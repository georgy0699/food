package com.example.foodrecipesapp.ui.main.favourite

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
import com.example.foodrecipesapp.databinding.FragmentFavouriteBinding
import com.example.foodrecipesapp.domain.model.Recipe
import com.example.foodrecipesapp.ui.FavouriteViewModelFactory
import com.example.foodrecipesapp.ui.main.home.RecipesAdapter

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val adapter = RecipesAdapter(
        { navigateToRecipeFragment(it) },
        { recipe -> addToFavourite(recipe) }
    )

    private val model: FavouriteViewModel by viewModels { FavouriteViewModelFactory(App.recipesRepository) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        model.recipes.observe(viewLifecycleOwner) { recipe ->
            if (recipe.isEmpty()) {
                adapter.submitList(emptyList())
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                adapter.submitList(recipe)
                binding.tvEmpty.visibility = View.GONE
            }
        }
        model.isError.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
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

    private fun navigateToRecipeFragment(recipeId: Int) {
        val action =
            FavouriteFragmentDirections.actionNavigationFavouriteToNavigationRecipe(recipeId)
        findNavController().navigate(action)
    }

    private fun addToFavourite(recipe: Recipe) {
        if (recipe.isFavourite) {
            model.removeFromFav(recipe.id)
            adapter.notifyDataSetChanged()
        } else {
            model.addToFav(recipe.id)
            adapter.notifyDataSetChanged()
        }
    }
}