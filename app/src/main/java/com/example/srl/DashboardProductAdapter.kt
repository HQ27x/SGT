package com.example.srl

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para el RecyclerView que muestra la lista de productos en el dashboard.
 *
 * @param productList La lista de productos a mostrar.
 */
class DashboardProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<DashboardProductAdapter.ProductViewHolder>() {

    /**
     * Crea una nueva vista para cada elemento de la lista.
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_product, parent, false)
        return ProductViewHolder(itemView)
    }

    /**
     * Vincula los datos de un producto con los elementos de la vista.
     */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.productName.text = currentItem.name
        holder.productQuantity.text = "Cantidad ${currentItem.quantity}"
        holder.productPrice.text = "Precio S/.${currentItem.price}"

        // Resaltar el producto si el stock es bajo
        if (currentItem.quantity <= 5) {
            holder.productName.setTextColor(Color.RED)
        } else {
            holder.productName.setTextColor(holder.defaultTextColor)
        }
    }

    /**
     * Devuelve el número total de elementos en la lista.
     */
    override fun getItemCount() = productList.size
    /**
     * Clase interna que representa la vista de cada elemento en la lista.
     */
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val defaultTextColor: ColorStateList = productName.textColors // Guardar el color de texto por defecto
    }
}