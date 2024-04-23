package com.example.artwisemobilecrud.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidmobilecrud.R
import com.example.artwisemobilecrud.data.model.Product
import com.example.artwisemobilecrud.ui.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val products by productViewModel.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.product_list_title)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("add_product")
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Product")
                    }
                }
            )
        }
    ) {
        ProductList(products = products, onItemClick = {
            navController.navigate("product_detail/${it.id}")
        })
    }
}

@Composable
fun ProductList(products: List<Product>, onItemClick: (Product) -> Unit) {
    LazyColumn {
        items(products) { product ->
            ProductListItem(product = product, onItemClick = onItemClick)
        }
    }
}

@Composable
fun ProductListItem(product: Product, onItemClick: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(product) },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.name)
            Text(text = product.description)
            // Afficher d'autres détails du produit si nécessaire
        }
    }
}
