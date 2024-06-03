package com.example.artwisemobilecrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilecrud.R
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException

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
        val product = Product(
            intent.getIntExtra("product_id", 0),
            intent.getStringExtra("product_name")!!,
            intent.getStringExtra("product_content")!!,
            intent.getStringExtra("product_description")!!,
            intent.getStringExtra("product_images")!!,
            intent.getStringExtra("product_price")!!,
            intent.getStringExtra("product_category")!!
        )

        productName.text = product.name
        productDescription.text = product.description
        productContent.text = product.content

        val imageUrl = product.images
        val fullImageUrl = "https://artwize-d28899f059fc.herokuapp.com/uploads/images/$imageUrl"
        Log.d("ProductDetailsActivity", "imageUrl: $imageUrl")
        if (imageUrl.isNotEmpty()) {
            Picasso.get().load(fullImageUrl).placeholder(R.drawable.placeholder).into(productImages)
        }
        productPrice.text = product.price
        productCategory.text = product.category

        val buttonEditProduct: Button = findViewById(R.id.button_edit_product)
        buttonEditProduct.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, EditProductActivity::class.java).apply {
                putExtra("product_id", product.id)
                putExtra("product_name", product.name)
                putExtra("product_description", product.description)
                putExtra("product_content", product.content)
                putExtra("product_images", product.images)
                putExtra("product_price", product.price)
                putExtra("product_category", product.category)
            }
            startActivity(intent)
        }
        val buttonDeleteProduct: Button = findViewById(R.id.button_delete_product)
        buttonDeleteProduct.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmation de suppression")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce produit ?")
                .setPositiveButton("Oui") { _, _ ->
                    val apiController = ApiController(this)
                    apiController.deleteProduct(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                Toast.makeText(this@ProductDetailsActivity, "Erreur lors de la suppression du produit : ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                runOnUiThread {
                                    Toast.makeText(this@ProductDetailsActivity, "Produit supprimé avec succès", Toast.LENGTH_SHORT).show()
                                }
                                finish()
                            } else {
                                runOnUiThread {
                                    Toast.makeText(this@ProductDetailsActivity, "Erreur lors de la suppression du produit : ${response.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }, product)
                }
                .setNegativeButton("Non", null)
                .show()
        }
    }
}