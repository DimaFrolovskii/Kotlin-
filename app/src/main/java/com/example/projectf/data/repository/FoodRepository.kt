package com.example.projectf.data.repository

import com.example.projectf.data.local.dao.ConsumedFoodDao
import com.example.projectf.data.local.dao.FoodDao
import com.example.projectf.data.local.entity.ConsumedFoodEntity
import com.example.projectf.data.local.entity.FoodEntity
import com.example.projectf.data.remote.FoodApiService
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val api: FoodApiService,
    private val foodDao: FoodDao,
    private val consumedFoodDao: ConsumedFoodDao
) {

    fun searchFoodsLocal(query: String): Flow<List<FoodEntity>> =
        foodDao.searchFoods(query)

    fun getFoodById(id: String): Flow<FoodEntity?> =
        foodDao.getFoodById(id)

    fun getFavoriteFoods(): Flow<List<FoodEntity>> =
        foodDao.getFavoriteFoods()

    suspend fun fetchAndCacheFoods(query: String) {
        val response = api.searchFood(query)

        val entities = response.products.mapNotNull { dto ->
            val id = dto.id?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val name = dto.name?.takeIf { it.isNotBlank() } ?: return@mapNotNull null

            FoodEntity(
                id = id,
                name = name,
                calories = dto.nutrients?.calories ?: 0.0,
                protein = dto.nutrients?.proteins ?: 0.0,
                fat = dto.nutrients?.fat ?: 0.0,
                carbs = dto.nutrients?.carbs ?: 0.0,
                isFavorite = false
            )
        }
        foodDao.insertFoods(entities)
    }

    suspend fun toggleFavorite(foodId: String, isFavorite: Boolean) {
        foodDao.updateFavoriteStatus(foodId, isFavorite)
    }

    suspend fun consumeFood(food: FoodEntity) {
        val consumedFood = ConsumedFoodEntity(
            foodId = food.id,
            name = food.name,
            calories = food.calories.toInt(),
            date = System.currentTimeMillis()
        )
        consumedFoodDao.insertConsumedFood(consumedFood)
    }

    fun getTodayTotalCalories(): Flow<Int?> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return consumedFoodDao.getTodayTotalCalories(calendar.timeInMillis)
    }
}