package com.app.smwc.Activity.EditProfile

import com.google.gson.annotations.SerializedName

data class UpdateProfileData(

    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("email_mobile") var emailMobile: String? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("company_email") var companyEmail: String? = null,
    @SerializedName("company_mobile") var companyMobile: String? = null,
    @SerializedName("company_address") var companyAddress: String? = null,
    @SerializedName("company_city") var companyCity: String? = null,
    @SerializedName("company_zipcode") var companyZipcode: String? = null,
    @SerializedName("image") var image: String? = null,

    )