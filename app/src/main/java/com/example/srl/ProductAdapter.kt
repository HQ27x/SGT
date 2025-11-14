package com.example.srl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para el RecyclerView que muestra la lista de productos en la pantalla de gestión.
 *
 * @param productList La lista de productos a mostrar.
 * @param onDeleteClicked La función a llamar cuando se pulsa el icono de eliminar.
 * @param onEditClicked La función a llamar cuando se pulsa el icono de editar.
 * @param onAddClicked La función a llamar cuando se pulsa el icono de añadir cantidad.
 * @param onRemoveClicked La función a llamar cuando se pulsa el icono de restar cantidad.
 */
class ProductAdapter(
    private val productList: List<Product>,
    private val onDeleteClicked: (String) -> Unit,
    private val onEditClicked: (Product) -> Unit,
    private val onAddClicked: (Product) -> Unit,
    private val onRemoveClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    /**
     * Crea una nueva vista para cada elemento de la lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    /**
     * Vincula los datos de un producto con los elementos de la vista.
     */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.productName.text = currentItem.name
        holder.productCategory.text = currentItem.category
        holder.productQuantity.text = currentItem.quantity.toString()
        holder.productPrice.text = "s./ ${currentItem.price}"

        // Configurar los listeners para los iconos de acción
        holder.deleteAction.setOnClickListener {
            onDeleteClicked(currentItem.id)
        }

        holder.editAction.setOnClickListener {
            onEditClicked(currentItem)
        }

        holder.addAction.setOnClickListener {
            onAddClicked(currentItem)
        }

        holder.removeAction.setOnClickListener {
            onRemoveClicked(currentItem)
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
        val productCategory: TextView = itemView.findViewById(R.id.product_category)
        val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val addAction: ImageView = itemView.findViewById(R.id.add_action)
        val removeAction: ImageView = itemView.findViewById(R.id.remove_action)
        val editAction: ImageView = itemView.findViewById(R.id.edit_action)
        val deleteAction: ImageView = itemView.findViewById(R.id.delete_action)
    }
}