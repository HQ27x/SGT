package com.example.srl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para el RecyclerView que muestra la lista de categorías.
 *
 * @param categoryList La lista de categorías a mostrar.
 * @param onEditClicked La función a llamar cuando se pulsa el icono de editar.
 * @param onDeleteClicked La función a llamar cuando se pulsa el icono de eliminar.
 */
class CategoryAdapter(
    private val categoryList: List<Category>,
    private val onEditClicked: (Category) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    /**
     * Crea una nueva vista para cada elemento de la lista.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(itemView)
    }

    /**
     * Vincula los datos de una categoría con los elementos de la vista.
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        holder.categoryName.text = currentItem.name

        // Configurar el listener para el icono de editar
        holder.editIcon.setOnClickListener {
            onEditClicked(currentItem)
        }

        // Configurar el listener para el icono de eliminar
        holder.deleteIcon.setOnClickListener {
            onDeleteClicked(currentItem.id)
        }
    }

    /**
     * Devuelve el número total de elementos en la lista.
     */
    override fun getItemCount() = categoryList.size

    /**
     * Clase interna que representa la vista de cada elemento en la lista.
     */
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.category_name_text_view)
        val editIcon: ImageView = itemView.findViewById(R.id.edit_category_icon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.delete_category_icon)
    }
}