package com.xyx.moneytree.data.vo

import com.google.gson.annotations.SerializedName
import java.util.*

data class Transaction(
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("date")
    val date: String,
    var calendar: Calendar?,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int
)