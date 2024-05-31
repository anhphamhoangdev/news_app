package com.example.thenewsappmobile.api

import com.example.thenewsappmobile.models.NewsResponse
import com.example.thenewsappmobile.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/

interface NewsAPI {


    // Purpose : fetch top headlines and
    // returns a Response<NewsResponse> object, which likely wraps the response data from the API in a NewsResponse data class.
    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = Constants.API_KEY
    ): Response<NewsResponse>



    // Purpose : search for news articles based on a search query
    // returns a Response<NewsResponse> object.
    @GET("/v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey : String = Constants.API_KEY,
    ): Response<NewsResponse>

}