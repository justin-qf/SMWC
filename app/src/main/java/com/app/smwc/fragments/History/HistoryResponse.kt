package com.app.smwc.fragments.History

import com.app.smwc.fragments.Home.Orders
import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: HistoryData? = HistoryData()
)

data class HistoryData(
    @SerializedName("orders") var orders: ArrayList<Orders> = arrayListOf()
)
