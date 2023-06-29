package com.vovance.securemywillcall.activity.LoginActivity

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("otp") var otp: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("email_mobile") var emailMobile: String? = null,
)