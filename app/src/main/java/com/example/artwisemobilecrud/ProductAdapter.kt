package com.example.artwisemobilecrud

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.example.androidmobilecrud.R
import com.squareup.picasso.Picasso
import java.util.Locale
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import com.google.gson.Gson
import okhttp3.Callback

class ProductAdapter(private val context: Context, private val products: List<Product>) :
    BaseAdapter(), Filterable {

    private var filteredProducts: List<Product> = products
    private val apiController = ApiController(context)

    override fun getCount(): Int {
        return filteredProducts.size
    }

    override fun getItem(position: Int): Any {
        return filteredProducts[position]
    }

    override fun getItemId(position: Int): Long {
        return filteredProducts[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.product_item, parent, false)

        val product = filteredProducts[position]
        val productName = view.findViewById<TextView>(R.id.product_name)
        val productDescription = view.findViewById<TextView>(R.id.product_description)
        val productContent = view.findViewById<TextView>(R.id.product_content)
        val productImages = view.findViewById<ImageView>(R.id.product_images)
        val productPrice = view.findViewById<TextView>(R.id.product_price)
        val productCategory = view.findViewById<TextView>(R.id.product_category)

        productName.text = product.name
        productDescription.text = product.description
        productContent.text = product.content

        val imageUrl = product.images
        val fullImageUrl = "https://artwize-d28899f059fc.herokuapp.com/uploads/images/$imageUrl"
        if (imageUrl.isNotEmpty()) {
            Picasso.get().load(fullImageUrl).placeholder(R.drawable.placeholder).into(productImages)
        }

        productPrice.text = product.price

        apiController.getCategoryById(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ProductAdapter", "API request failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val categoryJson = response.body?.string()
                    val gson = Gson()
                    val category = gson.fromJson(categoryJson, Category::class.java)
                    (context as Activity).runOnUiThread {
                        productCategory.text = category.name
                    }
                } else {
                    Log.e("ProductAdapter", "API request failed: ${response.code}")
                }
            }
        }, product.category.split("/").last().toInt())

        view.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra("product_id", product.id)
                putExtra("product_name", product.name)
                putExtra("product_description", product.description)
                putExtra("product_content", product.content)
                putExtra("product_images", product.images)
                putExtra("product_price", product.price)
                putExtra("product_category", product.category)
            }
            context.startActivity(intent)
        }

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrBlank()) {
                    products
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    products.filter { it.name.toLowerCase(Locale.ROOT).contains(filterPattern) }
                }

                return FilterResults().apply { values = filteredList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredProducts = results?.values as List<Product>
                notifyDataSetChanged()
            }
        }
    }
}