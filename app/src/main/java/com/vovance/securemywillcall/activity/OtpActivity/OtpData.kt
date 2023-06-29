package com.vovance.securemywillcall.activity.OtpActivity

import com.google.gson.annotations.SerializedName

data class OtpData(
    @SerializedName("email_mobile") var emailMobile: String? = null,
    @SerializedName("is_login") var isLogin: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("otp") var otp: String? = null,
)