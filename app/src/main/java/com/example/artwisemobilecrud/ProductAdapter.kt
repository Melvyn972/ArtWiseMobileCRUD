package com.example.artwisemobilecrud

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.androidmobilecrud.R

class ProductAdapter(private val context: Context, private val products: List<Product>) : BaseAdapter() {
    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Any {
        return products[position]
    }

    override fun getItemId(position: Int): Long {
        return products[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)

        val product = products[position]
        val productName = view.findViewById<TextView>(R.id.product_name)
        val productDescription = view.findViewById<TextView>(R.id.product_description)
        val productContent = view.findViewById<TextView>(R.id.product_content)
        val productImages = view.findViewById<TextView>(R.id.product_images)
        val productPrice = view.findViewById<TextView>(R.id.product_price)
        val productCategory = view.findViewById<TextView>(R.id.product_category)

        productName.text = product.name
        productDescription.text = product.description
        productContent.text = product.content
        productImages.text = product.images
        productPrice.text = product.price
        productCategory.text = product.category

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
}
