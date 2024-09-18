package sv.edu.udb.comida_mex

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        auth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    // Permanece en la actividad WelcomeActivity ya que es el "Inicio"
                    true
                }
                R.id.action_select_food -> {
                    // Abre la actividad para seleccionar alimentos y bebidas
                    val intent = Intent(this, SelectFoodActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_view_order -> {
                    // Abre la actividad para ver la orden
                    val intent = Intent(this, ViewOrderActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_order_history -> {
                    // Abre la actividad para ver el historial de compras
                    val intent = Intent(this, OrderHistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_logout -> {
                    // Cierra sesión, limpia la orden actual y regresa a la actividad de inicio
                    auth.signOut()
                    sessionManager.clearOrder()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}