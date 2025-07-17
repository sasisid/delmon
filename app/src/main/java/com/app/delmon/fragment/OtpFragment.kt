package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.activity.MainActivity
import com.app.delmon.databinding.FragmentOtpBinding
import com.app.delmon.interfaces.SingleTapListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.LoginViewModel


class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedHelper= SharedHelper(requireContext())

        initOtpView()
        binding.apply {
            proceedBtn.setOnClickListener {

                val userotp = binding.otp1.text.toString() + binding.otp2.text.toString() + binding.otp3.text.toString() + binding.otp4.text.toString()
            if (!userotp.isNullOrEmpty() && userotp.length==4){
                DialogUtils.showLoader(requireContext())
                otpVerify(userotp)
            }else{
                UiUtils.showToast(requireContext(),requireContext().resources.getString(R.string.invalid_otp),false)
            }
            }

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initOtpView() {

        binding.otp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() != "") {
                    binding.otp2.isFocusable = true
                    binding.otp2.requestFocus()
                }
            }
        })
        binding.otp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() != "") {
                    binding.otp3.isFocusable = true
                    binding.otp3.requestFocus()
                } else {
                    binding.otp1.isFocusable = true
                    binding.otp1.requestFocus()
                }
            }
        })
        binding.otp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() != "") {
                    binding.otp4.isFocusable = true
                    binding.otp4.requestFocus()
                } else {
                    binding.otp2.isFocusable = true
                    binding.otp2.requestFocus()
                }
            }
        })
        binding.otp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() != "") {
                    binding.otp4.isFocusable = true
                    binding.otp4.requestFocus()
                } else {
                    binding.otp3.isFocusable = true
                    binding.otp3.requestFocus()
                }
            }
        })


        binding.otp2.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                if (binding.otp2.text.toString().isEmpty()) {
                    binding.otp1.setText("")
                    binding.otp1.requestFocus()
                }
            }
            false
        }
        binding.otp3.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                if (binding.otp3.text.toString().isEmpty()) {
                    binding.otp2.setText("")
                    binding.otp2.requestFocus()
                }
            }
            false
        }
        binding.otp4.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                if (binding.otp4.text.toString().isEmpty()) {
                    binding.otp3.setText("")
                    binding.otp3.requestFocus()
                }
            }
            false
        }


    }

    private fun otpVerify(userotp: String) {
        loginViewModel.verifyOtp(requireContext(),Constants.User.mobileno,"+966",userotp)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding!!.root, msg)
                            }
                        } else {
                            it.data?.let { data ->
                                if (data.isApprove == 1) {
                                    MainActivity.isloggedIn = true
                                    sharedHelper.loggedIn = true
                                    Constants.User.isLoggedIn = true
                                    Constants.User.token = data.token!!
                                    sharedHelper.token = data.token!!
                                    sharedHelper.id = data.id!!
                                    Constants.User.id = data.id!!
                                    Log.d("TAG", "otpVerify: ${sharedHelper.id}")
                                    findNavController().navigate(R.id.action_otpFragment_to_profileFragment)
                                }else{
                                    sharedHelper.loggedIn = false
                                    Constants.User.isLoggedIn = false
                                    Constants.User.token = ""
                                    DialogUtils.showAlert(requireContext(),object :
                                        SingleTapListener {
                                        override fun singleTap() {
                                            findNavController().navigate(R.id.action_otp_fragment_to_home_fragment)
                                        }

                                    },requireContext().resources.getString(R.string.profile_under_review))                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


}