package com.example.projectf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projectf.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    // Получить все продукты
    @Query("SELECT * FROM food_table ORDER BY name ASC")
    fun getAllFoods(): Flow<List<FoodEntity>>

    // Поиск по закэшированным данным
    @Query("SELECT * FROM food_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchFoods(searchQuery: String): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food_table WHERE id = :foodId")
    fun getFoodById(foodId: String): Flow<FoodEntity?>

    // Получить только избранные
    @Query("SELECT * FROM food_table WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteFoods(): Flow<List<FoodEntity>>

    // ИСПРАВЛЕНО: REPLACE вместо IGNORE — иначе повторные запросы не обновляют кэш
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<FoodEntity>)

    // Переключить статус избранного
    @Query("UPDATE food_table SET isFavorite = :isFavorite WHERE id = :foodId")
    suspend fun updateFavoriteStatus(foodId: String, isFavorite: Boolean)

    // Очистка старого кэша (только не избранных)
    @Query("DELETE FROM food_table WHERE isFavorite = 0")
    suspend fun clearNonFavoriteFoods()
}