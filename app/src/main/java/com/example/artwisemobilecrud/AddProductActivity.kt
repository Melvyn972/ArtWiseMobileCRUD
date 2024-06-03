package com.example.artwisemobilecrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilecrud.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val addProductButton: Button = findViewById(R.id.add_product_button)
        addProductButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.product_name).text.toString()
            val content = findViewById<EditText>(R.id.product_content).text.toString()
            val description = findViewById<EditText>(R.id.product_description).text.toString()
            val price = findViewById<EditText>(R.id.product_price).text.toString()
            val category = findViewById<EditText>(R.id.product_category).text.toString()

            if (name.isEmpty() || content.isEmpty() || description.isEmpty() || price.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val product = Product(0, name, content, description, "", price, category)

            val apiController = ApiController(this)
            apiController.postProduct(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@AddProductActivity, "Erreur lors de la création du produit : ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@AddProductActivity, "Produit créé avec succès", Toast.LENGTH_SHORT).show()
                        }
                        finish()
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@AddProductActivity, "Erreur lors de la création du produit : ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, product)
        }
    }
}