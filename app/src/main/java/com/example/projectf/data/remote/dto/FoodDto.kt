package com.example.projectf.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    @SerializedName("products")
    val products: List<FoodProductDto> = emptyList()
)

data class FoodProductDto(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("product_name")
    val name: String?,
    @SerializedName("nutriments")
    val nutrients: NutrientsDto?
)

data class NutrientsDto(
    @SerializedName("energy-kcal_100g")
    val calories: Double?,
    @SerializedName("proteins_100g")
    val proteins: Double?,
    @SerializedName("fat_100g")
    val fat: Double?,
    @SerializedName("carbohydrates_100g")
    val carbs: Double?
)