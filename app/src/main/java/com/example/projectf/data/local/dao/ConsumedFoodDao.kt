package com.example.projectf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projectf.data.local.entity.ConsumedFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsumedFood(food: ConsumedFoodEntity)

    @Query("SELECT SUM(calories) FROM consumed_food_table WHERE date >= :startOfDay")
    fun getTodayTotalCalories(startOfDay: Long): Flow<Int?>

    @Query("SELECT * FROM consumed_food_table WHERE date >= :startOfDay ORDER BY date DESC")
    fun getTodayConsumedFoods(startOfDay: Long): Flow<List<ConsumedFoodEntity>>
}