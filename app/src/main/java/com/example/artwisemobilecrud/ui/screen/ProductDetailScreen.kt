package com.example.artwisemobilecrud.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidmobilecrud.R
import com.example.artwisemobilecrud.data.model.Product

@Composable
fun ProductDetailScreen(
    navController: NavController,
    product: Product
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = stringResource(id = R.string.product_name_label))
        Text(text = product.name)

        Text(text = stringResource(id = R.string.product_description_label))
        Text(text = product.description)

        // Afficher d'autres détails du produit si nécessaire
    }
}
