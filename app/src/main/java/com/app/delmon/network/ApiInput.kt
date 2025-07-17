package com.app.delmon.network

import android.content.Context
import org.json.JSONObject
import java.io.File

class ApiInput{

    var jsonObject: JSONObject? = null
    var url: String? = null
    var headers : MutableMap<String,String>? = null
    var file: File? = null
    var context: Context? = null
    var Content_Type: String? = null


}