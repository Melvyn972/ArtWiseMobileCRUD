package com.example.artwisemobilecrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.androidmobilecrud.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var productList: ListView
    private lateinit var addProductButton: Button
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productList = findViewById(R.id.product_list)
        addProductButton = findViewById(R.id.add_product_button)

        productAdapter = ProductAdapter(this, ArrayList())
        productList.adapter = productAdapter

        fetchProducts()

        addProductButton.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    private fun fetchProducts() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getProducts()
            Log.d("com.example.androidmobilecrud", "fetchProducts: $response")
            if (response.isSuccessful) {
                productAdapter.clear()
                productAdapter.addAll(response.body()!!)
            } else {
                Toast.makeText(this@MainActivity, "Failed to fetch products", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}