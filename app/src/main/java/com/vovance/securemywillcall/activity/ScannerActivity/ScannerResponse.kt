package com.vovance.securemywillcall.activity.ScannerActivity

import com.google.gson.annotations.SerializedName

data class ScannerResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ScannerData? = ScannerData()
)

data class ScannerData(
    @SerializedName("token_no") var tokenNo: String? = null
)