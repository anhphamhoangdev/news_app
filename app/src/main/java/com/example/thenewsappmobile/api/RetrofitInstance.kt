package com.example.thenewsappmobile.api

import com.example.thenewsappmobile.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
                        Person Responsible for this class : PHAM HOANG ANH - 21110753
*/


class RetrofitInstance {
    companion object{

        // Purpose : Defines a retrofit object as a singleton using the lazy delegate

        // In detail : prepares a Retrofit object to be able to send HTTP requests to Base URL,
        // using Gson for JSON conversion, and logging all HTTP requests and responses
        // with the level of detail including the headers and bodies

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        // Purpose : Create an API interface using the configured Retrofit instance
        // In detail : api object can be used to call the API methods defined in the NewsAPI interface,
        // which will send HTTP requests to the corresponding endpoints using the configured Retrofit instance.
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}