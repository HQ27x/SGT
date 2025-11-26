package com.example.srl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ManageProductsActivity : AppCompatActivity() {

    // Servicio de Firebase Firestore
    private lateinit var firestore: FirebaseFirestore

    // Elementos de la interfaz de usuario
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var emptyStateView: LinearLayout

    // Lista de datos
    private val productList = mutableListOf<Product>()
    private var productToDeleteId: String? = null

    // Lanzador de actividad para la pantalla de confirmación de borrado
    private val deleteConfirmationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            productToDeleteId?.let { deleteProductFromFirestore(it) }
        }
    }

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        // Inicializar Firestore
        firestore = FirebaseFirestore.getInstance()

        // Configurar el RecyclerView
        productsRecyclerView = findViewById(R.id.products_recycler_view)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        loadingSpinner = findViewById(R.id.loading_spinner)
        emptyStateView = findViewById(R.id.empty_state_view)

        // Configurar el botón para añadir un nuevo producto
        val addProductButton: Button = findViewById(R.id.add_product_button)
        addProductButton.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        // Configurar la navegación inferior
        setupBottomNavigation()

        // Obtener los productos de Firestore
        fetchProducts()
    }

    /**
     * Obtiene la lista de productos de Firestore en tiempo real.
     */
    private fun fetchProducts() {
        loadingSpinner.visibility = View.VISIBLE
        productsRecyclerView.visibility = View.GONE
        emptyStateView.visibility = View.GONE

        firestore.collection("products")
            .addSnapshotListener { snapshots, e ->
                loadingSpinner.visibility = View.GONE

                if (e != null) {
                    Log.w("ManageProductsActivity", "Error al obtener los documentos: ", e)
                    Toast.makeText(this, "Error al cargar los productos", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                productList.clear()
                for (document in snapshots!!) {
                    val product = document.toObject(Product::class.java).apply {
                        id = document.id
                    }
                    productList.add(product)
                }

                if (productList.isEmpty()) {
                    productsRecyclerView.visibility = View.GONE
                    emptyStateView.visibility = View.VISIBLE
                } else {
                    productsRecyclerView.visibility = View.VISIBLE
                    emptyStateView.visibility = View.GONE

                    // Inicializar el adaptador si no lo está, o notificar los cambios si ya existe
                    if (!::productAdapter.isInitialized) {
                        productAdapter = ProductAdapter(
                            productList,
                            onDeleteClicked = { productId -> showDeleteConfirmation(productId) },
                            onEditClicked = { product -> editProduct(product) },
                            onAddClicked = { product -> addProductQuantity(product) },
                            onRemoveClicked = { product -> removeProductQuantity(product) }
                        )
                        productsRecyclerView.adapter = productAdapter
                    } else {
                        productAdapter.notifyDataSetChanged()
                    }
                }
            }
    }

    /**
     * Muestra la pantalla de confirmación antes de eliminar un producto.
     */
    private fun showDeleteConfirmation(productId: String) {
        productToDeleteId = productId
        val intent = Intent(this, DeleteConfirmationActivity::class.java)
        deleteConfirmationLauncher.launch(intent)
    }

    /**
     * Elimina un producto de Firestore según su ID.
     */
    private fun deleteProductFromFirestore(productId: String) {
        firestore.collection("products").document(productId)
            .delete()
            .addOnSuccessListener {
                val intent = Intent(this, DeleteSuccessActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar el producto: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Abre la pantalla de edición de productos con los datos del producto seleccionado.
     */
    private fun editProduct(product: Product) {
        val intent = Intent(this, EditProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", product.id)
        intent.putExtra("PRODUCT_NAME", product.name)
        intent.putExtra("PRODUCT_CATEGORY", product.category)
        intent.putExtra("PRODUCT_QUANTITY", product.quantity)
        intent.putExtra("PRODUCT_PRICE", product.price)
        startActivity(intent)
    }

    /**
     * Incrementa la cantidad de un producto en 1.
     */
    private fun addProductQuantity(product: Product) {
        val newQuantity = product.quantity + 1
        firestore.collection("products").document(product.id)
            .update("quantity", newQuantity)
            .addOnSuccessListener {
                // El listener de snapshots actualizará la interfaz automáticamente
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar la cantidad: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Decrementa la cantidad de un producto en 1, siempre y cuando la cantidad sea mayor que 0.
     */
    private fun removeProductQuantity(product: Product) {
        if (product.quantity > 0) {
            val newQuantity = product.quantity - 1
            firestore.collection("products").document(product.id)
                .update("quantity", newQuantity)
                .addOnSuccessListener {
                    // El listener de snapshots actualizará la interfaz automáticamente
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar la cantidad: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    /**
     * Configura la vista de navegación inferior y su listener de selección de elementos.
     */
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.navigation_search -> {
                    Toast.makeText(this, "Función de búsqueda próximamente", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_add -> {
                    startActivity(Intent(this, AddProductActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}