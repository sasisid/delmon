package com.app.delmon.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.WalletResponse
import com.app.delmon.R
import com.app.delmon.adapter.DefaultAmountListAdapter
import com.app.delmon.adapter.PointsAdapter
import com.app.delmon.databinding.FragmentWalletBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WalletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentWalletBinding
    private lateinit var homeViewModel: HomeViewModel
    lateinit var mTestArray: Array<String>


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button event
            if (binding.addAmtLayout.visibility==View.VISIBLE) {
                binding.addAmtLayout.visibility = View.GONE
            }
            else{
                findNavController().popBackStack()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        requireActivity().apply {
            // Redirect system "Back" press to our dispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            points.text = Constants.User.Wallet+ " BD"
            addMoney.setOnClickListener {
                addAmtLayout.visibility = View.VISIBLE
            }
            addAmtLayout.setOnClickListener {

            }
            addAmtBtn.setOnClickListener {
                var amt = addAmtEditbox.text.toString()
                Log.d("amt", "onCreateView: $amt")
                if (amt.isNullOrEmpty()){
                    UiUtils.showToast(requireContext(),resources.getString(R.string.please_enter_amount),true)
                }else{
                    DialogUtils.showDialog(requireContext(),"","Are you Sure to Proceed",requireContext().resources.getString(R.string.ok),requireContext().resources.getString(R.string.cancel),object :
                        DialogCallBack {
                        override fun onPositiveClick(toString: String) {
                            val args = Bundle()
                            args.putString("from","wallet")
                            args.putString("amt", amt)
                            findNavController().navigate(R.id.action_walletFragment_to_paymentFragment,args)
                        }

                        override fun onNegativeClick() {
                        }

                    })
                }
            }
        }

        val stringArray: ArrayList<String> =  resources.getStringArray(R.array.default_amount).toList() as ArrayList<String>
        Log.d("stringArray", "onCreateView: ${stringArray[1].toString()}")
        amountRecyc(stringArray)

        // Inflate the layout for this fragment
        walletdata()
        return binding.root
    }
    fun walletdata(){
        DialogUtils.showLoader(requireContext())
        homeViewModel.getWalletList(requireContext()).observe(viewLifecycleOwner) {
            if (it.error!!) {
                Log.d("Error", "" + it.toString())

            } else {
                Log.d("fghj", "" + it.data!!.size)

                // UiUtils.showSnack(binding.root, it.message!!)
                walletRecyc(it.data)

            }
            DialogUtils.dismissLoader()
        }

    }

    fun addWallet(amount:Double){

    }

    private fun amountRecyc(data: ArrayList<String>) {
        binding.deafaultAmtRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.HORIZONTAL,false)
        binding.deafaultAmtRecyc.scheduleLayoutAnimation();
        binding.deafaultAmtRecyc.adapter = DefaultAmountListAdapter(requireContext(),data,object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                binding.addAmtEditbox.setText(data[position].toString())
            }
        })
    }
    private fun walletRecyc(data: List<WalletResponse.Data?>?) {
        binding.walletRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.walletRecyc.scheduleLayoutAnimation();
        binding.walletRecyc.adapter = PointsAdapter(requireContext(),data,object :
            OnClickListener {
            override fun onClickItem(position: Int) {
//                binding.weightCard.root.visibility = View.GONE
            }
        },"wallet")
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalletFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WalletFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}