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
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.AdminBasketResponse
import com.app.delmon.Model.BasketNameResponse
import com.app.delmon.R
import com.app.delmon.adapter.AdminBasketListAdapter
import com.app.delmon.adapter.BasketNameListAdapter
import com.app.delmon.databinding.FragmentBasketBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.ProductViewModel

class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding
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
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        binding.customisedBasket.setOnClickListener {
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.VISIBLE
            binding.customBasketRecyc.visibility = View.VISIBLE
            binding.ourBasketRecyc.visibility = View.GONE
            binding.newBasket.visibility = View.VISIBLE

            getBasketNameList()
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ourBasket.setOnClickListener {
            binding.view2.visibility = View.GONE
            binding.view1.visibility = View.VISIBLE
            binding.ourBasketRecyc.visibility = View.VISIBLE
            binding.customBasketRecyc.visibility = View.GONE
            binding.newBasket.visibility = View.GONE
            getAdminBasketNameList()
        }
        binding.ourBasket.performClick()


        binding.newBasket.setOnClickListener {
            if (Constants.User.token.isNullOrEmpty()){
                findNavController().navigate(R.id.action_basketFragment_to_login_fragment)
            }
            else {
                DialogUtils.showInputDialog(requireContext(), "Add Basket",
                    requireContext().resources.getString(R.string.ok),
                    requireContext().resources.getString(R.string.cancel), object : DialogCallBack {
                        override fun onPositiveClick(toString: String) {
                            createBasket(toString)
                        }

                        override fun onNegativeClick() {

                        }

                    })
            }
        }

        return binding.root
    }

    private fun createBasket(name: String) {
        DialogUtils.showLoader(requireContext())
        productViewModel.createBasketName(requireContext(),name)
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
                            getBasketNameList()
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


    private fun getBasketNameList(){
        DialogUtils.showLoader(requireContext())
        productViewModel.getBasketData(requireContext())
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
                                basketRecyc(data as ArrayList<BasketNameResponse.Data?>)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun getAdminBasketNameList() {
        DialogUtils.showLoader(requireContext())
        productViewModel.getAdminBasketData(requireContext())
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                          /*  it.message?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }*/
                        } else {
                            it.data?.let { data ->
                                ourBasketRecyc(data as ArrayList<AdminBasketResponse.Data?>)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun moveToCart(basketId:Int) {
        DialogUtils.showLoader(requireContext())
        productViewModel.basketToCart(requireContext(), basketId)
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
                            UiUtils.showToast(requireContext(), it.message!!,true)
                         }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun basketRecyc(data: ArrayList<BasketNameResponse.Data?>) {
        binding.customBasketRecyc.visibility = View.VISIBLE
        binding.customBasketRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.customBasketRecyc.scheduleLayoutAnimation()
        binding.customBasketRecyc.adapter = BasketNameListAdapter(requireContext(),"list",data,object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type:String) {
                when (type) {
                    "remove" -> {
                        deleteBasket(data,data[position]!!.id,position)
                    }
                    "moveCart" -> {
                        moveToCart(data[position]!!.id!!)
                    }
                    else -> {
                        var args = Bundle()
                        args.putInt("basketId", data[position]!!.id!!)
                        args.putString("basketName", data[position]!!.name!!)
                        args.putString("from", "user")
                        findNavController().navigate(
                            R.id.action_basketFragment_to_basketProductListFragment,
                            args
                        )
                        binding.customBasketRecyc.visibility = View.GONE
                        binding.customBasketRecyc.adapter?.notifyDataSetChanged()
                    }
                }
            }
        },0)
        binding.customBasketRecyc.scrollToPosition(0)
    }

    private fun ourBasketRecyc(data: ArrayList<AdminBasketResponse.Data?>) {
        binding.ourBasketRecyc.visibility = View.VISIBLE
        binding.ourBasketRecyc.layoutManager = GridLayoutManager(requireContext(),2,
            GridLayoutManager.VERTICAL,false)
        binding.ourBasketRecyc.scheduleLayoutAnimation()
        binding.ourBasketRecyc.adapter = AdminBasketListAdapter(requireContext(),"list",data as ArrayList<AdminBasketResponse.Data?>,object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type:String) {
//                binding.ourBasketRecyc.visibility = View.GONE
                var args = Bundle()
                args.putInt("basketId", data[position]!!.id!!)
                args.putString("basketName", data[position]!!.name!!.toString())
                args.putString("from", "admin")
                findNavController().navigate(
                    R.id.action_basketFragment_to_basketProductListFragment,
                    args
                )
                binding.ourBasketRecyc.adapter?.notifyDataSetChanged()
            }
        },0)
        binding.ourBasketRecyc.scrollToPosition(0)
    }


    private fun deleteBasket(data: ArrayList<BasketNameResponse.Data?>, basketId: Int?,position:Int) {
        DialogUtils.showLoader(requireContext())
        productViewModel.deleteBasketName(requireContext(), basketId!!)
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
                            data.removeAt(position)
                            binding.customBasketRecyc.adapter?.notifyDataSetChanged()
//                            getBasketData()
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

}