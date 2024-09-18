package sv.edu.udb.comida_mex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderHistoryAdapter(private val orderHistory: List<List<Food>>) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderDetailsTextView: TextView = view.findViewById(R.id.orderDetailsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_history, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = orderHistory[position]
        val orderText = buildString {
            order.forEach { food ->
                appendLine("${food.Nombre} - $${String.format("%.2f", food.Precio)}")
            }
            val total = order.sumOf { it.Precio }
            appendLine("Total: $${String.format("%.2f", total)}")
        }
        holder.orderDetailsTextView.text = orderText
    }

    override fun getItemCount(): Int {
        return orderHistory.size
    }
}
