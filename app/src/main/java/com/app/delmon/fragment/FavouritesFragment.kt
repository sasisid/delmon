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
import com.app.delmon.adapter.CategoryProductAdapter
import com.app.delmon.databinding.FragmentFavouritesBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.ProductViewModel


class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        DialogUtils.showLoader(requireContext())
        getData()
        binding.back.setOnClickListener {
            requireFragmentManager().popBackStack()
        }
        return binding.root
    }

    private fun getData(){
        productViewModel.getFavoritesProductData(requireContext())
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
                                if (!data.isNullOrEmpty())
                                    productRecyc(data as ArrayList<ProductResponse.Data>)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun productRecyc(data: ArrayList<ProductResponse.Data>) {
        binding.favRecyc.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )
        binding.favRecyc.scheduleLayoutAnimation();
        binding.favRecyc.adapter = CategoryProductAdapter(requireContext(), data!!, object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type: String) {
                if (type=="fav"){
                    DialogUtils.showLoader(requireContext())
                    updateFavourites(data[position]!!.id!!,0)
                    data.removeAt(position)
                }

            }
        }, object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                var args =Bundle()
                args.putInt("productId", data[position]!!.id!!)
                findNavController().navigate(R.id.action_favouritesFragment_to_product_details_fragment,args)
            }

        },"fav")
    }
    private fun updateFavourites(productId: Int,isfavourites:Int) {
        productViewModel.updateFavorites(requireContext(),productId, isfavourites)
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
                            binding.favRecyc.adapter?.notifyDataSetChanged()
//                            UiUtils.showToast(requireContext(),"Added to Cart",false)
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


}