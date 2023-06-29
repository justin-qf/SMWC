package com.vovance.omcsalesapp.Activity.SignInActivity

import com.google.gson.annotations.SerializedName

data class SendOtpResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("otp") var otp: String? = null
)