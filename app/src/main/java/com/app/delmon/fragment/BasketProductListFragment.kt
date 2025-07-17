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
import com.app.delmon.Model.ProductResponse
import com.app.delmon.R
import com.app.delmon.adapter.BasketProductAdapter
import com.app.delmon.adapter.CategoryProductAdapter
import com.app.delmon.databinding.FragmentBasketProductListBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.ProductViewModel


class BasketProductListFragment : Fragment() {
    private lateinit var binding: FragmentBasketProductListBinding
    private lateinit var productViewModel: ProductViewModel
    var basketId = 0
    var basketName = ""
    var from = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            basketId = it.getInt("basketId",0)
            basketName = it.getString("basketName").toString()
            from = it.getString("from").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketProductListBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
            binding.pageTitle.text = basketName
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addCart.setOnClickListener {
            if (from=="user"){
                moveToCart(basketId)
            }else{
                adminBasketmoveToCart(basketId)
            }
        }
        if (from=="user"){
            getBasketProductData(basketId)
        }else{
            getAdminBasketProductData(basketId)
        }

        return binding.root
    }
    private fun getBasketProductData(basketId: Int){
        DialogUtils.showLoader(requireContext())
        productViewModel.getBasketProductData(requireContext(), basketId)
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
                                if (data.isNotEmpty()) {
                                    productRecyc(data as ArrayList<ProductResponse.Data>,"user")
                                    binding.addCart.visibility=View.VISIBLE
                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
    private fun getAdminBasketProductData(basketId: Int){
        DialogUtils.showLoader(requireContext())
        productViewModel.getAdminBasketProductData(requireContext(), basketId)
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
                                    productRecyc(data as ArrayList<ProductResponse.Data>,"admin")
                                    binding.addCart.visibility=View.VISIBLE
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun productRecyc(data: ArrayList<ProductResponse.Data>,from:String) {
        binding.productRecyc.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )
        binding.productRecyc.scheduleLayoutAnimation()
        binding.productRecyc.adapter = BasketProductAdapter(requireContext(), data, object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type: String) {
                if (type=="remove"){
                    deleteBasketProduct(data[position].id!!,position,data)
                }

            }
        }, object :
            OnClickListener {
            override fun onClickItem(position: Int) {
//                var args =Bundle()
//                args.putInt("productId", data[position].id!!)
//                findNavController().navigate(R.id.action_basketProductListFragment_to_product_details_fragment,args)
            }

        },from)
    }

    private fun deleteBasketProduct(
        basketId: Int,
        position: Int,
        data: ArrayList<ProductResponse.Data>
    ){
        DialogUtils.showLoader(requireContext())
        productViewModel.deleteBasketProduct(requireContext(), basketId)
            .observe(viewLifecycleOwner)
            {
                Log.d(ContentValues.TAG, "categorydata: $it")
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            data.removeAt(position)
                            binding.productRecyc.adapter?.notifyDataSetChanged()
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


    private fun moveToCart(basketId:Int){
        DialogUtils.showLoader(requireContext())
        productViewModel.basketToCart(requireContext(), basketId)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showToast(requireContext(), msg,true)
                            }
                        } else {
                            UiUtils.showToast(requireContext(), it.message!!,true)
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
    private fun adminBasketmoveToCart(basketId:Int){
        DialogUtils.showLoader(requireContext())
        productViewModel.adminbasketToCart(requireContext(), basketId)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showToast(requireContext(), msg,true)
                            }
                        } else {
                            UiUtils.showToast(requireContext(), it.message!!,true)
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


}