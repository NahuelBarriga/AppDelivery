package com.deliveryapp.ui.activities

import android.os.Bundle
import com.deliveryapp.R
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.deliveryapp.databinding.ActivityMainBinding
import com.deliveryapp.ui.adapters.PedidoAdapter
import com.deliveryapp.viewmodel.PedidoViewModel
import com.deliveryapp.viewmodel.PedidoViewModelFactory
import com.deliveryapp.repository.PedidoRepository

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    // Instanciamos el ViewModel usando un Factory que recibe el repository
    private val pedidoViewModel: PedidoViewModel by viewModels {
        PedidoViewModelFactory(PedidoRepository())
    }

    private lateinit var adapter: PedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        // Cargar datos al iniciar
        pedidoViewModel.cargarPedidos()
    }

    private fun setupRecyclerView() {
        adapter = PedidoAdapter(emptyList()) { pedido ->
            // Callback cuando el usuario presiona "Cambiar Estado" para un pedido
            mostrarDialogoCambioEstado(pedido.id)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter
    }

    private fun observeViewModel() {
        pedidoViewModel.pedidos.observe(this) { lista ->
            adapter.actualizarLista(lista)
            binding.progressBar.visibility = View.GONE
        }
        pedidoViewModel.error.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun mostrarDialogoCambioEstado(idPedido: Int) {
        // Aquí podrías mostrar un AlertDialog para que el usuario elija el nuevo estado.
        // Para simplificar, supongamos que siempre cambia a "ENTREGADO".
        binding.progressBar.visibility = View.VISIBLE
        pedidoViewModel.cambiarEstadoPedido(idPedido, "ENTREGADO")
    }
}