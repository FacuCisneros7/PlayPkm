package com.electrofire.playpkm.Data.NetworkData

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiPokemon {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id")id:Int): PokemonResponse

}