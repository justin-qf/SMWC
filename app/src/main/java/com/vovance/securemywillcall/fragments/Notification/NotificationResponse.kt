package com.vovance.securemywillcall.fragments.Notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<NotificationData> = arrayListOf()
)

data class NotificationData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("notification") var notification: String? = null,
    @SerializedName("notification_type") var notificationType: String? = null,
    @SerializedName("token_id") var tokenId: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("is_read") var isRead: Int? = null,
    @SerializedName("read_at") var readAt: String? = null,
    @SerializedName("is_guest") var isGuest: Int? = null,
    @SerializedName("created_date") var createdDate: String? = null,
    @SerializedName("created_time") var createdTime: String? = null

)