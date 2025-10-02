package com.joshayoung.lazypizza

import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class StorageService {
    public fun getStorage() {
        val storage = Firebase.storage
    }
}