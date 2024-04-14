package com.example.foodrecipesapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodrecipesapp.domain.IRecipesRepository
import com.example.foodrecipesapp.domain.IUserRepository
import com.example.foodrecipesapp.ui.auth.login.LoginViewModel
import com.example.foodrecipesapp.ui.auth.register.RegisterViewModel
import com.example.foodrecipesapp.ui.main.favourite.FavouriteViewModel
import com.example.foodrecipesapp.ui.main.home.HomeViewModel
import com.example.foodrecipesapp.ui.main.newrecipe.AddRecipeViewModel
import com.example.foodrecipesapp.ui.main.profile.ProfileViewModel
import com.example.foodrecipesapp.ui.main.recipe.RecipeViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val userRepository: IUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = LoginViewModel(userRepository)
        return viewModel as T
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val recipesRepository: IRecipesRepository
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = HomeViewModel(recipesRepository)
        return viewModel as T
    }
}

/*@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val movieRepository: AppRepository
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = ProfileViewModel(movieRepository)
        return viewModel as T
    }
}*/

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(
    private val userRepository: IUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = RegisterViewModel(userRepository)
        return viewModel as T
    }
}

@Suppress("UNCHECKED_CAST")
class RecipeViewModelFactory(
    private val recipesRepository: IRecipesRepository,
    private val userRepository: IUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = RecipeViewModel(recipesRepository,userRepository)
        return viewModel as T
    }
}

@Suppress("UNCHECKED_CAST")
class AddRecipeViewModelFactory(
    private val recipesRepository: IRecipesRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = AddRecipeViewModel(recipesRepository)
        return viewModel as T
    }
}

@Suppress("UNCHECKED_CAST")
class FavouriteViewModelFactory(
    private val recipesRepository: IRecipesRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = FavouriteViewModel(recipesRepository)
        return viewModel as T
    }
}
@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val userRepository: IUserRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = ProfileViewModel(userRepository)
        return viewModel as T
    }
}