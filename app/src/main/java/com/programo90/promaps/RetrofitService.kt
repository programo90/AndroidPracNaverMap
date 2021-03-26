package com.programo90.promaps

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("geocode")
    fun getAddress(
            @Header("X-NCP-APIGW-API-KEY-ID") clientId: String
            ,@Header("X-NCP-APIGW-API-KEY") clientKey: String
            ,@Query("query") query: String
            ,@Query("count") count: Int
    ):Call<RetrofitGeo>
}