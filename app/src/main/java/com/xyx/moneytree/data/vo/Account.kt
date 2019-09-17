package com.xyx.moneytree.vo


import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("currency")
    val currency: String,
    @SerializedName("current_balance")
    val currentBalance: Double,
    @SerializedName("current_balance_in_base")
    val currentBalanceInBase: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("institution")
    val institution: String,
    @SerializedName("name")
    val name: String
)