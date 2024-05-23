package com.example.artwisemobilecrud

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilecrud.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiController = ApiController(this)

        checkApi(apiController)
        fetchProduct(apiController,page)
    }

    private fun checkApi(apiController: ApiController) {
        apiController.checkApi(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showToast("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        // API is working
                    } else {
                        showToast("API is not working")
                    }
                }
            }
        })
    }

    private fun fetchProduct(apiController: ApiController, page: Int) {
        apiController.getProduct(page, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showToast("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val products = parseProducts(responseBody)
                        runOnUiThread {
                            displayProducts(products)
                            showToast(responseBody)
                        }
                    }
                } else {
                    showToast("API Request Failed: ${response.code}")
                }
            }
        })
    }

    private fun parseProducts(responseBody: String): List<Product> {
        val jsonObject = JSONObject(responseBody)
        val jsonArray = jsonObject.getJSONArray("hydra:member")
        val products = mutableListOf<Product>()
        for (i in 0 until jsonArray.length()) {
            val productJson = jsonArray.getJSONObject(i)
            val product = Product(
                productJson.getInt("id"),
                productJson.getString("name"),
                productJson.getString("content"),
                productJson.getString("description"),
                productJson.getString("images"),
                productJson.getString("price"),
                productJson.getString("category")
            )
            products.add(product)
        }
        return products
    }

    private fun displayProducts(products: List<Product>) {
        val listView: ListView = findViewById(R.id.product_list)
        listView.adapter = ProductAdapter(this, products)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}