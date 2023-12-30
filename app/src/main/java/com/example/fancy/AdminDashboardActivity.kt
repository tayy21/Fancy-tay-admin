package com.example.fancy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)

        // Find buttons by their IDs
        val btnManageProducts: Button = findViewById(R.id.btnManageProducts)
        val btnViewOrders: Button = findViewById(R.id.btnViewOrders)

        // Set click listeners for each button
        btnManageProducts.setOnClickListener {
            // Handle the click, for example, navigate to the ManageProductsActivity
            val manageProductsIntent = Intent(this, ManageProductsActivity::class.java)
            startActivity(manageProductsIntent)
        }

        btnViewOrders.setOnClickListener {
            // Handle the click, for example, navigate to the ViewOrdersActivity
            val viewOrdersIntent = Intent(this, ViewOrdersActivity::class.java)
            startActivity(viewOrdersIntent)
        }

        // Add more click listeners for other buttons as needed

    }
}