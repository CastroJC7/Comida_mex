package sv.edu.udb.comida_mex

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import sv.edu.udb.comida_mex.databinding.ActivitySelectFoodBinding

class SelectFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectFoodBinding
    private lateinit var adapter: FoodAdapter
    private lateinit var sessionManager: SessionManager
    private val orderItems = mutableListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        orderItems.addAll(sessionManager.getOrder())

        binding.foodRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(emptyList()) { food ->
            orderItems.add(food)
            updateOrderSummary()
            sessionManager.saveOrder(orderItems)
            Log.d("SelectFoodActivity", "Añadido a la orden: ${food.Nombre}")
            Toast.makeText(this, "${food.Nombre} añadido a la orden", Toast.LENGTH_SHORT).show()
        }
        binding.foodRecyclerView.adapter = adapter

        binding.viewOrderButton.setOnClickListener {
            val intent = Intent(this, ViewOrderActivity::class.java)
            intent.putParcelableArrayListExtra("orderItems", ArrayList(orderItems))
            startActivityForResult(intent, 1001) // Usa startActivityForResult para esperar un resultado
        }

        fetchData()
        updateOrderSummary()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val paymentSuccess = data?.getBooleanExtra("paymentSuccess", false) ?: false
            if (paymentSuccess) {
                // Limpiar la lista de items después del pago
                orderItems.clear()
                sessionManager.saveOrder(orderItems)
                updateOrderSummary()
            }
        }
    }

    private fun fetchData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("comida")
            .get()
            .addOnSuccessListener { result ->
                val foodList = result.mapNotNull { document ->
                    try {
                        Food(
                            id = document.id,
                            Nombre = document.getString("Nombre") ?: "",
                            Precio = document.getDouble("Precio") ?: 0.0,
                            Descripcion = document.getString("Descripcion") ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error parsing document", e)
                        null
                    }
                }
                adapter.updateData(foodList)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents.", exception)
            }
    }

    private fun updateOrderSummary() {
        val totalItems = orderItems.size
        val totalPrice = orderItems.sumOf { it.Precio }
        binding.orderSummaryTextView.text = "Orden: $totalItems  - Total: $${String.format("%.2f", totalPrice)}"
    }
}
