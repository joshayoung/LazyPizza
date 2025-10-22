package com.joshayoung.lazypizza.cart.data

import com.joshayoung.lazypizza.cart.data.database.CartDao
import com.joshayoung.lazypizza.cart.domain.LocalDataSource
import com.joshayoung.lazypizza.cart.domain.models.CartEntity
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

class CartLocalDataSource(
    private var cartDao: CartDao
) : LocalDataSource {
    override fun getCartItems(): Flow<List<CartEntity>> = cartDao.getCartItems()

    override suspend fun upsertCart(note: CartEntity): Result<CartEntity, DataError.Local> {
        cartDao.upsertCartItem(note)

        // TODO: This return is probably not correct:
        return Result.Success(data = note)
    }
}