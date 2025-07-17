package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentTypeofBinding
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.LoginViewModel

class TypeofAccountFragment : Fragment() {
    private lateinit var binding: FragmentTypeofBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTypeofBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.apply {
            normalUser.setOnClickListener {
                Constants.User.usertype = "USER"
                createUser(Constants.User.mobileno,Constants.User.usertype,"91","")
                findNavController().navigate(R.id.action_type_of_account_fragment_to_otp_fragment)
            }
            merchant.setOnClickListener {
                Constants.User.usertype = "MERCHANT"
                binding.inputLayout.root.visibility = View.VISIBLE
            }
            employees.setOnClickListener {
                Constants.User.usertype = "EMPLOYEE"
                binding.inputLayout.root.visibility = View.VISIBLE
            }
            inputLayout.proceedBtn.setOnClickListener {
                if (inputLayout.numberInput.text.isNullOrEmpty()){
                   UiUtils.showToast(requireContext(),requireContext().resources.getString(R.string.please_add_details),true)
                }else{
                    createUser(Constants.User.mobileno,Constants.User.usertype,"91",inputLayout.numberInput.text.toString())
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun createUser(mobileno: String, usertype: String, cCode: String, crNumber: String) {
        DialogUtils.showLoader(requireContext())
        loginViewModel.createUser(requireContext(),mobileno,usertype,cCode,crNumber)
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
//                                data
                                Constants.User.otp = it.otp.toString()
                                findNavController().navigate(R.id.action_type_of_account_fragment_to_otp_fragment)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

}