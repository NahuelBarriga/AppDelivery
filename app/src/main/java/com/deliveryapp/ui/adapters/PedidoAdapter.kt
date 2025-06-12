package com.deliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deliveryapp.R
import com.deliveryapp.data.models.Pedido

class PedidoAdapter(
    private var listaPedidos: List<Pedido>,
    private val listenerCambioEstado: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIdPedido: TextView = itemView.findViewById(R.id.tvIdPedido)
        val tvCliente: TextView = itemView.findViewById(R.id.tvCliente)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val btnCambiarEstado: Button = itemView.findViewById(R.id.btnCambiarEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listaPedidos[position]
        holder.tvIdPedido.text = "Pedido #${pedido.id}"
        holder.tvCliente.text = "Cliente: ${pedido.nombreCliente}"
        holder.tvEstado.text = "Estado: ${pedido.estado}"

        // Al hacer click, invocamos el callback que viene de la Activity/Fragment
        holder.btnCambiarEstado.setOnClickListener {
            listenerCambioEstado(pedido)
        }
    }

    override fun getItemCount(): Int = listaPedidos.size

    fun actualizarLista(nuevaLista: List<Pedido>) {
        listaPedidos = nuevaLista
        notifyDataSetChanged()
    }
}