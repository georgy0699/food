package com.example.foodrecipesapp.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foodrecipesapp.App
import com.example.foodrecipesapp.databinding.FragmentRegisterBinding
import com.example.foodrecipesapp.domain.model.UserNew
import com.example.foodrecipesapp.ui.RegisterViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val model: RegisterViewModel by viewModels { RegisterViewModelFactory(App.userRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRestrictions()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRestrictions() {
        binding.btRegister.setOnClickListener {
            if (binding.etName.text.isBlank() || binding.etLastname.text.isBlank() || binding.etEmail.text.isBlank() || binding.etPhone.text.isBlank() || binding.etPassword.text.isBlank()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else if (binding.etRepeatPassword.text.toString() != binding.etPassword.text.toString()) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            } else {
                register()
                binding.btRegister.isEnabled = false
            }
        }
    }

    private fun observeViewModel() {
        model.isUserRegistered.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }
        model.isError.observe(viewLifecycleOwner) { message ->
            message.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val lastname = binding.etLastname.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etRepeatPassword.text.toString()

        val user = UserNew(0, email, name, lastname, phone, password)
        model.register(user)
    }
}