package com.example.artwisemobilecrud

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.androidmobilecrud.R
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {
    private var productId: Int = 0
    private lateinit var productName: TextView
    private lateinit var productSlug: TextView
    private lateinit var productContent: TextView
    private lateinit var productDescription: TextView
    private lateinit var productImages: ImageView
    private lateinit var productPrice: TextView
    private lateinit var productCategory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productId = intent.getIntExtra("product_id", 0)

        productName = findViewById(R.id.product_name)
        productSlug = findViewById(R.id.product_slug)
        productContent = findViewById(R.id.product_content)
        productDescription = findViewById(R.id.product_description)
        productImages = findViewById(R.id.product_images)
        productPrice = findViewById(R.id.product_price)
        productCategory = findViewById(R.id.product_category)

        fetchProductDetails()
    }

    private fun fetchProductDetails() {
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getProduct(productId)
            if (response.isSuccessful) {
                val product = response.body()
                productName.text = product?.name
                productSlug.text = product?.slug
                productContent.text = product?.content
                productDescription.text = product?.description
                productPrice.text = "Price: $ ${product?.price}"
                productCategory.text = "Category: ${product?.category}"
            } else {
                Toast.makeText(this@ProductDetailsActivity, "Failed to fetch product details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}