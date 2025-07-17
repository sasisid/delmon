package com.app.delmon.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.delmon.databinding.FragmentHelpSupportBinding
import com.app.delmon.utils.UiUtils


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelpSupportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HelpSupportFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHelpSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHelpSupportBinding.inflate(inflater, container, false)
        binding.apply {
            call.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)

                intent.data = Uri.parse("tel:" + ProfileFragment.settingData.call)
                requireActivity()!!.startActivity(intent)
            }
            chat.setOnClickListener {
                openWhatsAppChat()
            }
            mail.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:") // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(ProfileFragment.settingData.email))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback")
                    startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    UiUtils.showToast(
                        requireContext(),
                        "There are no email client installed on your device.",false
                    )
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }
    fun openWhatsAppChat() {
        val url = "https://api.whatsapp.com/send?phone=${ProfileFragment.settingData.whatsAppNumber}"
        try {
            requireActivity().packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
        } catch (e: PackageManager.NameNotFoundException) {
            requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelpSupportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HelpSupportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}