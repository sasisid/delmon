package com.app.delmon.Model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable
import java.util.ArrayList

class CommonResponse : Serializable {

    @SerializedName("error")
    var error: Boolean? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("message")
    var message: String? = null

   @SerializedName("massage")
    var massage: String? = null

    @SerializedName("otp")
    var otp: String? = null




}


