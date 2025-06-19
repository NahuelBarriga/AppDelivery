package com.deliveryapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.deliveryapp.databinding.ActivityMainBinding
import com.deliveryapp.repository.PedidoRepository
import com.deliveryapp.ui.adapters.PedidoAdapter
import com.deliveryapp.viewmodel.PedidoViewModel
import com.deliveryapp.viewmodel.PedidoViewModelFactory

@UnstableApi
class MainActivity : AppCompatActivity(), LoginDialogFragment.LoginDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pedidoAdapter: PedidoAdapter

    private val pedidoViewModel: PedidoViewModel by viewModels {
        PedidoViewModelFactory(PedidoRepository())
    }

    private var isUserAuthenticated = false // Simula autenticación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isUserAuthenticated) {
            showLoginDialog()
        } else {
            initializePedidosFeature()
        }
    }

    private fun showLoginDialog() {
        val loginDialog = LoginDialogFragment.newInstance()
        loginDialog.setLoginListener(this)
        loginDialog.show(supportFragmentManager, LoginDialogFragment.TAG)
    }

    private fun initializePedidosFeature() {
        binding.recyclerPedidos.visibility = android.view.View.VISIBLE

        pedidoAdapter = PedidoAdapter(emptyList()) { idPedido, nuevoEstado, onSuccess ->
            pedidoViewModel.actualizarEstadoPedido(idPedido, nuevoEstado) { exito ->
                if (exito) {
                    onSuccess() // Llama a onSuccess() solo si fue exitoso
                    pedidoViewModel.cargarPedidos()
                } else {
                    Toast.makeText(this, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.recyclerPedidos.layoutManager = LinearLayoutManager(this)
        binding.recyclerPedidos.adapter = pedidoAdapter

        pedidoViewModel.pedidos.observe(this) { pedidos ->
            val pedidosFiltrados = pedidos.filter {
                it.estado.equals("listo para enviar", ignoreCase = true) ||
                        it.estado.equals("en camino", ignoreCase = true)
            }
            pedidoAdapter.actualizarLista(pedidosFiltrados)
        }
        pedidoViewModel.cargarPedidos()

        binding.btnRefresh.setOnClickListener {
            pedidoViewModel.cargarPedidos()
        }
    }

    private fun blockAppContent() {
        binding.recyclerPedidos.visibility = android.view.View.GONE
        Toast.makeText(this, "Se requiere inicio de sesión para usar la aplicación.", Toast.LENGTH_LONG).show()
    }

    override fun onLoginSuccess() {
        isUserAuthenticated = true
        Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
        initializePedidosFeature()
    }

    override fun onLoginCancelled() {
        Toast.makeText(this, "Inicio de sesión cancelado.", Toast.LENGTH_SHORT).show()
        blockAppContent()
    }
}
