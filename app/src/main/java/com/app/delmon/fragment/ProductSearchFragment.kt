package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.ProductResponse
import com.app.delmon.Model.TrendingProductResponse
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.adapter.BasketProductAdapter
import com.app.delmon.adapter.CategoryProductAdapter
import com.app.delmon.adapter.RecentSearchAdapter
import com.app.delmon.adapter.TrendingProductsAdapter
import com.app.delmon.adapter.WeightChildAdapter
import com.app.delmon.databinding.FragmentProductSearchBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel

class ProductSearchFragment : Fragment() {
    private lateinit var binding: FragmentProductSearchBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recentSearches: String
    private lateinit var sharedHelper: SharedHelper
    private lateinit var recentList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductSearchBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        sharedHelper = SharedHelper(requireContext())
        recentList = ArrayList<String>()
        // Inflate the layout for this fragment
        getData()
        getTrendingData()
        binding.apply {
            search.setOnClickListener {
                getSearchData(searchTxt.text.toString(), "search")
            }
//            back.setOnClickListener {
//                requireFragmentManager().popBackStack();
//
//            }
        }

        checkRecentSearches()
        return binding.root
    }

    private fun checkRecentSearches() {
        if (sharedHelper.recentSearches != null) {
            recentSearches = sharedHelper.recentSearches
            if (recentSearches != "") {
                Log.e("appSample", "Data: $recentSearches")
                recentList = ArrayList(recentSearches.split(","))
                recentSearchRecycle(recentList)
            } else {
                binding.materialTextView.visibility = View.GONE
                binding.trendingTextView.visibility = View.GONE
                binding.searchRecyc.visibility = View.GONE
                binding.trendingRecyc.visibility = View.GONE
            }
        }
    }

    private fun getTrendingData() {
        homeViewModel.getTrendingProductData(requireContext())
            .observe(
                viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "TrendingData: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding!!.root, msg)
                            }
                        } else {

                            if (error) {

                            } else {
                                it.data?.let { data ->
                                    if (data.isNullOrEmpty()) {
                                        binding.trendingTextView.visibility = View.GONE
                                        binding.trendingRecyc.visibility = View.GONE
                                    } else {
                                        binding.trendingTextView.visibility = View.VISIBLE
                                        binding.trendingRecyc.visibility = View.VISIBLE
                                        trendingProductRecycle(data as ArrayList<TrendingProductResponse.Data>)
                                    }
                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun getData() {
        DialogUtils.showLoader(requireContext())
        homeViewModel.getProductData(requireContext(), 0)
            .observe(
                viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding!!.root, msg)
                            }
                        } else {
                            it.data?.let {

                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun trendingProductRecycle(data: ArrayList<TrendingProductResponse.Data>) {
        binding.trendingRecyc.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )
        binding.trendingRecyc.scheduleLayoutAnimation()
        binding.trendingRecyc.adapter = TrendingProductsAdapter(requireContext(), data, object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int, itemName: String) {
                Log.e("appSample", "ProductId: ${data[position]!!.productId!!}")
                var args = Bundle()
                args.putInt("productId", data[position]!!.productId!!)
                findNavController().navigate(
                    R.id.action_productSearchFragment_to_product_details_fragment2,
                    args
                )
            }
        }, object :
            OnClickListener {
            override fun onClickItem(position: Int) {

            }

        })
    }

    private fun recentSearchRecycle(data: ArrayList<String>) {
        binding.searchRecyc.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )
        binding.searchRecyc.scheduleLayoutAnimation()
        binding.searchRecyc.adapter = RecentSearchAdapter(requireContext(), data, object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int, itemName: String) {

                recentList.removeAt(position)
                recentList.add(0, "" + itemName)

                binding.searchTxt.text = Editable.Factory.getInstance().newEditable(itemName)
                var data = ""
                for ((iteration, item) in recentList.withIndex()) {
                    if(iteration < 5) {
                        data = if(data == "") {
                            item
                        } else {
                            "$data,$item"
                        }
                    }
                }
                sharedHelper.recentSearches = data
                checkRecentSearches()

                /*if (type=="remove"){
                    deleteBasketProduct(data[position].id!!,position,data)
                }*/

            }
        }, object :
            OnClickListener {
            override fun onClickItem(position: Int) {

            }

        })
    }

    private fun productRecyc(data: ArrayList<ProductResponse.Data>) {
        binding.productRecyc.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )
        binding.productRecyc.scheduleLayoutAnimation();
        binding.productRecyc.adapter = CategoryProductAdapter(requireContext(), data!!, object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int, type: String) {
                if (type == "weight") {
                    binding.weightCard.root.visibility = View.VISIBLE
                    UiUtils.showToast(requireContext(), "clicked", false)
                    weightRecyc()
                }

            }
        }, object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                Log.e("appSample", "ID: ${data[position]!!.id!!}");
                var args = Bundle()
                args.putInt("productId", data[position]!!.id!!)
                findNavController().navigate(
                    R.id.action_productSearchFragment_to_product_details_fragment2,
                    args
                )

            }

        }, "search")
    }

    private fun weightRecyc() {
        binding.weightCard.catWeightRecyc.layoutManager = GridLayoutManager(
            requireContext(), 1,
            GridLayoutManager.VERTICAL, false
        )
        binding.weightCard.catWeightRecyc.scheduleLayoutAnimation();
        binding.weightCard.catWeightRecyc.adapter = WeightChildAdapter(requireContext(), object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                binding.weightCard.root.visibility = View.GONE
            }
        })
    }

    private fun getSearchData(text: String, type: String) {

        if(!recentList.contains(text)) {
            if(recentList != null) {
                if(recentList.size < 5) {
                    recentList.add(0, text)
                } else {
                    recentList.removeAt(recentList.size - 1)
                    recentList.add(0, text)
                }
            }
        }

        var data = ""
        for ((iteration, item) in recentList.withIndex()) {
            if(iteration < 5) {
                data = if(data == "") {
                    item
                } else {
                    "$data,$item"
                }
            }
        }
        sharedHelper.recentSearches = data

        /*if (sharedHelper.recentSearches != "") {
            sharedHelper.recentSearches = text + "," + sharedHelper.recentSearches
        } else {
            sharedHelper.recentSearches = "" + text.trim()
        }*/
        Log.e("appSample", "RecentSearches: ${sharedHelper.recentSearches}")
        Log.e("appSample", "RecentSearches: $recentList")
        checkRecentSearches()

        DialogUtils.showLoader(requireContext())
        homeViewModel.getSearchProductData(requireContext(), text)
            .observe(
                viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "onLoginClicked: $it")
                //                    DialogUtils.dismissLoader()
                it?.let {
                    it.error?.let { error ->
                        if (error) {

                        } else {
                            it.data?.let { data ->
                                if (data.isNullOrEmpty()) {
                                    binding.productRecyc.visibility = View.GONE
                                    binding.materialTextView.visibility = View.VISIBLE
                                    binding.trendingTextView.visibility = View.VISIBLE
                                    binding.searchRecyc.visibility = View.VISIBLE
                                    binding.trendingRecyc.visibility = View.VISIBLE
                                } else {
                                    binding.productRecyc.visibility = View.VISIBLE
                                    binding.materialTextView.visibility = View.GONE
                                    binding.trendingTextView.visibility = View.GONE
                                    binding.searchRecyc.visibility = View.GONE
                                    binding.trendingRecyc.visibility = View.GONE
                                    productRecyc(data as ArrayList<ProductResponse.Data>)
                                }
                                binding.searchTxt.text.clear()
                            }
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


}