package com.example.artwisemobilecrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.androidmobilecrud.R
import kotlinx.coroutines.launch

class AddProductActivity : AppCompatActivity() {
    private lateinit var addProductButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var slugEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var imagesEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categoryEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        addProductButton = findViewById(R.id.add_product_button)
        nameEditText = findViewById(R.id.product_name)
        slugEditText = findViewById(R.id.product_slug)
        contentEditText = findViewById(R.id.product_content)
        descriptionEditText = findViewById(R.id.product_description)
        imagesEditText = findViewById(R.id.product_images)
        priceEditText = findViewById(R.id.product_price)
        categoryEditText = findViewById(R.id.product_category)

        addProductButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val slug = slugEditText.text.toString()
            val content = contentEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val images = imagesEditText.text.toString()
            val price = priceEditText.text.toString()
            val categoryText = categoryEditText.text.toString()
            val category = categoryText.toIntOrNull()?.toString() ?: "0"

            addProduct(name, slug, content, description, images, price, category)
        }
    }

    private fun addProduct(name: String, slug: String, content: String, description: String, images: String, price: String, category: String) {
        lifecycleScope.launch {
            val product = Product(0, name, slug, content, description, images, price, category.toInt())
            val response = RetrofitClient.apiService.createProduct(product)
            if (response.isSuccessful) {
                Toast.makeText(this@AddProductActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@AddProductActivity, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
