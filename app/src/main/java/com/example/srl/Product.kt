package com.example.srl

data class Product(
    val name: String = "",
    val category: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val barcode: String = ""
) {
    var id: String = ""
}