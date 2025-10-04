package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchItemsViewModel : ViewModel() {
    private val storage = FirebaseStorage.getInstance()

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images

    init {
        viewModelScope.launch {
            var i = fetchImageUrls()

            _images.value = i
        }
    }

    suspend fun fetchImageUrls(): List<String> {
        val storageReference = FirebaseStorage.getInstance().reference.child("pizzas/")
        val result: ListResult = storageReference.listAll().await()
        return result.items.map { it.downloadUrl.await().toString() }
    }
}
