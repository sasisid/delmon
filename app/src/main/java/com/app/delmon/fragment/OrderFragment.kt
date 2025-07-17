package com.app.delmon.fragment

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.delmon.Model.CartResponseNew
import com.app.delmon.Model.OrderListResponse
import com.app.delmon.R
import com.app.delmon.adapter.OrderListAdapter
import com.app.delmon.adapter.OrderTitleAdapter
import com.app.delmon.databinding.FragmentOrderBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel




class OrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentOrderBinding
    private lateinit var homeViewModel: HomeViewModel
    var currentPos = 0
    var orderDataList:ArrayList<OrderListResponse.OrderData?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        if (!Constants.User.isLoggedIn){
            findNavController().navigate(R.id.action_order_fragment_to_login_fragment)
        }else{
            titleRecyc()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun titleRecyc(){
        binding.titleRecyc.layoutManager = GridLayoutManager(requireContext(),1, GridLayoutManager.HORIZONTAL,false)
        binding.titleRecyc.scheduleLayoutAnimation();
        binding.titleRecyc.adapter = OrderTitleAdapter(requireContext(),object : OnClickListener {
            override fun onClickItem(position: Int) {
                when (position) {
                    0 -> {
                        // PLACED
                        loadRecycler(0,"PLACED,APPROVE")
                    }
                    1->{
                        loadRecycler(position,"USERACCEPTED,OUTFORDELIVERY,DRIVERASSIGNED,UNAVAILABLE")
                    }
                    2 -> {
                        // USERACCEPTED,OUTFORDELIVERY
                        loadRecycler(position,"COMPLETED")
                    }
                    3 -> {
                        // USERREJECTED
                        loadRecycler(position,"USERREJECTED")
                    }
                }
            }
        },0)
        loadRecycler(0,"PLACED,APPROVE")
    }


    fun loadRecycler(position:Int,value:String) {
        DialogUtils.showLoader(requireContext())
        homeViewModel.getOrderList(requireContext(),value).observe(viewLifecycleOwner) {
            if (it.success!!) {
                Log.d("fghj", "" + it.data!!.size)
                if (it.data!!.isNotEmpty()) {
                    orderDataList = it.data as ArrayList<OrderListResponse.OrderData?>
                    binding.orderRecycler.visibility = View.VISIBLE
                    binding.orderRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.orderRecycler.adapter =
                        OrderListAdapter(requireContext(),
                            orderDataList as List<OrderListResponse.OrderData>, object : OnClickListnereWithType {
                            override fun onClickItem(position: Int, type: String) {
                                DialogUtils.showLoader(requireContext())
                                orderUpdate( it.data!![position].id.toString(),type,position)
                            }
                        }, position)
                } else {
                    binding.orderRecycler.visibility = View.VISIBLE
                    binding.orderRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.orderRecycler.adapter =
                        OrderListAdapter(requireContext(), ArrayList(), object : OnClickListnereWithType {
                            override fun onClickItem(position: Int, type: String) {

                            }
                        }, position)
                }
            } else {
                // UiUtils.showSnack(binding.root, it.message!!)
                binding.orderRecycler.visibility = View.GONE
            }
            DialogUtils.dismissLoader()
        }
    }

    fun orderUpdate(orderId:String,status:String,position: Int){
        homeViewModel.orderupdateStatus(requireContext(),orderId,status)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.success?.let { error ->
                        if (!error) {
                            it.massage?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            orderDataList.removeAt(position)
                            binding.orderRecycler.adapter!!.notifyDataSetChanged()
                            UiUtils.showSnack(binding.root, it.massage!!)

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}