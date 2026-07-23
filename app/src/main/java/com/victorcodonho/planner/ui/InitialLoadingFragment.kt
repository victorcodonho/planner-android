package com.victorcodonho.planner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.victorcodonho.planner.R
import com.victorcodonho.planner.databinding.FragmentInitialLoadingBinding
import com.victorcodonho.planner.ui.viewmodel.UserRegistrationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitialLoadingFragment : Fragment() {

    private var _binding: FragmentInitialLoadingBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    private val userRegistrationViewModel by activityViewModels<UserRegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitialLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(1_500)
            navController.navigate(
                if (userRegistrationViewModel.getIsUserRegistered())
                    R.id.action_initialLoadingFragment_to_homeFragment
                else
                    R.id.action_initialLoadingFragment_to_userRegistrationFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}