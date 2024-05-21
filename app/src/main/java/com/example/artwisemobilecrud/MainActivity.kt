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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiController = ApiController(this)
        val listView: ListView = findViewById(R.id.product_list)

        apiController.checkApi(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        // Toast.makeText(this@MainActivity, "API is working", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "API is not working", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })


        apiController.getProduct(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val products = parseProducts(responseBody)
                        runOnUiThread {
                            listView.adapter = ProductAdapter(this@MainActivity, products)

                            Toast.makeText(this@MainActivity, responseBody, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "API Request Failed: ${response.code}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
                productJson.getString("price"),
                productJson.getString("images"),
                productJson.getString("category")
            )
            products.add(product)
        }
        return products
    }

}