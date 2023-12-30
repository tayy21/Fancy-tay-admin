package com.example.fancy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.Order

class OrdersAdapter(
    private val context: Context,
    private val ordersList: List<Order>
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private val selectedOrders = mutableSetOf<Order>()

    fun getSelectedOrders(): Set<Order> {
        return selectedOrders
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textOrderId: TextView = itemView.findViewById(R.id.textOrderId)
        private val textOrderStatus: TextView = itemView.findViewById(R.id.textOrderStatus)
        private val checkBoxOrder: CheckBox = itemView.findViewById(R.id.checkBoxOrder)

        fun bind(order: Order) {
            textOrderId.text = "Order ID: ${order.orderId}"
            textOrderStatus.text = "Status: ${order.orderStatus}"

            // Set checked state based on selection
            checkBoxOrder.isChecked = selectedOrders.contains(order)

            // Set click listener
            itemView.setOnClickListener {
                // Toggle the selection state
                if (selectedOrders.contains(order)) {
                    selectedOrders.remove(order)
                } else {
                    selectedOrders.add(order)
                }

                // Update the checkbox state
                checkBoxOrder.isChecked = selectedOrders.contains(order)
            }
        }
    }
}
