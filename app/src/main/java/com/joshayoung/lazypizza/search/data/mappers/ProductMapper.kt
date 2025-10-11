package com.joshayoung.lazypizza.search.data.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import kotlinx.serialization.json.Json

fun String.toProduct(): ProductUi = Json.decodeFromString(this)

fun Product.toJson(): String = Json.encodeToString(this)