package com.example.projectf.data.remote

import com.example.projectf.data.remote.dto.FoodSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {

    @GET("cgi/search.pl?action=process&json=1&page_size=20")
    suspend fun searchFood(
        @Query("search_terms") query: String
    ): FoodSearchResponse

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
    }
}