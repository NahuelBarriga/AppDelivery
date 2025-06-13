package com.deliveryapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.deliveryapp.data.models.Pedido
import com.deliveryapp.databinding.ItemPedidoBinding

class PedidoAdapter(private var pedidos: List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: Pedido, position: Int) {
            // Configurar datos básicos
            binding.tvCliente.text = pedido.nombreCliente
            binding.tvId.text = pedido.id.toString()
            binding.tvDireccion.text = pedido.direccion
            binding.tvTotal.text = "$${pedido.total}"

            // Configurar estado visual
            configurarEstadoPedido(pedido)

            // Configurar expansión
            configurarExpansion(pedido, position)

            // Configurar botones según estado
            configurarBotones(pedido, position)
        }

        private fun configurarEstadoPedido(pedido: Pedido) {
            val estado = pedido.estado.lowercase()

            when (estado) {
                "pendiente envio" -> {
                    binding.tvEstado.text = "PENDIENTE ENVÍO"
                    binding.tvEstado.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF9800"))
                    binding.statusIndicator.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF9800"))
                }
                "enviado" -> {
                    binding.tvEstado.text = "ENVIADO"
                    binding.tvEstado.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                    binding.statusIndicator.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
                }
                "entregado" -> {
                    binding.tvEstado.text = "ENTREGADO"
                    binding.tvEstado.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                    binding.statusIndicator.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                }
                "cancelado", "entrega cancelada" -> {
                    binding.tvEstado.text = "CANCELADO"
                    binding.tvEstado.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F44336"))
                    binding.statusIndicator.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F44336"))
                }
            }
        }

        private fun configurarExpansion(pedido: Pedido, position: Int) {
            // Mostrar u ocultar parte expandida
            binding.layoutExpandible.visibility = if (pedido.expandido) View.VISIBLE else View.GONE

            // Rotar flecha según estado
            binding.ivExpandArrow.rotation = if (pedido.expandido) 180f else 0f

            // Click listener para expandir/contraer
            binding.itemRoot.setOnClickListener {
                val wasExpanded = pedido.expandido
                pedido.expandido = !pedido.expandido

                // Animar flecha
                val targetRotation = if (pedido.expandido) 180f else 0f
                binding.ivExpandArrow.animate()
                    .rotation(targetRotation)
                    .setDuration(200)
                    .start()

                // Actualizar visibilidad
                binding.layoutExpandible.visibility = if (pedido.expandido) View.VISIBLE else View.GONE

                notifyItemChanged(position)
            }
        }

        private fun configurarBotones(pedido: Pedido, position: Int) {
            val estado = pedido.estado.lowercase()

            // Ocultar todos los botones primero
            binding.btnAsignarse.visibility = View.GONE
            binding.btnEntregado.visibility = View.GONE
            binding.btnCancelado.visibility = View.GONE

            when (estado) {
                "pendiente envio" -> {
                    // Solo mostrar botón asignarse
                    binding.btnAsignarse.visibility = View.VISIBLE
                    binding.btnAsignarse.setOnClickListener {
                        pedido.estado = "enviado"
                        notifyItemChanged(position)
                        // TODO: hacer PATCH al servidor
                    }
                }
                "enviado" -> {
                    // Mostrar botones entregado y cancelado
                    binding.btnEntregado.visibility = View.VISIBLE
                    binding.btnCancelado.visibility = View.VISIBLE

                    binding.btnEntregado.setOnClickListener {
                        pedido.estado = "entregado"
                        notifyItemChanged(position)
                        // TODO: hacer PATCH al servidor
                    }

                    binding.btnCancelado.setOnClickListener {
                        mostrarDialogoConfirmacion(pedido, position)
                    }
                }
                "entregado", "cancelado", "entrega cancelada" -> {
                    // No mostrar botones para estados finales
                }
            }
        }

        private fun mostrarDialogoConfirmacion(pedido: Pedido, position: Int) {
            AlertDialog.Builder(binding.root.context)
                .setTitle("Confirmar cancelación")
                .setMessage("¿Estás seguro de que quieres cancelar este pedido?")
                .setPositiveButton("Sí, cancelar") { _, _ ->
                    pedido.estado = "cancelado"
                    notifyItemChanged(position)
                    // TODO: hacer PATCH al servidor
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.bind(pedido, position)
    }

    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nuevosPedidos: List<Pedido>) {
        pedidos = nuevosPedidos
        notifyDataSetChanged()
    }
}