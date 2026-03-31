package com.app.delmon.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.activity.MainActivity
import com.app.delmon.databinding.FragmentSettingsBinding
import com.app.delmon.utils.Constants
import com.app.delmon.utils.LanguageManager
import com.app.delmon.utils.UiUtils
import java.util.*




/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {



    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sharedHelper= SharedHelper(requireContext())
        MainActivity.languageSelected = sharedHelper.language

        if(sharedHelper.language == LanguageManager.DEFAULT_LANGUAGE) {
            binding.arabicCheck.setImageResource(R.drawable.icon_awesome_check_circle__selected);
            binding.englishCheck.setImageResource(R.drawable.icon_awesome_check_circle_unselected);
            requireActivity().window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            binding.arabicCheck.setImageResource(R.drawable.icon_awesome_check_circle_unselected);
            binding.englishCheck.setImageResource(R.drawable.icon_awesome_check_circle__selected);
            requireActivity().window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        
        binding.apply {
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            mainLayout.isClickable = false
            mainLayout.isEnabled = false

            notification.setOnClickListener {
                notificationFrame.visibility = View.VISIBLE
                languageFrame.visibility = View.GONE
            }

            proceedBtn.setOnClickListener {
                languageFrame.visibility = View.GONE
                setLocale(MainActivity.languageSelected)
//                setLocale(sharedHelper.language)
            }
            notifyProceedBtn.setOnClickListener {
                notificationFrame.visibility = View.GONE
            }
            language.setOnClickListener {
                notificationFrame.visibility = View.GONE
                languageFrame.visibility = View.VISIBLE
            }
            arabicCard.setOnClickListener{
                sharedHelper.language = LanguageManager.DEFAULT_LANGUAGE
                MainActivity.languageSelected = LanguageManager.DEFAULT_LANGUAGE
                changeLanguage(LanguageManager.DEFAULT_LANGUAGE)
                arabicCheck.setImageResource(R.drawable.icon_awesome_check_circle__selected);
                englishCheck.setImageResource(R.drawable.icon_awesome_check_circle_unselected);

            }
            
            englishCard.setOnClickListener {
                sharedHelper.language = LanguageManager.LANGUAGE_ENGLISH
                MainActivity.languageSelected = LanguageManager.LANGUAGE_ENGLISH
                arabicCheck.setImageResource(R.drawable.icon_awesome_check_circle_unselected)
                englishCheck.setImageResource(R.drawable.icon_awesome_check_circle__selected)
                changeLanguage(LanguageManager.LANGUAGE_ENGLISH)
            }
            
            aboutUs.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("ptitle", "About US")
                bundle.putString("url", ProfileFragment.settingData.about)
                bundle.putString("pcontent", resources.getString(R.string.about_content))
                findNavController().navigate(R.id.action_settings_fragment_to_webAppsFragment,bundle)
            }
            terms.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("ptitle", resources.getString(R.string.terms_condition))
                bundle.putString("url", ProfileFragment.settingData.termsAndCondition)
                bundle.putString("pcontent", resources.getString(R.string.about_content))
                findNavController().navigate(R.id.action_settings_fragment_to_webAppsFragment,bundle)
            }
            privacy.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("ptitle", resources.getString(R.string.privacy_policy))
                bundle.putString("url", ProfileFragment.settingData.about)
                bundle.putString("pcontent", resources.getString(R.string.about_content))
                findNavController().navigate(R.id.action_settings_fragment_to_webAppsFragment,bundle)
            }


        }

        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setLocale(lang: String) {
        val language = Locale(lang)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = language
        res.updateConfiguration(conf, dm)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            res.configuration.setLayoutDirection(language)
            (if (lang == LanguageManager.DEFAULT_LANGUAGE) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }).also { requireActivity().window.decorView.layoutDirection = it }
        }
        findNavController().navigate(R.id.action_settings_fragment_self)
    }
    private fun changeLanguage(languageCode: String) {
        val code = LanguageManager.normalizeLanguageCode(languageCode)
        Constants.User.language = code
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
        setLocale(code)
        LanguageManager.changeLanguage(requireActivity(), code)
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        LanguageManager.setLayoutDirection(rootView, code)
        recreate(requireActivity())
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }
}