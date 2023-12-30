package com.example.fancy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fancy.data.Product

class ProductAdapter(
    private val context: Context,
    private val productList: List<Product>,
    private var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Selected product in the adapter
    private var selectedProduct: Product? = null

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    init {
        this.itemClickListener = itemClickListener
    }

    // Setter method for item click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    // Getter method for the selected product
    fun getSelectedProduct(): Product? {
        return selectedProduct
    }

    // Method to clear the selection
    fun clearSelection() {
        selectedProduct = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product, itemClickListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        private val textProductName: TextView = itemView.findViewById(R.id.textProductName)
        private val textProductPrice: TextView = itemView.findViewById(R.id.textProductPrice)

        // Method to bind data to views
        fun bind(product: Product, clickListener: OnItemClickListener) {
            // Bind data to views
            // For simplicity, assuming that you have a method to load an image from a URL
            // into the ImageView (e.g., using Glide or Picasso)
            // Replace 'loadImageFromUrl()' with your actual method
            // loadImageFromUrl(product.prodImage, imageProduct)

            textProductName.text = product.prodName
            textProductPrice.text = context.getString(R.string.price_format, product.prodPrice)

            // Set click listener
            itemView.setOnClickListener {
                // Update the selected product and notify the adapter
                selectedProduct = product
                notifyDataSetChanged()

                // Trigger the onItemClick method
                clickListener.onItemClick(product)
            }
        }
    }
}