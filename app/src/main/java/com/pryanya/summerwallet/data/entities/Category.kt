package com.pryanya.summerwallet.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val prebuilt: Boolean = false,
    val type: Int
)