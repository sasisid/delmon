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
import com.app.delmon.Model.SettingsResponses
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.FragmentProfileBinding
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.LoginViewModel


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedHelper: SharedHelper


    companion object{
         lateinit var settingData: SettingsResponses.Data

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        sharedHelper= SharedHelper(requireContext())

        if (Constants.User.isLoggedIn){
            getUser()
        }
        getSettings()
        binding.apply {
            if (Constants.User.isLoggedIn){
                afterLoginMain.visibility = View.VISIBLE
                afterLoginSub.visibility = View.VISIBLE
                beforeLoginLayout.visibility = View.GONE
            }else{
                afterLoginMain.visibility = View.GONE
                afterLoginSub.visibility = View.GONE
                beforeLoginLayout.visibility = View.VISIBLE
            }
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            loginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_login_fragment)
            }
            feedbackCard.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_feedback_fragment)
            }
            helpCard.setOnClickListener {
                var args = Bundle()
                args.putString("call","profile")
                args.putString("chat","profile")
                args.putString("mail","profile")
                findNavController().navigate(R.id.action_profile_fragment_to_helpsupport_fragment,args)
            }
            settingCard.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_settings_fragment)
            }
            edit.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_editProfileFragment)
            }
            loyaltyCard.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_loyaltyFragment)
            }
            walletCard.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_walletFragment)
            }
            addressCard.setOnClickListener {
                var args = Bundle()
                args.putString("from","profile")
                findNavController().navigate(R.id.action_profile_fragment_to_addressFragment,args)
            }
            favCard.setOnClickListener {
                findNavController().navigate(R.id.action_profile_fragment_to_favouritesFragment)
            }
            logoutCard.setOnClickListener {
                sharedHelper.loggedIn = false
                Constants.User.isLoggedIn = false
                sharedHelper.token = ""
                Constants.User.token = ""
                sharedHelper.mobileNumber = ""
                sharedHelper.name = ""
                findNavController().navigate(R.id.action_profile_fragment_to_login_fragment)
            }



        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getUser(){
        DialogUtils.showLoader(requireContext())
        loginViewModel.getUser(requireContext())
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
                                binding.name.text = data.userName
                                binding.mobileEmail.text = "${data.mobileNumber} . ${data.email} "
                                binding.textView13.text = data?.walletAmount.toString()!! + " BD"
                                binding.loyaltyPoint.text = data?.loyaltyPoint.toString()!!
                                Constants.User.mobileno = data?.mobileNumber!!
                                if (!data?.userName.isNullOrEmpty()){
                                    Constants.User.uname = data?.userName!!
                                }
                                if (!data?.email.isNullOrEmpty()){
                                    Constants.User.email = data?.email!!
                                }
                                Constants.User.usertype = data?.userType.toString()
                                sharedHelper.userType = data?.userType.toString()
                                Constants.User.Wallet = data?.walletAmount.toString()!!
                                Constants.User.loyalty = data?.loyaltyPoint.toString()!!
//                                Constants.User.uname = data?.userName!!
                                if (data?.userType.equals("EMPLOYEE") || data?.userType.equals("MERCHANT")){
                                    binding.walletCard.visibility = View.GONE
                                    binding.loyaltyCard.visibility = View.GONE
                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun getSettings(){
        DialogUtils.showLoader(requireContext())
        loginViewModel.getSettings(requireContext())
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.success?.let { error ->
                        if (!error) {
//                            it.message?.let { msg ->
//                                UiUtils.showSnack(binding.root, msg)
//                            }
                        } else {
                            settingData = it.data!!
                        }
                    }
                }
                DialogUtils.dismissLoader()
            }
    }


}