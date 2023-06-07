package com.app.smwc.Activity.LoginActivity

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("errorCode") var errorCode: Int? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("list") var list: ArrayList<List> = arrayListOf(),
    @SerializedName("status") var status: Int? = null
)

data class List(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("userName") var userName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("role") var role: Role? = Role(),
    @SerializedName("userRights" ) var userRights : String? = null,
    @SerializedName("updatedOn") var updatedOn: String? = null,
    @SerializedName("addedOn") var addedOn: String? = null
)

data class Role(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("permission") var permission: String? = null,
    @SerializedName("addedOn") var addedOn: String? = null
)
