package com.app.smwc.Activity.EditProfile

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null
)

