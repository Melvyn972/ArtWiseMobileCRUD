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
fun EditProductScreen(
    navController: NavController,
    product: Product // Le produit à éditer
) {
    val (name, setName) = remember { mutableStateOf(product.name) }
    val (description, setDescription) = remember { mutableStateOf(product.description) }
    val (slug, setSlug) = remember { mutableStateOf(product.slug) }
    val (content, setContent) = remember { mutableStateOf(product.content) }
    val (images, setImages) = remember { mutableStateOf(product.images) }
    val (price, setPrice) = remember { mutableStateOf(product.price) }
    val (category, setCategory) = remember { mutableStateOf(product.category) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.edit_product_title))
        Spacer(modifier = Modifier.height(16.dp))

        // Entrées de l'utilisateur pour le nom et la description du produit
        // Les champs sont préremplis avec les valeurs actuelles du produit

        Button(onClick = {
            // Mettre à jour les valeurs du produit avec les données entrées par l'utilisateur
            val updatedProduct = product.copy(
                id = 0, // You can assign an ID or leave it blank depending on your API's logic
                name = name,
                slug = slug,
                content = content,
                description = description,
                images = images,
                price = price,
                category = category
                // Mettez à jour d'autres champs du produit si nécessaire
            )
            // Appeler la méthode pour mettre à jour le produit dans le ViewModel
            // productViewModel.updateProduct(updatedProduct)

            // Naviguer vers l'écran de liste des produits après la mise à jour réussie
            navController.popBackStack()
        }) {
            Text(text = stringResource(id = R.string.edit_product_button))
        }
    }
}
