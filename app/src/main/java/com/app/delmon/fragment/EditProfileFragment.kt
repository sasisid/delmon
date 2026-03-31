package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentEditProfileBinding
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.LoginViewModel


class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.apply {
            numberEdit.setText(Constants.User.mobileno)
            nameEdit.setText(Constants.User.uname)
            emailEdit.setText(Constants.User.email)
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            save.setOnClickListener {
                if (nameEdit.text.toString().isNullOrEmpty() || emailEdit.text.toString().isNullOrEmpty()){
                    UiUtils.showToast(requireContext(),requireContext().resources.getString(R.string.enter_all_field),false)
                }else{
                    DialogUtils.showLoader(requireContext())
                    updateUser(nameEdit.text.toString(),emailEdit.text.toString())
                }
            }

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun updateUser(name: String, email: String) {
        loginViewModel.updateUser(requireContext(),name,email,"")
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            it.data?.let { data ->
                                Constants.User.email = data.email!!
                                Constants.User.mobileno = data.mobileNumber!!
                                Constants.User.uname = data.userName!!
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
}