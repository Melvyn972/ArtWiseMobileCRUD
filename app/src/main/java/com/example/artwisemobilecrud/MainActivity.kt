package com.example.artwisemobilecrud

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilecrud.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private val allProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiController = ApiController(this)

        checkApi(apiController)
        fetchAllProducts(apiController)

        val fabAddProduct: FloatingActionButton = findViewById(R.id.fab_add_product)
        fabAddProduct.setOnClickListener {
            val intent = Intent(this@MainActivity, AddProductActivity::class.java)
            startActivity(intent)
        }

        val searchView: SearchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter.filter(newText)
                return false
            }
        })
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

    private fun fetchAllProducts(apiController: ApiController) {
        var page = 1

        fun fetchPage(currentPage: Int) {
            fetchProduct(apiController, currentPage) { products, isLastPage ->
                allProducts.addAll(products)
                if (!isLastPage) {
                    fetchPage(currentPage + 1)
                } else {
                    runOnUiThread {
                        displayProducts(allProducts)
                    }
                }
            }
        }

        fetchPage(page)
    }

    private fun fetchProduct(apiController: ApiController, page: Int, callback: (List<Product>, Boolean) -> Unit) {
        apiController.getProduct(page, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showToast("Error: ${e.message}")
                callback(emptyList(), true)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val products = parseProducts(responseBody)
                        val isLastPage = products.isEmpty()
                        callback(products, isLastPage)
                    }
                } else {
                    showToast("API Request Failed: ${response.code}")
                    callback(emptyList(), true)
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
        productAdapter = ProductAdapter(this, products)
        listView.adapter = productAdapter
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
