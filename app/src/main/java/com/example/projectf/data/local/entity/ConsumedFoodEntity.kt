package com.example.projectf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumed_food_table")
data class ConsumedFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodId: String,
    val name: String,
    val calories: Int,
    val date: Long // Timestamp
)