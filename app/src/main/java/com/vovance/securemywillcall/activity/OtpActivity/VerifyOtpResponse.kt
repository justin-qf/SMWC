package com.vovance.securemywillcall.activity.LoginActivity

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: LoginResponseData? = LoginResponseData()
)

data class VerifyOtpModelData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email_mobile") var emailMobile: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("company_id") var companyId: String? = null,
    @SerializedName("is_verify") var isVerify: String? = null,
    @SerializedName("token") var token: String? = null
)