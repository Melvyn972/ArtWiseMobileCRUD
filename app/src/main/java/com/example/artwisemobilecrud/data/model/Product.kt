package com.example.artwisemobilecrud.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("content") val content: String,
    @SerializedName("description") val description: String,
    @SerializedName("images") val images: String,
    @SerializedName("price") val price: String,
    @SerializedName("category") val category: String
)