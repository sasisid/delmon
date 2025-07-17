package com.app.delmon.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.ActivitySelectionBinding
import com.app.delmon.utils.Constants
import com.app.delmon.utils.LottieLoader
import java.util.*

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionBinding
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocale("ar")

        sharedHelper= SharedHelper(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.poltryCard.setOnClickListener {
            setLocale("ar")
            Constants.User.apptype = 1
            sharedHelper.appType = "POULTRY"
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        binding.feedingCard.setOnClickListener {
            setLocale("hi")
            Constants.User.apptype = 2
            sharedHelper.appType = "FEEDING"
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setLocale(lang: String) {
        Log.d("TAG", "setLocale: $lang")
        val language = Locale(lang)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = language
        res.updateConfiguration(conf, dm)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            res.configuration.setLayoutDirection(language)
            (when (lang) {
                "ar" -> {
                    View.LAYOUT_DIRECTION_RTL
                }
                "hi" -> {
                    View.LAYOUT_DIRECTION_LTR
                }
                else -> {
                    View.LAYOUT_DIRECTION_LTR
                }
            }).also { window.decorView.layoutDirection = it }
        }
    }
}