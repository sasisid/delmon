package com.app.delmon.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.ActivitySelectionBinding
import com.app.delmon.utils.Constants
import com.app.delmon.utils.LanguageManager
import com.app.delmon.utils.LottieLoader
import java.util.*

class SelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionBinding
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedHelper = SharedHelper(this)

        LanguageManager.changeLanguage(this,sharedHelper.language)


        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.poltryCard.setOnClickListener {
            Constants.User.apptype = 1
            sharedHelper.appType = "POULTRY"
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        binding.feedingCard.setOnClickListener {
            Constants.User.apptype = 2
            sharedHelper.appType = "FEEDING"
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }
}