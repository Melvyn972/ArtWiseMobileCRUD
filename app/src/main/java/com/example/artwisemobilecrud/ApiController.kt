package com.example.artwisemobilecrud

import okhttp3.*
import java.io.IOException
import android.content.Context
import com.example.androidmobilecrud.R
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


class ApiController(private val context: Context) {

    private val client = OkHttpClient()

    fun checkApi(callback: Callback) {
        val url = context.getString(R.string.url)
        sendRequest(url, emptyMap(), "GET", callback)
    }

    fun getProduct(page: Int?, callback: Callback) {
        val url =
            context.getString(R.string.url) + "/products" + if (page != null) "?page=$page" else ""
        sendRequest(url, emptyMap(), "GET", callback)
    }

    fun getProductById(callback: Callback, id: String) {
        val url = context.getString(R.string.url) + "/products/$id"
        sendRequest(url, emptyMap(), "GET", callback)
    }

    fun postProduct(callback: Callback, product: Product) {
        val url = context.getString(R.string.url) + "/products"
        val params = mapOf(
            "name" to product.name,
            "slug" to product.name.toLowerCase().replace(" ", "_"),
            "content" to product.content,
            "description" to product.description,
            "price" to product.price,
            "category" to product.category
        )
        sendRequest(url, params, "POST", callback)
    }

    fun deleteProduct(callback: Callback, product: Product) {
        val url = context.getString(R.string.url) + "/products/${product.id}"
        sendRequest(url, emptyMap(), "DELETE", callback)
    }

    fun putProduct(callback: Callback, product: Product) {
        val url = context.getString(R.string.url) + "/products/${product.id}"
        val params = mapOf(
            "name" to product.name,
            "slug" to product.name.toLowerCase().replace(" ", "_"),
            "content" to product.content,
            "description" to product.description,
            "price" to product.price,
            "category" to product.category
        )
        sendRequest(url, params, "PUT", callback)
    }

    fun getCategory(callback: Callback, page: Int?) {
        val url =
            context.getString(R.string.url) + "/categories" + if (page != null) "?page=$page" else ""
        sendRequest(url, emptyMap(), "GET", callback)
    }

    fun getCategoryById(callback: Callback, id: String) {
        val url = context.getString(R.string.url) + "/categories/$id"
        sendRequest(url, emptyMap(), "GET", callback)
    }

    private fun sendRequest(
        url: String,
        params: Map<String, String>,
        method: String,
        callback: Callback
    ) {

        val requestBuilder = Request.Builder().url(url)

        when (method) {
            "GET" -> {
                val httpUrlBuilder = url.toHttpUrlOrNull()?.newBuilder()
                params.forEach { (key, value) ->
                    httpUrlBuilder?.addQueryParameter(key, value)
                }
                val httpUrl = httpUrlBuilder?.build()
                if (httpUrl != null) {
                    requestBuilder.url(httpUrl)
                }
                requestBuilder.get()
            }

            "POST", "PUT", "PATCH" -> {
                val formBuilder = FormBody.Builder()
                params.forEach { (key, value) ->
                    formBuilder.add(key, value)
                }
                val formBody = formBuilder.build()
                when (method) {
                    "POST" -> requestBuilder.post(formBody)
                    "PUT" -> requestBuilder.put(formBody)
                    "PATCH" -> requestBuilder.patch(formBody)
                }
            }

            "DELETE" -> {
                requestBuilder.delete()
            }

            else -> {
                throw IllegalArgumentException("Method not supported")
            }
        }
        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call, e)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }
        })
    }
}