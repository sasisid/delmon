package com.app.delmon.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.adapter.OnboardingAdapter
import com.app.delmon.databinding.ActivityOnBoardingBinding
import com.app.delmon.utils.LanguageManager
import me.relex.circleindicator.CircleIndicator

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var btnBack: TextView
    private lateinit var btnNext: TextView
    private lateinit var binding: ActivityOnBoardingBinding
    lateinit var indicator: CircleIndicator
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedHelper = SharedHelper(this)
        LanguageManager.changeLanguage(this,sharedHelper.language)

        mViewPager = binding.viewPager
        mViewPager.adapter = OnboardingAdapter(supportFragmentManager, this)
        indicator = binding.pageIndicatorView as CircleIndicator
        indicator.setViewPager(binding.viewPager)
        mViewPager.offscreenPageLimit = 1
        btnBack = binding.btnPreviousStep
        btnNext = binding.btnNextStep

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                if(position == 2){
                    btnNext.text = getText(R.string.finish)
                }else{
                    btnNext.text = getText(R.string.next)
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })

        btnNext.setOnClickListener {
            if (getItem(+1) > mViewPager.childCount-1) {
                val intent = Intent(this, SelectionActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            } else {
                mViewPager.setCurrentItem(getItem(+1), true)
            }
        }

        binding.btnSkip.setOnClickListener {
            val intent = Intent(this, SelectionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener {
            if (getItem(+1) == 1) {
                finish()
            } else {
                mViewPager.setCurrentItem(getItem(-1), true)
            }
        }
    }

    private fun getItem(i: Int): Int {
        return mViewPager.currentItem + i
    }
}