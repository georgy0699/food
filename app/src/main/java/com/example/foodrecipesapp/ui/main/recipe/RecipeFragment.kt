package com.example.foodrecipesapp.ui.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodrecipesapp.App
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.FragmentRecipeBinding
import com.example.foodrecipesapp.domain.model.Review
import com.example.foodrecipesapp.ui.RecipeViewModelFactory

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val adapterSteps = StepsAdapter()
    private val adapterProducts = ProductsAdapter()
    private val adapterReviews = ReviewsAdapter()
    private val model: RecipeViewModel by viewModels {
        RecipeViewModelFactory(
            App.recipesRepository,
            App.userRepository
        )
    }
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        model.loadRecipe(args.recipeId)
        model.loadProducts(args.recipeId)
        model.loadSteps(args.recipeId)
        model.loadReviews(args.recipeId)
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initRecyclerViews()
        initSpinner()
        addReviewListener()
        backButtonListener()
        sendReviewListener()
        binding.ivFav.setOnClickListener {
            addToFavourite()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun addReviewListener() {
        binding.btAddReview.setOnClickListener {
            it.visibility = View.GONE
            binding.clAddReview.visibility = View.VISIBLE
        }
    }

    private fun sendReviewListener() {
        binding.btSendReview.setOnClickListener {
            val rating = binding.sRate.selectedItem.toString().toInt()
            val reviewText = binding.etReviewText.text.toString()
            if (reviewText.isNotBlank()) {
                val review = Review(
                    userId = 0,
                    userName = "",
                    userLastname = "",
                    rating = rating,
                    comment = reviewText
                )
                model.sendReview(review, args.recipeId)
                binding.clAddReview.visibility = View.GONE
                binding.btAddReview.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Заполните поля!", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun checkFav() {
        if (model.recipe.value!!.isFavourite) {
            binding.ivFav.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.ivFav.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun backButtonListener() {
        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.marks_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sRate.adapter = adapter
        }
    }

    private fun observeViewModel() {
        model.recipe.observe(viewLifecycleOwner) {
            setData()
            checkFav()
        }
        model.products.observe(viewLifecycleOwner) { products ->
            adapterProducts.submitList(products)
            var totalCal = 0.0
            model.products.value!!.forEach {
                totalCal += (it.calories * it.gram) / 100.0
            }
            binding.tvCalories.text = totalCal.toInt().toString()
        }
        model.steps.observe(viewLifecycleOwner) { steps ->
            adapterSteps.submitList(steps)
        }
        model.isError.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        model.author.observe(viewLifecycleOwner) {
            binding.tvAuthor.text =
                binding.root.context.getString(R.string.from_author, it.firstName, it.lastName)
        }
        model.reviews.observe(viewLifecycleOwner) { reviews ->
            adapterReviews.submitList(reviews)
        }
        model.isReviewAdded.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                model.loadReviews(args.recipeId)
                adapterReviews.notifyDataSetChanged()
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

    private fun initRecyclerViews() {
        binding.rvSteps.adapter = adapterSteps
        binding.rvSteps.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapterProducts
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReviews.adapter = adapterReviews
        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setData() {
        binding.tvName.text = model.recipe.value!!.title
        binding.tvDuration.text = model.recipe.value!!.duration
        binding.tvCategory.text = model.recipe.value!!.category
        binding.tvDescriptionText.text = model.recipe.value!!.description
        binding.tvRating.text = model.recipe.value!!.rating
        Glide
            .with(binding.root)
            .load(model.recipe.value!!.imgUrl)
            .into(binding.ivDish)
    }

    private fun addToFavourite() {
        if (model.recipe.value!!.isFavourite) {
            model.removeFromFav()
            checkFav()
        } else {
            model.addToFav()
            checkFav()
        }
    }
}