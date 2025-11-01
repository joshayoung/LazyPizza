package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity

@Dao
interface ToppingDao {
    @Upsert
    suspend fun upsertTopping(toppingEntity: ToppingEntity)

    @Query("SELECT * FROM topping")
    suspend fun getAllToppings(): List<ToppingEntity>
}