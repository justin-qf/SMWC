package com.vovance.securemywillcall.fragments.Home

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("data") var data: HomeData? = HomeData(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null
)

data class HomeData(

    @SerializedName("completed_order") var completedOrder: String? = null,
    @SerializedName("incompleted_order") var incompletedOrder: String? = null,
    @SerializedName("orders") var orders: ArrayList<Orders> = arrayListOf(),
    @SerializedName("pending_order") var pendingOrder: Int? = null,
    @SerializedName("queue_token") var queueToken: String? = null
)


data class Orders(
    @SerializedName("address") var address: String? = null,
    @SerializedName("company_title") var companyTitle: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("item_pickup_up") var itemPickupUp: Int? = null,
    @SerializedName("order_number") var orderNumber: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("status_name") var statusName: String? = null,
    @SerializedName("token_no") var tokenNo: String? = null,
    @SerializedName("total_amount") var totalAmount: Int? = null,
    @SerializedName("type") var type: Int? = null,

    )

data class OrdersType(
    var order: Orders? = null,
    var date: String = "",

    var type: Int = 0,

    )