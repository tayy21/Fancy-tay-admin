package com.example.fancy

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.Order
import com.google.firebase.database.*

class ViewOrdersActivity : AppCompatActivity() {

    private lateinit var btnMarkDone: Button
    private lateinit var btnRemoveCompleted: Button
    private lateinit var recyclerViewOrders: RecyclerView

    private lateinit var ordersAdapter: OrdersAdapter
    private val ordersList = mutableListOf<Order>()

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_orders)

        btnMarkDone = findViewById(R.id.btnMarkDone)
        btnRemoveCompleted = findViewById(R.id.btnRemoveCompleted)
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders)

        // Initialize RecyclerView
        /*ordersAdapter = OrdersAdapter(ordersList)
        recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        recyclerViewOrders.adapter = ordersAdapter*/

        // Set click listener for Mark Done button
        btnMarkDone.setOnClickListener {
            markOrdersDone()
        }

        // Set click listener for Remove Completed button
        btnRemoveCompleted.setOnClickListener {
            removeCompletedOrders()
        }

        // Load orders from the database
        loadOrders()
    }

    private fun loadOrders() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ordersList.clear()
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(Order::class.java)
                    order?.let {
                        it.orderId = snapshot.key.orEmpty()
                        ordersList.add(it)
                    }
                }
                ordersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@ViewOrdersActivity,
                    "Error loading orders: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun markOrdersDone() {
        // Implement logic to mark selected orders as done
        val selectedOrders = ordersAdapter.getSelectedOrders()
        // Update the status of selected orders to "Completed" in the database
        // You need to implement the database update logic here
        Toast.makeText(this, "Marked orders as done", Toast.LENGTH_SHORT).show()
    }

    private fun removeCompletedOrders() {
        // Implement logic to remove completed orders from the database
        // You need to implement the database removal logic here
        Toast.makeText(this, "Removed completed orders", Toast.LENGTH_SHORT).show()
    }
}
