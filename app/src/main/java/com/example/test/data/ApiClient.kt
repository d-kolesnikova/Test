package com.example.test.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://demo3005513.mockable.io/api/v1/"

   val apiService: ApiService by lazy {
       val retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()

       retrofit.create(ApiService::class.java)
   }
}