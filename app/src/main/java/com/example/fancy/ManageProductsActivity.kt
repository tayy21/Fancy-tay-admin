package com.example.fancy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.Product
import com.google.firebase.database.*
import android.util.Log

class ManageProductsActivity : AppCompatActivity() {

    // Declare the variables

    private lateinit var productNameEditText: EditText
    private lateinit var productPriceEditText: EditText
    private lateinit var addEditProductButton: Button
    private lateinit var deleteProductButton: Button
    private lateinit var recyclerViewProducts: RecyclerView

    private lateinit var productsAdapter: ProductAdapter
    private val productsList = mutableListOf<Product>()

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("products")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        productNameEditText = findViewById(R.id.editTextProductName)
        productPriceEditText = findViewById(R.id.editTextProductPrice)
        addEditProductButton = findViewById(R.id.btnAddEditProduct)
        deleteProductButton = findViewById(R.id.btnDeleteProduct)
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)

        // Initialize RecyclerView
        productsAdapter = ProductAdapter(this, productsList, object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                // Handle item click
            }
        })
        recyclerViewProducts.adapter = productsAdapter
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)

        // Set click listener for Add/Edit Product button
        addEditProductButton.setOnClickListener {
            addEditProduct()
        }

        // Set click listener for Delete Product button
        deleteProductButton.setOnClickListener {
            deleteProduct()
        }

        // Set item click listener for RecyclerView
        productsAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                productNameEditText.setText(product.prodName)
                productPriceEditText.setText(product.prodPrice.toString())
            }
        })

        // Load products from Firebase Realtime Database
        loadProducts()
    }

    private fun addEditProduct() {
        val productName = productNameEditText.text.toString().trim()
        val productPrice = productPriceEditText.text.toString().trim()

        if (productName.isNotEmpty() && productPrice.isNotEmpty()) {
            val price = productPrice.toDouble()

            val product = Product(prodName = productName, prodPrice = price)

            // Check if editing an existing product
            val existingProduct = productsAdapter.getSelectedProduct()
            if (existingProduct != null) {
                updateProduct(existingProduct.prodId, product)
            } else {
                addProduct(product)
            }
        } else {
            Toast.makeText(this, "Please enter product details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProduct(product: Product) {
        var productId = databaseReference.push().key
        productId?.let {
            product.prodId = it
            // Debugging: Print product information before writing to the database
            Log.d("AddProduct", "Adding product: $product")

            databaseReference.child(it).setValue(product)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Debugging: Print success message after writing to the database
                        Log.d("AddProduct", "Product added successfully to database")
                        // Update local list and notify adapter
                        productsList.add(product)
                        productsAdapter.notifyDataSetChanged()
                        clearFields()
                        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        // Debugging: Print error message if writing to the database fails
                        Log.e("AddProduct", "Error adding product to database: ${task.exception}")
                        Toast.makeText(this, "Error adding product to database", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateProduct(productId: String, updatedProduct: Product) {
        databaseReference.child(productId).setValue(updatedProduct)
            .addOnSuccessListener {
                val index = productsList.indexOfFirst { it.prodId == productId }
                if (index != -1) {
                    productsList[index] = updatedProduct
                    productsAdapter.notifyDataSetChanged()
                    clearFields()
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error updating product: $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteProduct() {
        val selectedProduct = productsAdapter.getSelectedProduct()

        if (selectedProduct != null) {
            databaseReference.child(selectedProduct.prodId)
                .removeValue()
                .addOnSuccessListener {
                    productsList.remove(selectedProduct)
                    productsAdapter.notifyDataSetChanged()
                    clearFields()
                    Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error deleting product: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select a product to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProducts() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                productsList.clear()
                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(Product::class.java)
                    product?.let {
                        it.prodId = snapshot.key.orEmpty()
                        productsList.add(it)
                    }
                }
                productsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@ManageProductsActivity,
                    "Error loading products: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun clearFields() {
        productNameEditText.text.clear()
        productPriceEditText.text.clear()
        productsAdapter.clearSelection()
    }
}


