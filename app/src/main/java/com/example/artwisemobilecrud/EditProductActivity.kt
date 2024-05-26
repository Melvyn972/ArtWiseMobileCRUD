package com.example.artwisemobilecrud

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilecrud.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class EditProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_product_activity)

        val intent = intent
        val product = Product(
            intent.getIntExtra("product_id", 0),
            intent.getStringExtra("product_name")!!,
            intent.getStringExtra("product_content")!!,
            intent.getStringExtra("product_description")!!,
            intent.getStringExtra("product_images")!!,
            intent.getStringExtra("product_price")!!,
            intent.getStringExtra("product_category")!!
        )

        val editProductName: EditText = findViewById(R.id.edit_product_name)
        val editProductDescription: EditText = findViewById(R.id.edit_product_description)
        val editProductContent: EditText = findViewById(R.id.edit_product_content)
        val editProductPrice: EditText = findViewById(R.id.edit_product_price)
        val editProductCategory: EditText = findViewById(R.id.edit_product_category)

        editProductName.setText(product.name)
        editProductDescription.setText(product.description)
        editProductContent.setText(product.content)
        editProductPrice.setText(product.price)
        editProductCategory.setText(product.category)

        val buttonSaveProduct: Button = findViewById(R.id.button_save_product)
        buttonSaveProduct.setOnClickListener {
            val updatedProduct = Product(
                product.id,
                editProductName.text.toString(),
                editProductContent.text.toString(),
                editProductDescription.text.toString(),
                product.images,
                editProductPrice.text.toString(),
                editProductCategory.text.toString()
            )

            val apiController = ApiController(this)
            apiController.putProduct(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("EditProductActivity", "API request failed", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.d("EditProductActivity", "Product updated successfully")
                    } else {
                        Log.e("EditProductActivity", "API request failed: ${response.code}")
                    }
                }
            }, updatedProduct)
        }
    }
}