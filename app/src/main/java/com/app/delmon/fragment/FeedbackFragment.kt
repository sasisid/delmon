package com.app.delmon.fragment

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.app.delmon.R
import com.app.delmon.databinding.FragmentFeedbackBinding
import com.app.delmon.databinding.FragmentOtpBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.ProductViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FeedbackFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedbackFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

    binding.feedbackBtn.setOnClickListener {
        DialogUtils.showDialog(requireContext(),"","Are you Sure to Submit",requireContext().resources.getString(R.string.ok),requireContext().resources.getString(R.string.cancel),object :
            DialogCallBack {
            override fun onPositiveClick(toString: String) {
                DialogUtils.showLoader(requireContext())
                updateFeedback(binding.textInputEditText2.text.toString())
            }

            override fun onNegativeClick() {
            }

        })
    }

        val html = requireContext().resources.getString(R.string.if_you_have_complaint)
//        val html = "If you Want to Share Any Complaints,<u style='color:blue'>please Click Here</u>"
        binding.complaints.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.complaints.setOnClickListener {
            val uris = Uri.parse("https://dawajen.bh/contact#complain")
            val intents = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intents)
        }
        // Inflate the layout for this fragment
        return binding.root
    }


    private fun updateFeedback(comment:String){
        productViewModel.updateReport(requireContext(),comment)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.massage?.let { msg ->

                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            binding.textInputEditText2.text?.clear()
                            UiUtils.showSnack(binding.root, it.massage!!)

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


}