package com.electrofire.playpkm.Data.NetworkData

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiPokemon {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): PokemonResponse

    @GET("pokemon")
    suspend fun getAllPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET
    suspend fun getPokemonByUrl(@Url url: String): PokemonResponse

    @GET
    suspend fun getAbilityByUrl(@Url url: String): AbilityResponse

    @GET("ability")
    suspend fun getAllAbilities(@Query("limit") limit: Int = 1000): AbilityListResponse

    @GET("ability/{abilityName}")
    suspend fun getPokemonByAbility(@Path("abilityName") abilityName: String): ListPokemonAbilityResponse


}