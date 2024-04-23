package com.example.artwisemobilecrud.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artwisemobilecrud.data.api.ProductService
import com.example.artwisemobilecrud.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val productService: ProductService) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val response = productService.getProducts()
            if (response.isSuccessful) {
                _products.value = response.body() ?: emptyList()
            } else {
                // Gérer l'erreur de récupération des produits
            }
        }
    }

    // Ajoutez d'autres méthodes pour créer, mettre à jour et supprimer des produits
}
