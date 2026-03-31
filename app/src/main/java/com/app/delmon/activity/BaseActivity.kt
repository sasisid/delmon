package com.app.delmon.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.delmon.utils.LanguageManager

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.onAttach(newBase))
    }
}
