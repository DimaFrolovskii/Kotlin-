package com.example.projectf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projectf.data.local.dao.ConsumedFoodDao
import com.example.projectf.data.local.dao.FoodDao
import com.example.projectf.data.local.entity.ConsumedFoodEntity
import com.example.projectf.data.local.entity.FoodEntity

@Database(entities = [FoodEntity::class, ConsumedFoodEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val foodDao: FoodDao
    abstract val consumedFoodDao: ConsumedFoodDao
}