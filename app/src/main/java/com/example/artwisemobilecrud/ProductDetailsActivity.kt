package com.example.artwisemobilecrud

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilecrud.R
import com.squareup.picasso.Picasso

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val productName = findViewById<TextView>(R.id.product_name)
        val productDescription = findViewById<TextView>(R.id.product_description)
        val productContent = findViewById<TextView>(R.id.product_content)
        val productImages = findViewById<ImageView>(R.id.product_images)
        val productPrice = findViewById<TextView>(R.id.product_price)
        val productCategory = findViewById<TextView>(R.id.product_category)

        val intent = intent
        productName.text = intent.getStringExtra("product_name")
        productDescription.text = intent.getStringExtra("product_description")
        productContent.text = intent.getStringExtra("product_content")

        val imageUrl = intent.getStringExtra("product_images")
        val fullImageUrl = "https://artwize-d28899f059fc.herokuapp.com/uploads/images/$imageUrl"
        Log.d("ProductDetailsActivity", "imageUrl: $imageUrl")
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Picasso.get().load(fullImageUrl).placeholder(R.drawable.placeholder).into(productImages)
        }
        productPrice.text = intent.getStringExtra("product_price")
        productCategory.text = intent.getStringExtra("product_category")
    }
}
