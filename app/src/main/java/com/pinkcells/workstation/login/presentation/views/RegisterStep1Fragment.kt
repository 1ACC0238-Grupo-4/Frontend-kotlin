package com.example.workstation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.workstation.R
import com.example.workstation.databinding.FragmentRegisterStep1Binding

class RegisterStep1Fragment : Fragment() {

    private var _binding: FragmentRegisterStep1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val name = binding.etName.text.toString()
            val last = binding.etLastname.text.toString()

            val action = R.id.action_register1_to_register2
            val args = Bundle().apply {
                putString("name", name)
                putString("lastname", last)
            }
            findNavController().navigate(action, args)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}