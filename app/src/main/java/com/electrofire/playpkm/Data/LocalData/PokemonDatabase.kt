package com.electrofire.playpkm.Data.LocalData

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PokemonEntity::class, AbilityEntity::class], version = 2
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun abilityDao(): AbilityDao
}
