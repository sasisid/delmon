package com.app.delmon.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentLoginBinding
import com.app.delmon.interfaces.SingleTapListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.apply {
            loginBtn.setOnClickListener {
            var mobile = binding.mobileNum.text.toString()
                if (mobile.isNullOrEmpty()){
                    UiUtils.showToast(requireContext(),requireContext().resources.getString(R.string.mobileno_empty),true)
                }
//                else if (isValidMobile(mobile)){
//                    UiUtils.showToast(requireContext(),requireContext().resources.getString(R.string.invalid_mobile_no),true)
//                }
                else{
                    DialogUtils.showLoader(requireContext())
                    checkUser(mobile)
                }
//                findNavController().navigate(R.id.action_loginFragment_to_otpFragment)
            }


            skip.setOnClickListener {
                findNavController().popBackStack(R.id.home_fragment,false)
            }

        }
        // Inflate the layout for this fragment
        return binding.root
    }

    fun checkUser(mobile: String) {
        loginViewModel.checkUser(requireContext(),mobile,"+966")
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
                                // Todo -
//                                UiUtils.showToast(requireContext(),"Your OTP Is "+it.otp.toString(),true)
                                Log.e("appSample", "OTP: ${it.otp.toString()}")
                                if (data.isRegistered!!){
                                    if (data?.isApprove == 0){
                                        DialogUtils.showAlert(requireContext(),object :
                                            SingleTapListener {
                                            override fun singleTap() {
                                                findNavController().navigate(R.id.action_login_fragment_self)
                                            }

                                        },requireContext().resources.getString(R.string.profile_under_review))
                                    }else {
                                        Constants.User.mobileno = mobile
                                        Constants.User.otp = data.otp.toString()
                                        findNavController().navigate(R.id.action_loginFragment_to_otpFragment)
                                    }
                                }else{
                                    Constants.User.mobileno = mobile
                                    findNavController().navigate(R.id.action_login_fragment_to_type_of_account_fragment)
                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}