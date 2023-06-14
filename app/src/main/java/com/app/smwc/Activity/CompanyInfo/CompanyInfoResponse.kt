package com.app.smwc.Activity.CompanyInfo

import com.google.gson.annotations.SerializedName

data class CompanyInfoResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null
)

