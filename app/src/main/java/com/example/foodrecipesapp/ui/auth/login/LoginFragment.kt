package com.example.foodrecipesapp.ui.auth.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodrecipesapp.App
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.FragmentLoginBinding
import com.example.foodrecipesapp.ui.LoginViewModelFactory

/**
 * Фрагмент для отображения экрана входа в приложение.
 */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var loginSuccessListener: OnLoginSuccessListener? = null
    private val model: LoginViewModel by viewModels { LoginViewModelFactory(App.userRepository) }

    /**
     * Вызывается при присоединении фрагмента к активности.
     * Проверяет, реализует ли активность интерфейс OnLoginSuccessListener.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginSuccessListener) {
            loginSuccessListener = context
        } else {
            throw RuntimeException("$context must implement OnLoginSuccessListener")
        }
    }

    /**
     * Создает и возвращает представление фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Вызывается после завершения создания представления фрагмента.
     * Настроивает слушателей и обработчики событий.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btSignUp.setOnClickListener {
            navigateToRegistration()
        }
        binding.btSignIn.setOnClickListener {
            model.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
        observeLoginError()
        observeUserLoggedIn()
    }

    /**
     * Вызывается при уничтожении представления фрагмента.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Наблюдение за состоянием входа пользователя.
     * При успешном входе вызывает метод onUserLoginSuccess у слушателя.
     */
    private fun observeUserLoggedIn() {
        model.isUserLoggedIn.observe(viewLifecycleOwner) {
            loginSuccessListener?.onUserLoginSuccess()
        }
    }

    /**
     * Наблюдение за ошибкой входа.
     * При появлении ошибки выводит соответствующее сообщение через Toast.
     */
    private fun observeLoginError() {
        model.isLoginError.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Навигация к экрану регистрации.
     */
    private fun navigateToRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }
}

/**
 * Интерфейс для обработки успешного входа пользователя.
 */
interface OnLoginSuccessListener {
    fun onUserLoginSuccess()
}