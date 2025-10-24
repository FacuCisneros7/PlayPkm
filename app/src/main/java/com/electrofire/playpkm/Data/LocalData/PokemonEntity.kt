package com.electrofire.playpkm.Data.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val nombre: String
)