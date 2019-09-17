package com.xyx.moneytree.vo


import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int
)