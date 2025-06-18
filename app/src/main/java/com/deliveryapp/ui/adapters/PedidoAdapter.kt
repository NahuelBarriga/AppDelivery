package com.deliveryapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.deliveryapp.data.models.Pedido
import com.deliveryapp.databinding.ItemPedidoBinding

class PedidoAdapter(
    private var pedidos: List<Pedido>,
    private val onEstadoChange: (idPedido: Int, nuevoEstado: String, onSuccess: () -> Unit) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: Pedido, position: Int) {
            binding.tvCliente.text = pedido.cliente.nombre
            binding.tvId.text = pedido.id.toString()
            binding.tvDireccion.text = pedido.cliente.direccion

            configurarEstadoPedido(pedido)
            configurarExpansion(pedido, position)
            configurarBotones(pedido, position)
        }

        private fun configurarEstadoPedido(pedido: Pedido) {
            val estado = pedido.estado.lowercase()

            when (estado) {
                "listo para enviar" -> {
                    binding.tvEstado.text = "listo para enviar"
                    binding.tvEstado.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF9800"))
                    binding.statusIndicator.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF9800"))
                }
                "en camino" -> {
                    binding.tvEstado.text = "EN CAMINO"
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
            binding.layoutExpandible.visibility = if (pedido.expandido) View.VISIBLE else View.GONE
            binding.ivExpandArrow.rotation = if (pedido.expandido) 180f else 0f

            binding.itemRoot.setOnClickListener {
                pedido.expandido = !pedido.expandido
                val targetRotation = if (pedido.expandido) 180f else 0f
                binding.ivExpandArrow.animate()
                    .rotation(targetRotation)
                    .setDuration(200)
                    .start()
                binding.layoutExpandible.visibility = if (pedido.expandido) View.VISIBLE else View.GONE
                notifyItemChanged(position)
            }
        }

        @OptIn(UnstableApi::class)
        private fun configurarBotones(pedido: Pedido, position: Int) {
            val estado = pedido.estado.lowercase()

            binding.btnAsignarse.visibility = View.GONE
            binding.btnEntregado.visibility = View.GONE
            binding.btnCancelado.visibility = View.GONE

            when (estado) {
                "listo para enviar" -> {
                    Log.w("Prueba", "llegue")
                    binding.btnAsignarse.visibility = View.VISIBLE
                    binding.btnAsignarse.setOnClickListener {
                        onEstadoChange(pedido.id, "en camino") {
                            pedido.estado = "en camino"
                            notifyItemChanged(position)
                        }
                    }
                }
                "en camino" -> {
                    binding.btnEntregado.visibility = View.VISIBLE
                    binding.btnCancelado.visibility = View.VISIBLE

                    binding.btnEntregado.setOnClickListener {
                        onEstadoChange(pedido.id, "entregado") {
                            pedido.estado = "entregado"
                            notifyItemChanged(position)
                        }
                    }

                    binding.btnCancelado.setOnClickListener {
                        mostrarDialogoConfirmacion(pedido, position)
                    }
                }
            }
        }

        private fun mostrarDialogoConfirmacion(pedido: Pedido, position: Int) {
            AlertDialog.Builder(binding.root.context)
                .setTitle("Confirmar cancelación")
                .setMessage("¿Estás seguro de que quieres cancelar este pedido?")
                .setPositiveButton("Sí, cancelar") { _, _ ->
                    onEstadoChange(pedido.id, "cancelado") {
                        pedido.estado = "cancelado"
                        notifyItemChanged(position)
                    }
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
