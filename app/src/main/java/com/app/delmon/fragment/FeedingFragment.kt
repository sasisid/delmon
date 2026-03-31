package com.app.delmon.fragment

import android.content.ContentValues
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
import com.app.delmon.Model.HomeResponse
import com.app.delmon.Model.SubCategoryResponse
import com.app.delmon.R
import com.app.delmon.activity.MainActivity
import com.app.delmon.adapter.FeedingSubcatAdapter
import com.app.delmon.adapter.HomeBannerAdapter
import com.app.delmon.adapter.HomeCategoryAdapter
import com.app.delmon.databinding.FragmentFeedingBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel
import me.ibrahimsn.lib.SmoothBottomBar
import me.relex.circleindicator.CircleIndicator


class FeedingFragment : Fragment() {

    private lateinit var binding: FragmentFeedingBinding
    lateinit var viewPagerAdapter: HomeBannerAdapter
    lateinit var indicator: CircleIndicator
    private lateinit var homeViewModel: HomeViewModel
    var catId:Int =0
    var catIdPosition:Int =0
    var parentlist:ArrayList<Int> = ArrayList()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button event
            Log.d("TAG", "handleOnBackPressed: ${parentlist.toString()} ")
            if (!parentlist.isNullOrEmpty()) {
                if (parentlist.size > 1) {
                    Constants.User.PreviouscatId = parentlist[parentlist.size - 1]
                    parentlist.remove(parentlist[parentlist.size - 1])
                    fetchSubCatData(Constants.User.PreviouscatId, 0)
                }
                else if(parentlist.size == 1){
                    parentlist.remove(parentlist[parentlist.size - 1])
                    binding!!.subcatFrame.visibility = View.GONE
                    val navBar =
                        requireActivity().findViewById<SmoothBottomBar>(R.id.bottomBar)

                    navBar.visibility = View.VISIBLE
                }
            }else{
                requireActivity().finish()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedingBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_feedingFragment_to_productSearchFragment)
        }

        DialogUtils.showLoader(requireContext())
        getData()
        MainActivity.isloggedIn = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            // Redirect system "Back" press to our dispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        }
    }

    fun catrecyc(category: List<HomeResponse.Data.Category?>?) {
        binding!!.catRecyc.layoutManager = GridLayoutManager(requireContext(),3,
            GridLayoutManager.VERTICAL,false)
        binding!!.catRecyc.scheduleLayoutAnimation();
        binding!!.catRecyc.adapter = HomeCategoryAdapter(requireContext(), category, object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args = Bundle()
                catId = category!![position]!!.id!!
                catIdPosition = position
                parentlist.add(category[position]!!.parentId!!)
                    Constants.User.PreviouscatId = category[position]!!.parentId!!
                fetchSubCatData(category!![position]!!.id!!, category[position]!!.parentId!!)
                args.putInt("catId",catId)
                args.putString("from","onclick")

            }
        })

    }
    private fun getData(){
        homeViewModel.getHomeData(requireContext())
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
                                catrecyc(data.category)
                                bannerRecyc(data.banner)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
    private fun bannerRecyc(banner: List<HomeResponse.Data.Banner?>?) {
        viewPagerAdapter = HomeBannerAdapter(requireContext(),banner)
        binding.viewpager.adapter = viewPagerAdapter
        indicator = binding.indicator as CircleIndicator
        indicator.setViewPager(binding.viewpager)
    }

    fun fetchSubCatData(catId:Int,parentId:Int) {
        DialogUtils.showLoader(requireContext())
        homeViewModel.getSubCategoryData(requireContext(),catId)
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
                                val args = Bundle()
                                args.putInt("catId",catId)
                                if (data.isNullOrEmpty()){
                                    binding!!.subcatFrame.visibility = View.GONE

                                        findNavController().navigate(
                                            R.id.action_feedingFragment_to_category_fragment,
                                            args
                                        )
                                    parentlist = ArrayList()

                                }else{
                                    Constants.User.PreviouscatId = data[0]!!.parentId!!
                                    val navBar =
                                        requireActivity().findViewById<SmoothBottomBar>(R.id.bottomBar)

                                    navBar.visibility = View.GONE
                                    binding!!.subcatFrame.visibility = View.VISIBLE
                                    fetchSubCat(data)
                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    fun fetchSubCat(data: List<SubCategoryResponse.Data?>) {
        binding!!.subcatRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding!!.subcatRecyc.scheduleLayoutAnimation();
        binding!!.subcatRecyc.adapter = FeedingSubcatAdapter(requireContext(),data,object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args = Bundle()
                args.putInt("catId", data[position]!!.id!!)
                args.putString("from", "onclick")
                Constants.User.PreviouscatId = data!![position]!!.parentId!!
                parentlist.add(data[position]!!.parentId!!)
                Log.d("TAG", "onBackPressed onClickItem:AddedParentList ${ parentlist}")
                fetchSubCatData(data[position]!!.id!!,data!![position]!!.parentId!!)

            }
        })

    }

override fun onDestroyView() {
    // It is optional to remove since our dispatcher is lifecycle-aware. But it wouldn't hurt to just remove it to be on the safe side.
    onBackPressedCallback.remove()
    super.onDestroyView()
}
}