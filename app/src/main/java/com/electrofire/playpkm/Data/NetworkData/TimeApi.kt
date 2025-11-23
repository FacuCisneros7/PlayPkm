package com.electrofire.playpkm.Data.NetworkData

import retrofit2.Response
import retrofit2.http.GET

interface TimeApi {
    @GET("/")
    suspend fun getServerTime(): Response<Unit>
}