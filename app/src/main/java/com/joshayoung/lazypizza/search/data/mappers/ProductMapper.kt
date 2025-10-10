package com.joshayoung.lazypizza.search.data.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import kotlinx.serialization.json.Json

fun String.toProduct(): Product = Json.decodeFromString(this)

fun Product.toJson(): String = Json.encodeToString(this)