package com.joshayoung.lazypizza.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> List<T>.toFlow(): Flow<T> =
    flow {
        for (item in this@toFlow) {
            emit(item)
        }
    }

fun <T> List<T>.toFlowList(): Flow<List<T>> =
    flow {
        emit(this@toFlowList)
    }