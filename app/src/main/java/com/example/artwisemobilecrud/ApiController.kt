package com.example.artwisemobilecrud

import okhttp3.*
import java.io.IOException
import android.content.Context
import android.util.Log
import com.example.androidmobilecrud.R
import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


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

    fun getCategoryById(callback: Callback, id: Int) {
        val url = context.getString(R.string.url) + "/categories/$id"
        sendRequest(url, emptyMap(), "GET", callback)
    }

    fun getAllCategories(callback: Callback) {
        val url = context.getString(R.string.url) + "/categories"
        sendRequest(url, emptyMap(), "GET", callback)
    }

    fun postProduct(callback: Callback, product: Product) {
        val url = context.getString(R.string.url) + "/products"
        val params = mapOf(
            "name" to product.name,
            "slug" to product.name.toLowerCase().replace(" ", "_"),
            "content" to product.content,
            "description" to product.description,
            "images" to product.images,
            "price" to product.price.toDouble(),
            "category" to product.category
        )
        sendRequest(url, params, "POST", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call, e)
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val responseJson = gson.toJson(response.body?.string())
                Log.d("onResponse", "Response: $responseJson")
                callback.onResponse(
                    call, Response.Builder()
                        .code(response.code)
                        .message(responseJson)
                        .protocol(response.protocol)
                        .request(call.request())
                        .build()
                )
            }
        })
    }

    fun deleteProduct(callback: Callback, product: Product) {
        val url = context.getString(R.string.url) + "/products/${product.id}"
        sendRequest(url, emptyMap(), "DELETE", callback)
    }

    fun putProduct(callback: Callback, product: Product) {
        val url = context.getString(R.string.url) + "/products/${product.id}"
        Log.d("putProduct", "url: $url")
        val params = mapOf(
            "name" to product.name,
            "slug" to product.name.toLowerCase().replace(" ", "_"),
            "content" to product.content,
            "description" to product.description,
            "images" to product.images,
            "price" to product.price.toDouble(),  // Ensure price is an integer
            "category" to product.category
        )

        sendRequest(url, params, "PUT", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call, e)
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val responseJson = gson.toJson(response.body?.string())
                Log.d("onResponse", "Response: $responseJson")
                callback.onResponse(
                    call, Response.Builder()
                        .code(response.code)
                        .message(responseJson)
                        .protocol(response.protocol)
                        .request(call.request())
                        .build()
                )
            }
        })
    }

    private fun sendRequest(
        url: String,
        params: Map<String, Any>,  // Changed type to Any to allow different types of values
        method: String,
        callback: Callback
    ) {

        val requestBuilder = Request.Builder().url(url)

        when (method) {
            "GET" -> {
                val httpUrlBuilder = url.toHttpUrlOrNull()?.newBuilder()
                params.forEach { (key, value) ->
                    httpUrlBuilder?.addQueryParameter(key, value.toString())
                }
                val httpUrl = httpUrlBuilder?.build()
                if (httpUrl != null) {
                    requestBuilder.url(httpUrl)
                }
                requestBuilder.get()
            }

            "POST", "PUT", "PATCH" -> {
                val gson = Gson()
                val jsonBody = gson.toJson(params)
                val body = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
                Log.d("jsonBody", jsonBody)
                when (method) {
                    "POST" -> requestBuilder.post(body)
                    "PUT" -> requestBuilder.put(body)
                    "PATCH" -> requestBuilder.patch(body)
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