package com.joshayoung.lazypizza.core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topping")
data class ToppingEntity(
    @PrimaryKey(autoGenerate = true) val toppingId: Long = 0,
    val remoteId: String,
    val name: String,
    val price: String,
    val imageUrl: String? = null
)
