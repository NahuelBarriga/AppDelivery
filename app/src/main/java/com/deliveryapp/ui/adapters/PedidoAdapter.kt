//package com.deliveryapp.ui.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.deliveryapp.R
//import com.deliveryapp.data.models.Pedido
//
//class PedidoAdapter(
//    private var listaPedidos: List<Pedido>,
//    private val listenerCambioEstado: (Pedido) -> Unit
//) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {
//
//    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvIdPedido: TextView = itemView.findViewById(R.id.tvIdPedido)
//        val tvCliente: TextView = itemView.findViewById(R.id.tvCliente)
//        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
//        val btnCambiarEstado: Button = itemView.findViewById(R.id.btnCambiarEstado)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_pedido, parent, false)
//        return PedidoViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
//        val pedido = listaPedidos[position]
//        holder.tvIdPedido.text = "Pedido #${pedido.id}"
//        holder.tvCliente.text = "Cliente: ${pedido.nombreCliente}"
//        holder.tvEstado.text = "Estado: ${pedido.estado}"
//
//        // Al hacer click, invocamos el callback que viene de la Activity/Fragment
//        holder.btnCambiarEstado.setOnClickListener {
//            listenerCambioEstado(pedido)
//        }
//    }
//
//    override fun getItemCount(): Int = listaPedidos.size
//
//    fun actualizarLista(nuevaLista: List<Pedido>) {
//        listaPedidos = nuevaLista
//        notifyDataSetChanged()
//    }
//}

package com.deliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deliveryapp.data.models.Pedido
import com.deliveryapp.databinding.ItemPedidoBinding

class PedidoAdapter(private var pedidos: List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(private val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pedido: Pedido) {
            binding.tvCliente.text = pedido.nombreCliente
            binding.tvEstado.text = pedido.estado
            binding.tvId.text = "Pedido #${pedido.id}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(pedidos[position])
    }

    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nuevosPedidos: List<Pedido>) {
        pedidos = nuevosPedidos
        notifyDataSetChanged()
    }
}
