package com.electrofire.playpkm.Data.NetworkData

import com.electrofire.playpkm.Data.Repository.PokemonApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object PokemonModule{

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiPokemon(retrofit: Retrofit):
            ApiPokemon = retrofit.create(ApiPokemon::class.java)

    @Provides
    @Singleton
    fun providePokemonRepository(api: ApiPokemon):
            PokemonApiRepository = PokemonApiRepository(api)

}