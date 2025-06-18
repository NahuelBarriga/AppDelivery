package com.deliveryapp.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.deliveryapp.databinding.DialogLoginBinding
import com.deliveryapp.data.api.RetrofitClient
import com.deliveryapp.repository.UsuarioRepository
import com.deliveryapp.viewmodel.LoginResult
import com.deliveryapp.viewmodel.LoginViewModel
import com.deliveryapp.viewmodel.LoginViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginDialogFragment : DialogFragment() {

    private var _binding: DialogLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    interface LoginDialogListener {
        fun onLoginSuccess()
        fun onLoginCancelled()
    }

    private var listener: LoginDialogListener? = null

    @OptIn(UnstableApi::class)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogLoginBinding.inflate(LayoutInflater.from(context))
        isCancelable = false

        // Inicialización CORRECTA del ViewModel con factory
        val repository = UsuarioRepository(RetrofitClient.apiService)
        val factory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        setupObservers()
        setupListeners()

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    fun setLoginListener(listener: LoginDialogListener) {
        this.listener = listener
    }

    private fun setupObservers() {
        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Loading -> {
                    binding.progressBarLoginDialog.visibility = View.VISIBLE
                    binding.buttonLoginDialog.isEnabled = false
                }
                is LoginResult.Success -> {
                    binding.progressBarLoginDialog.visibility = View.GONE
                    binding.buttonLoginDialog.isEnabled = true
                    Toast.makeText(context, "Login exitoso!", Toast.LENGTH_SHORT).show()
                    listener?.onLoginSuccess()
                    dismiss()
                }
                is LoginResult.Error -> {
                    binding.progressBarLoginDialog.visibility = View.GONE
                    binding.buttonLoginDialog.isEnabled = true
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.buttonLoginDialog.setOnClickListener {
            val username = binding.editTextUsernameDialog.text.toString().trim()
            val password = binding.editTextPasswordDialog.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(username, password)
            } else {
                Toast.makeText(context, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "LoginDialogFragment"
        fun newInstance(): LoginDialogFragment {
            return LoginDialogFragment()
        }
    }
}
