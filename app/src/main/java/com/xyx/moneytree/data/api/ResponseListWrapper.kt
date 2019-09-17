package com.xyx.moneytree.data.api

import com.google.gson.annotations.SerializedName

data class ResponseListWrapper<T>(
    @SerializedName(value = "", alternate = ["accounts", "transactions"])
    val data: List<T>,
    @SerializedName("timestamp")
    val timestamp: Int
)