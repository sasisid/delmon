package com.app.delmon.Session

import android.content.Context
import android.content.SharedPreferences

import com.google.gson.Gson


class SharedPref(private val context: Context) {
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    companion object {
        const val Cache = "Cache"
    }


    fun putKey(key: String, value: String) {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        editor!!.putString(key, value)
        editor!!.apply()
    }

    fun getKey(Key: String): String {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        return sharedPreferences!!.getString(Key, "").toString()
    }

    fun getKey(Key: String, defaultvalue: String): String? {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        return sharedPreferences!!.getString(Key, defaultvalue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        editor!!.putBoolean(key, value)
        editor!!.apply()
    }

    fun getBoolean(Key: String): Boolean {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean(Key, false)
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        editor!!.putInt(key, value)
        editor!!.apply()
    }

    fun getInt(key: String): Int {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        return sharedPreferences!!.getInt(key, 0)
    }



    fun putFloat(key: String, value: Float) {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        editor!!.putFloat(key, value)
        editor!!.apply()
    }

    fun getFloat(key: String): Float {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        return sharedPreferences!!.getFloat(key, 0.0F)
    }


    fun saveObjectToSharedPreference(serializedObjectKey: String, `object`: Any) {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences!!.edit()
        val gson = Gson()
        val serializedObject = gson.toJson(`object`)
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject)
        sharedPreferencesEditor.apply()
    }

    fun <GenericClass> getSavedObjectFromPreference(preferenceKey: String, classType: Class<GenericClass>): GenericClass? {
        sharedPreferences = context.getSharedPreferences(Cache, Context.MODE_PRIVATE)
        if (sharedPreferences!!.contains(preferenceKey)) {
            val gson = Gson()
            return gson.fromJson(sharedPreferences!!.getString(preferenceKey, ""), classType)
        }
        return null
    }

}