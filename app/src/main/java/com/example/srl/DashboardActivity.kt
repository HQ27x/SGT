package com.example.srl

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    // Servicios de Firebase
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // Elementos de la interfaz de usuario
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productAdapter: DashboardProductAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var totalUnitsText: TextView
    private lateinit var totalProductsText: TextView
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var contentScrollView: ScrollView
    private lateinit var pieChart: PieChart
    private lateinit var searchEditText: EditText
    private lateinit var scanBarcodeButton: ImageButton

    // Listas de datos
    private val productList = mutableListOf<Product>() // Lista completa de productos de Firestore
    private val filteredProductList = mutableListOf<Product>() // Lista de productos a mostrar después de filtrar

    // Lanzador para la actividad de escaneo de código de barras
    private val scanBarcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scannedBarcode = result.data?.getStringExtra("SCANNED_BARCODE")
            if (!scannedBarcode.isNullOrEmpty()) {
                searchEditText.setText(scannedBarcode)
            }
        }
    }

    /**
     * Esta función se llama cuando la actividad se crea por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Inicializar servicios de Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inicializar elementos de la interfaz de usuario
        totalUnitsText = findViewById(R.id.total_units_text)
        totalProductsText = findViewById(R.id.total_products_text)
        loadingSpinner = findViewById(R.id.loading_spinner)
        contentScrollView = findViewById(R.id.content_scroll_view)
        pieChart = findViewById(R.id.category_pie_chart)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        searchEditText = findViewById(R.id.search_edit_text)
        scanBarcodeButton = findViewById(R.id.scan_barcode_button)

        // Configurar el icono de la hamburguesa para abrir el menú lateral
        val menuIcon: ImageView = findViewById(R.id.menu_icon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Configurar el icono de perfil para abrir la actividad de perfil
        val profileIcon: ImageView = findViewById(R.id.profile_icon)
        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Configurar el listener de selección de elementos del menú lateral
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_manage_categories -> {
                    startActivity(Intent(this, ManageCategoriesActivity::class.java))
                }
                R.id.nav_logout -> {
                    logoutUser()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Configurar el RecyclerView
        productsRecyclerView = findViewById(R.id.dashboard_products_recycler_view)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = DashboardProductAdapter(filteredProductList)
        productsRecyclerView.adapter = productAdapter

        // Configurar el botón para navegar a ManageProductsActivity
        val manageProductsButton: Button = findViewById(R.id.button_manage_products)
        manageProductsButton.setOnClickListener {
            val intent = Intent(this, ManageProductsActivity::class.java)
            startActivity(intent)
        }

        // Configurar la barra de búsqueda para filtrar productos en tiempo real
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Configurar el botón de escaneo
        scanBarcodeButton.setOnClickListener {
            val intent = Intent(this, ScanBarcodeActivity::class.java)
            scanBarcodeLauncher.launch(intent)
        }

        // Configurar el gráfico de pastel
        setupPieChart()

        // Configurar la navegación inferior
        setupBottomNavigation()

        // Obtener los productos de Firestore
        fetchProducts()
    }

    /**
     * Configura el estilo inicial del gráfico de pastel.
     */
    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Categorías"
        pieChart.setCenterTextSize(16f)
        pieChart.description.isEnabled = false
        
        val legend = pieChart.legend
        legend.isEnabled = false // Ocultamos la leyenda para que se vea más limpio
    }

    /**
     * Carga los datos en el gráfico de pastel basándose en la lista de productos.
     */
    private fun loadPieChartData() {
        val categoryMap = HashMap<String, Int>()

        // Contar productos por categoría
        for (product in productList) {
            val category = if (product.category.isNotEmpty()) product.category else "Sin Categoría"
            categoryMap[category] = categoryMap.getOrDefault(category, 0) + 1
        }

        val entries = ArrayList<PieEntry>()
        for ((category, count) in categoryMap) {
            entries.add(PieEntry(count.toFloat(), category))
        }

        val colors = ArrayList<Int>()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataSet = PieDataSet(entries, "Categorías")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate() // Refrescar el gráfico
        pieChart.animateY(1400) // Animación
    }

    /**
     * Cierra la sesión del usuario actual y navega a la pantalla de Login.
     */
    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Obtiene la lista de productos de Firestore en tiempo real.
     */
    private fun fetchProducts() {
        loadingSpinner.visibility = View.VISIBLE
        contentScrollView.visibility = View.GONE

        firestore.collection("products")
            .addSnapshotListener { snapshots, e ->
                loadingSpinner.visibility = View.GONE
                contentScrollView.visibility = View.VISIBLE

                if (e != null) {
                    Log.w("DashboardActivity", "Error al obtener los documentos: ", e)
                    Toast.makeText(this, "Error al cargar los productos", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                productList.clear()
                for (document in snapshots!!) {
                    val product = document.toObject(Product::class.java)
                    productList.add(product)
                }
                updateDashboardStats()
                loadPieChartData() // Actualizar el gráfico con los nuevos datos
                filterProducts("") // Mostrar todos los productos inicialmente
            }
    }

    /**
     * Calcula y actualiza las estadísticas del dashboard (total de productos y total de unidades).
     */
    private fun updateDashboardStats() {
        val totalProducts = productList.size
        val totalUnits = productList.sumOf { it.quantity }

        totalProductsText.text = totalProducts.toString()
        totalUnitsText.text = totalUnits.toString()
    }

    /**
     * Filtra la lista de productos según una consulta de búsqueda y actualiza el RecyclerView.
     */
    private fun filterProducts(query: String) {
        filteredProductList.clear()
        if (query.isEmpty()) {
            filteredProductList.addAll(productList)
        } else {
            for (product in productList) {
                if (product.name.contains(query, ignoreCase = true) || product.barcode == query) {
                    filteredProductList.add(product)
                }
            }
        }
        productAdapter.notifyDataSetChanged()
    }

    /**
     * Configura la vista de navegación inferior y su listener de selección de elementos.
     */
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Ya estamos en el dashboard, no hacer nada
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