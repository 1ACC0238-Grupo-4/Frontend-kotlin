package com.example.workstation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.workstation.R
import com.example.workstation.databinding.FragmentRegisterStep2Binding
import com.example.workstation.ui.viewmodel.RegisterViewModel

class RegisterStep2Fragment : Fragment() {

    private var _binding: FragmentRegisterStep2Binding? = null
    private val binding get() = _binding!!

    private val vm: RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFinish.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val name = arguments?.getString("name").orEmpty()
            val last = arguments?.getString("lastname").orEmpty()

            val r = vm.register(name, last, phone, email, pass)
            if (r.success) {
                Toast.makeText(requireContext(), "Registro OK", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_register2_to_login)
            } else {
                Toast.makeText(requireContext(), r.error ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}