package com.example.test.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("entities/getAllIds")
    suspend fun getAllIds(): EntityResponse

    @GET("object/{id}")
    suspend fun getObject(@Path("id") id: Int): ObjectResponse
}
