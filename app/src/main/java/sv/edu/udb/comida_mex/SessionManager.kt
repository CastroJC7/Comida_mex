package sv.edu.udb.comida_mex

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppSession", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveOrder(orderItems: List<Food>) {
        val json = gson.toJson(orderItems)
        sharedPreferences.edit().putString("current_order", json).apply()
    }

    fun getOrder(): List<Food> {
        val json = sharedPreferences.getString("current_order", null)
        return if (json != null) {
            val type = object : TypeToken<List<Food>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }


    fun clearOrder() {
        sharedPreferences.edit().remove("current_order").apply()
    }
}