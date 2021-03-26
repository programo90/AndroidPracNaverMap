package com.programo90.promaps

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverReverseGeocodingAPI {
    @GET("asd")
    fun getAddr(
            @Header("X-NCP-APIGW-API-KEY-ID") clientId: String
            ,@Header("X-NCP-APIGW-API-KEY") clientSecret: String
            ,@Query("coords") coords: String
            ,@Query("orders") orders: String
            ,@Query("output") output: String = "json"
    ): Call<ReverseAddr>
}