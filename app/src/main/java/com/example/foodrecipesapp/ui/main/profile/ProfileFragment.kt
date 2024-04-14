package com.example.foodrecipesapp.ui.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodrecipesapp.App
import com.example.foodrecipesapp.databinding.FragmentProfileBinding
import com.example.foodrecipesapp.ui.ProfileViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var logoutListener: OnLogoutListener? = null
    private val model: ProfileViewModel by viewModels { ProfileViewModelFactory(App.userRepository) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLogoutListener) {
            logoutListener = context
        } else {
            throw RuntimeException("$context must implement OnLogoutListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.btLogout.setOnClickListener {
            logoutListener?.logoutClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        model.userInfo.observe(viewLifecycleOwner) { user ->
            initTextViews()
        }
        model.isError.observe(viewLifecycleOwner) { result ->
            result.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun initTextViews() {
        binding.tvName.text = model.userInfo.value?.firstName
        binding.tvLastname.text = model.userInfo.value?.lastName
        binding.tvPhone.text = model.userInfo.value?.phoneNumber
        binding.tvEmail.text = model.userInfo.value?.email
    }
}

interface OnLogoutListener {
    fun logoutClicked()
}