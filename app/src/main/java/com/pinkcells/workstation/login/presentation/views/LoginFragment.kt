package com.pinkcells.workstation.login.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pinkcells.workstation.R
import com.pinkcells.workstation.databinding.FragmentLoginBinding
import com.example.workstation.ui.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val vm: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val result = vm.login(email, pass)
            if (result.success) {
                Toast.makeText(requireContext(), "Bienvenido ${'$'}{result.userName}", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), result.error ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvForgot.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot)
        }

        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register1)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}