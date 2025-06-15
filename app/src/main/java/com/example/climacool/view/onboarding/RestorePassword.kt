package com.example.climacool.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.climacool.R
import com.example.climacool.databinding.FragmentRestorePasswordBinding

class RestorePassword : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentRestorePasswordBinding? = null
    private val binding get() = _binding!!
    var isValid: Boolean = false
    private lateinit var communicator: _root_ide_package_.com.example.climacool.utils.FragmentComunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {

        binding.imageButton.setOnClickListener {
            findNavController().navigate(R.id.action_restorePassword_to_layout_login)
        }

        binding.etCorreo.addTextChangedListener {
            if (binding.etCorreo.text.toString().isEmpty()) {
                binding.tilCorreo.error = "Por favor introduce un correo"
                isValid = false
            } else {
                isValid = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}