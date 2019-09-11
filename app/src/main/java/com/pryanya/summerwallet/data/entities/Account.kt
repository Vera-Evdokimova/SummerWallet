package com.pryanya.summerwallet.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var name: String,
    val type: Int,
    var sum: Double = 0.0,
    val isCountable: Boolean = true,
    var endDate: Long? = null,
    val targetSum: Double? = null
)