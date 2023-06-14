package com.app.smwc.Activity.CompanyInfo

import com.google.gson.annotations.SerializedName

data class CompanyData(
    @SerializedName("address") var address: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("zipcode") var zipcode: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("email") var email: String? = null,
)