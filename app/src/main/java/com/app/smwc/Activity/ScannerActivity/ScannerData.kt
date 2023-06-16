package com.app.smwc.Activity.ScannerActivity

import com.google.gson.annotations.SerializedName

data class ScannerResponseData(
    @SerializedName("qr_id") var qrId: String? = null,
)