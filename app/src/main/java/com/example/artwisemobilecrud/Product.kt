package com.example.artwisemobilecrud

data class Product(
    val id: Int,
    val name: String,
    val slug: String,
    val content: String,
    val description: String,
    val images: String,
    val imageFile: String,
    val price: Int,
    val category: Int
)
