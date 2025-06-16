package com.deliveryapp.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.deliveryapp.databinding.DialogLoginBinding // Importa el ViewBinding generado para dialog_login.xml
import com.deliveryapp.viewmodel.LoginResult
import com.deliveryapp.viewmodel.LoginViewModel
import kotlin.text.isNotEmpty
import kotlin.text.trim

class LoginDialogFragment : DialogFragment() {

    private var _binding: DialogLoginBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val loginViewModel: LoginViewModel by viewModels() // Puedes compartir el ViewModel con la Activity si es necesario

    // Interfaz para comunicar el resultado a la Activity que lo llama
    interface LoginDialogListener {
        fun onLoginSuccess()
        fun onLoginCancelled() // Opcional, si quieres manejar la cancelación del diálogo
    }

    private var listener: LoginDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogLoginBinding.inflate(LayoutInflater.from(context))

        // No permitir cerrar el diálogo tocando fuera o con el botón atrás hasta que se loguee
        isCancelable = false

        setupObservers()
        setupListeners()

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            // .setTitle("Iniciar Sesión") // Opcional, ya que lo tienes en el layout
            // No agregamos botones aquí, ya que están en el layout del diálogo
            .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // El view ya se infló en onCreateDialog, pero DialogFragment requiere que se retorne aquí.
        // Si no se usa MaterialAlertDialogBuilder, se inflaría aquí.
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
                    dismiss() // Cierra el diálogo
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
        _binding = null // Evitar memory leaks
    }

    companion object {
        const val TAG = "LoginDialogFragment"
        fun newInstance(): LoginDialogFragment {
            return LoginDialogFragment()
        }
    }
}