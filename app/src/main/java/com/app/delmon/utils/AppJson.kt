package com.app.delmon.utils

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * Shared Gson instances and safe JSON parsing for API callbacks.
 */
object AppJson {

    private class StringListAdapter : JsonDeserializer<List<String?>> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): List<String?> {
            return when {
                json == null || json.isJsonNull -> emptyList()
                json.isJsonArray -> json.asJsonArray.map { it.asString }
                json.isJsonPrimitive -> {
                    val value = json.asString
                    if (value.isBlank()) emptyList() else listOf(value)
                }
                else -> emptyList()
            }
        }
    }

    val defaultGson: Gson by lazy { Gson() }

    val productDetailGson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<List<String?>>() {}.type,
                StringListAdapter()
            )
            .create()
    }

    fun <T> parseOrError(
        jsonObject: JSONObject,
        clazz: Class<T>,
        gson: Gson = defaultGson
    ): Pair<T?, String?> {
        return try {
            Pair(gson.fromJson(jsonObject.toString(), clazz), null)
        } catch (e: Exception) {
            Pair(null, e.message ?: "Parse error")
        }
    }

    fun <T> applyToLiveData(
        jsonObject: JSONObject,
        liveData: MutableLiveData<T>,
        clazz: Class<T>,
        gson: Gson = defaultGson,
        onError: (String?) -> T
    ) {
        val (parsed, err) = parseOrError(jsonObject, clazz, gson)
        liveData.value = parsed ?: onError(err)
    }
}

/** Volley success callback helper — keeps parsing logic in one place. */
fun <T> MutableLiveData<T>.postFromJson(
    jsonObject: JSONObject,
    clazz: Class<T>,
    gson: Gson = AppJson.defaultGson,
    onErr: (String?) -> T
) {
    AppJson.applyToLiveData(jsonObject, this, clazz, gson, onErr)
}
