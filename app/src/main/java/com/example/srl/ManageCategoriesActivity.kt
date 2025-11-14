package com.example.srl

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ManageCategoriesActivity : AppCompatActivity() {

    // Servicio de Firebase Firestore
    private lateinit var firestore: FirebaseFirestore

    // Elementos de la interfaz de usuario
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    // Lista de datos
    private val categoryList = mutableListOf<Category>()

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance()

        // Configurar el RecyclerView
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar el botón y el campo de texto para añadir una nueva categoría
        val addCategoryButton = findViewById<Button>(R.id.add_category_button)
        val categoryNameEditText = findViewById<EditText>(R.id.category_name_edit_text)

        addCategoryButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                addCategory(categoryName)
                categoryNameEditText.text.clear() // Limpiar el campo de texto después de añadir
            }
        }

        // Obtener las categorías de Firestore
        fetchCategories()
    }

    /**
     * Obtiene la lista de categorías de Firestore en tiempo real.
     */
    private fun fetchCategories() {
        firestore.collection("categories")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ManageCategories", "Error al obtener los documentos: ", e)
                    return@addSnapshotListener
                }

                categoryList.clear()
                for (document in snapshots!!) {
                    val category = document.toObject(Category::class.java).apply {
                        id = document.id
                    }
                    categoryList.add(category)
                }

                // Inicializar el adaptador si no lo está, o notificar los cambios si ya existe
                if (!::categoryAdapter.isInitialized) {
                    categoryAdapter = CategoryAdapter(categoryList,
                        onEditClicked = { category -> showEditDialog(category) },
                        onDeleteClicked = { categoryId -> showDeleteDialog(categoryId) }
                    )
                    categoriesRecyclerView.adapter = categoryAdapter
                } else {
                    categoryAdapter.notifyDataSetChanged()
                }
            }
    }

    /**
     * Añade una nueva categoría a Firestore.
     */
    private fun addCategory(name: String) {
        val category = Category(name)
        firestore.collection("categories").add(category)
    }

    /**
     * Muestra un diálogo para editar el nombre de una categoría.
     */
    private fun showEditDialog(category: Category) {
        val editText = EditText(this)
        editText.setText(category.name)

        AlertDialog.Builder(this)
            .setTitle("Editar Categoría")
            .setView(editText)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    updateCategory(category.id, newName)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Actualiza el nombre de una categoría en Firestore.
     */
    private fun updateCategory(id: String, newName: String) {
        firestore.collection("categories").document(id).update("name", newName)
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar una categoría.
     */
    private fun showDeleteDialog(categoryId: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Borrado")
            .setMessage("¿Está seguro de que desea eliminar esta categoría?")
            .setPositiveButton("Sí") { _, _ ->
                deleteCategory(categoryId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    /**
     * Elimina una categoría de Firestore según su ID.
     */
    private fun deleteCategory(categoryId: String) {
        firestore.collection("categories").document(categoryId).delete()
    }
}