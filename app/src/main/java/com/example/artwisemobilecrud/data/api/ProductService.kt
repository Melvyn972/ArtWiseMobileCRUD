package com.example.artwisemobilecrud.data.api

import com.example.artwisemobilecrud.data.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}