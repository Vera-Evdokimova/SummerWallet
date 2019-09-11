package com.pryanya.summerwallet.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Operation(

    val date: Long,
    val sum: Double,
    val type: Int,
    var toId: Long? = null,
    val accountId: Long = PrebuiltAccounts.CASH_ACCOUNT.id,
    var categoryId: Long = PrebuiltCategories.NO_CATEGORY_PROFIT.id,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var category: String,
    var account: String,
    var to: String? = null
)