package com.example.artwisemobilecrud.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidmobilecrud.R
import com.example.artwisemobilecrud.data.model.Product

@Composable
fun AddProductScreen(navController: NavController) {
    val (name, setName) = remember { mutableStateOf("") }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (slug, setSlug) = remember { mutableStateOf("") }
    val (content, setContent) = remember { mutableStateOf("") }
    val (images, setImages) = remember { mutableStateOf("") }
    val (price, setPrice) = remember { mutableStateOf("") }
    val (category, setCategory) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.add_product_title))
        Spacer(modifier = Modifier.height(16.dp))

        // User inputs for the product's name, description, slug, content, images, price, and category
        // You can add other fields as needed

        Button(onClick = {
            // Create a Product object from the user-entered data
            val newProduct = Product(
                id = 0, // You can assign an ID or leave it blank depending on your API's logic
                name = name,
                slug = slug,
                content = content,
                description = description,
                images = images,
                price = price,
                category = category
            )
            // Call the method to create a product in the ViewModel
            // productViewModel.createProduct(newProduct)

            // Navigate to the product list screen after successful addition
            navController.popBackStack()
        }) {
            Text(text = stringResource(id = R.string.add_product_button))
        }
    }
}
