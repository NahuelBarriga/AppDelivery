//package com.deliveryapp.ui.activities
//
//import android.os.Bundle
//import com.deliveryapp.R
//import androidx.appcompat.app.AppCompatActivity
//import android.view.View
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.deliveryapp.databinding.ActivityMainBinding
//import com.deliveryapp.ui.adapters.PedidoAdapter
//import com.deliveryapp.viewmodel.PedidoViewModel
//import com.deliveryapp.viewmodel.PedidoViewModelFactory
//import com.deliveryapp.repository.PedidoRepository
//
//class MainActivity : AppCompatActivity() {
//
//
//    private lateinit var binding: ActivityMainBinding
//
//    // Instanciamos el ViewModel usando un Factory que recibe el repository
//    private val pedidoViewModel: PedidoViewModel by viewModels {
//        PedidoViewModelFactory(PedidoRepository())
//    }
//
//    private lateinit var adapter: PedidoAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupRecyclerView()
//        observeViewModel()
//
//        // Cargar datos al iniciar
//        pedidoViewModel.cargarPedidos()
//    }
//
//    private fun setupRecyclerView() {
//        adapter = PedidoAdapter(emptyList()) { pedido ->
//            // Callback cuando el usuario presiona "Cambiar Estado" para un pedido
//            mostrarDialogoCambioEstado(pedido.id)
//        }
//        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
//        binding.rvPedidos.adapter = adapter
//    }
//
//    private fun observeViewModel() {
//        pedidoViewModel.pedidos.observe(this) { lista ->
//            adapter.actualizarLista(lista)
//            binding.progressBar.visibility = View.GONE
//        }
//        pedidoViewModel.error.observe(this) { mensaje ->
//            mensaje?.let {
//                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
//                binding.progressBar.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun mostrarDialogoCambioEstado(idPedido: Int) {
//        // Aquí podrías mostrar un AlertDialog para que el usuario elija el nuevo estado.
//        // Para simplificar, supongamos que siempre cambia a "ENTREGADO".
//        binding.progressBar.visibility = View.VISIBLE
//        pedidoViewModel.cambiarEstadoPedido(idPedido, "ENTREGADO")
//    }
//}
package com.deliveryapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.deliveryapp.databinding.ActivityMainBinding
import com.deliveryapp.repository.PedidoRepository
import com.deliveryapp.ui.adapters.PedidoAdapter
import com.deliveryapp.viewmodel.PedidoViewModel
import com.deliveryapp.viewmodel.PedidoViewModelFactory

/*class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PedidoAdapter

    private val viewModel: PedidoViewModel by viewModels {
        PedidoViewModelFactory(PedidoRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PedidoAdapter(emptyList())
        binding.recyclerPedidos.layoutManager = LinearLayoutManager(this)
        binding.recyclerPedidos.adapter = adapter

        viewModel.pedidos.observe(this) { pedidos ->
            adapter.actualizarLista(pedidos)
        }

        viewModel.cargarPedidos()
    }
}*/




class MainActivity : AppCompatActivity(), LoginDialogFragment.LoginDialogListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pedidoAdapter: PedidoAdapter // Renombré 'adapter' a 'pedidoAdapter' para claridad

    // ViewModel para los pedidos
    private val pedidoViewModel: PedidoViewModel by viewModels {
        PedidoViewModelFactory(PedidoRepository()) // Asumo que tienes PedidoRepository
    }

    private var isUserAuthenticated = false // Simula el estado de autenticación inicial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialmente, el contenido principal podría estar oculto o deshabilitado
        // binding.recyclerPedidos.visibility = View.GONE // Ejemplo
        // binding.textViewPlaceholder.visibility = View.VISIBLE // Un placeholder mientras no se loguea

        // Comprobar si el usuario ya está autenticado (ej. desde SharedPreferences en un caso real)
        // Por ahora, 'isUserAuthenticated' controla esto. En una app real, leerías esto
        // de SharedPreferences o similar.
        if (!isUserAuthenticated) {
            showLoginDialog()
        } else {
            // Si ya está autenticado (ej. sesión recordada), carga el contenido directamente
            initializePedidosFeature()
        }
    }

    private fun showLoginDialog() {
        val loginDialog = LoginDialogFragment.newInstance()
        loginDialog.setLoginListener(this)
        loginDialog.show(supportFragmentManager, LoginDialogFragment.TAG)
    }

    /**
     * Esta función se llama después de que el login es exitoso.
     * Aquí es donde configuras el RecyclerView y cargas los pedidos.
     */
    private fun initializePedidosFeature() {
        // Ocultar cualquier placeholder de "login requerido" y mostrar el recycler
        // binding.textViewPlaceholder.visibility = View.GONE // Ejemplo
        binding.recyclerPedidos.visibility = View.VISIBLE

        pedidoAdapter = PedidoAdapter(emptyList()) // Pasas tu lambda de click si la tienes
        binding.recyclerPedidos.layoutManager = LinearLayoutManager(this)
        binding.recyclerPedidos.adapter = pedidoAdapter

        pedidoViewModel.pedidos.observe(this) { pedidos ->
            pedidoAdapter.actualizarLista(pedidos)
            // Aquí podrías manejar la visibilidad de un ProgressBar si la carga es asíncrona dentro del ViewModel
        }

        // Solo carga los pedidos después de que el usuario esté autenticado y la UI esté lista
        pedidoViewModel.cargarPedidos()

        // Aquí podrías habilitar otros elementos de la UI relacionados con los pedidos
    }

    /**
     * Esta función se llama si el login es cancelado o falla y el diálogo se cierra sin éxito.
     * Mantiene la app en un estado "bloqueado" o muestra un mensaje.
     */
    private fun blockAppContent() {
        binding.recyclerPedidos.visibility = View.GONE
        // Muestra un mensaje o un placeholder indicando que se requiere login
        // Por ejemplo, si tienes un TextView en tu layout activity_main.xml:
        // binding.textViewLoginRequiredMessage.visibility = View.VISIBLE
        // binding.textViewLoginRequiredMessage.text = "Por favor, inicia sesión para ver los pedidos."
        Toast.makeText(this, "Se requiere inicio de sesión para usar la aplicación.", Toast.LENGTH_LONG).show()
    }

    // --- Implementación de LoginDialogFragment.LoginDialogListener ---
    override fun onLoginSuccess() {
        isUserAuthenticated = true // Marcar como autenticado
        Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
        // Ahora que el login fue exitoso, inicializa la funcionalidad de pedidos
        initializePedidosFeature()
    }

    override fun onLoginCancelled() {
        Toast.makeText(this, "Inicio de sesión cancelado.", Toast.LENGTH_SHORT).show()
        // Decide qué hacer: cerrar la app, mantenerla bloqueada, etc.
        blockAppContent()
        // podrías considerar finish() si el login es absolutamente obligatorio y se cancela
        // finish()
    }
}