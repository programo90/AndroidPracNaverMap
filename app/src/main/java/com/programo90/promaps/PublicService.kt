package com.programo90.promaps

import retrofit2.Call
import retrofit2.http.GET

interface PublicService {
    @GET("asd")
    fun getReatimeBikeStationList(): Call<ArrayList<BikeStation>>
}