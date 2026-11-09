package com.electrofire.playpkm.Data.LocalData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abilities")
data class AbilityEntity(
    @PrimaryKey val nombre: String
)
