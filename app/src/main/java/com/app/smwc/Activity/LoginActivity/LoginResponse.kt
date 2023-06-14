package com.app.smwc.Activity.LoginActivity

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: LoginResponseData? = LoginResponseData()
)

data class LoginResponseData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email_mobile") var emailMobile: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("company_id") var companyId: Int? = null,
    @SerializedName("is_verify") var isVerify: Int? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("company_email") var companyEmail: String? = null,
    @SerializedName("company_mobile") var companyMobile: String? = null,
    @SerializedName("company_city") var companyCity: String? = null,
    @SerializedName("company_zipcode") var companyZipcode: String? = null,
    @SerializedName("company_address") var companyAddress: String? = null
)
