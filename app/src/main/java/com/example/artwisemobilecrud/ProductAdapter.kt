package com.example.artwisemobilecrud

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.widget.TextView
import com.example.androidmobilecrud.R


class ProductAdapter(private val context: Context, private val products: MutableList<Product>) :
    ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.product_item, parent, false)

        view.findViewById<TextView>(R.id.product_name).text = product?.name
        view.findViewById<TextView>(R.id.product_price).text = "Price: $ ${product?.price}"
        view.findViewById<TextView>(R.id.product_category).text = "Category: ${product?.category}"
        view.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("product_id", product?.id)
            context.startActivity(intent)
        }

        return view
    }
}