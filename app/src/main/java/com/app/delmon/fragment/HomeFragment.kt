package com.app.delmon.fragment

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.HomeResponse
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.activity.MainActivity
import com.app.delmon.adapter.HomeBannerAdapter
import com.app.delmon.adapter.HomeCategoryAdapter
import com.app.delmon.adapter.HomeProductParentAdapter
import com.app.delmon.databinding.FragmentHomeBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnclickTwoPositionListner
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.LanguageManager
import com.app.delmon.utils.LottieLoader
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewPagerAdapter: HomeBannerAdapter
    lateinit var indicator: CircleIndicator
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedHelper: SharedHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        sharedHelper= SharedHelper(requireContext())
        changeLanguage(sharedHelper.language)
//        sharedHelper.recentSearches = ""
//        WindowCompat.setDecorFitsSystemWindows(req, false)
        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_productSearchFragment)
        }

        binding.basket.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_basketFragment)
        }

        binding.SpinWheel.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_luckyWheelFragment)
        }

        DialogUtils.showLoader(requireContext())
        getData()
        MainActivity.isloggedIn = false
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getData() {
        /*binding.lottieAnimation.visibility = View.VISIBLE
        binding.lottieAnimation.speed = 1.0f
        binding.lottieAnimation.playAnimation()*/

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
                                productRecyc(data.productdata)
                            }
                            it.popupAdvertisement?.let { image ->
                                if(image != "") {
                                    showImageDialog(requireContext(), image )
                                }
                            }
                        }
                    }


                    /*binding.lottieAnimation.visibility = View.GONE
                    binding.lottieAnimation.cancelAnimation()*/
                }
                DialogUtils.dismissLoader()
            }
    }

    private fun showImageDialog(context: Context, imageUrl: String) {
        // Create a custom dialog
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image)

        // Initialize views
        val imageView: ImageView = dialog.findViewById(R.id.imageView)
        val closeButton: ImageButton = dialog.findViewById(R.id.closeButton)

        // Load and display the image using Picasso
//        Picasso.get().load(imageUrl).into(imageView)

        if(imageUrl != null) {
            Picasso.get().load(imageUrl)
                .placeholder(R.drawable.placeholder_image).error(
                    R.drawable.placeholder_image
                ).into(imageView)
        }


        // Set click listener for the close button
        closeButton.setOnClickListener {
            // Close the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun catrecyc(category: List<HomeResponse.Data.Category?>?) {
        binding.catRecyc.layoutManager = GridLayoutManager(requireContext(),3,
            GridLayoutManager.VERTICAL,false)
        binding.catRecyc.scheduleLayoutAnimation();
        binding.catRecyc.adapter = HomeCategoryAdapter(requireContext(),category,object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args = Bundle()
                args.putInt("catId", category!![position]!!.id!!.toInt())
                args.putInt("catIdPosition", position)
                findNavController().navigate(R.id.action_homeFragment_to_categoryFragment,args)

            }
        })

    }

    private fun productRecyc(productdata: List<HomeResponse.Data.Productdata?>?) {
        binding.childrecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.childrecyc.scheduleLayoutAnimation();
        binding.childrecyc.adapter = productdata?.let {
            HomeProductParentAdapter(requireContext(),
                it,object :
                    OnclickTwoPositionListner {
                    override fun onClickItem(position: Int,subPosition: Int) {
                        val args= Bundle()
                        args.putInt("productId", productdata[position]!!.data!![subPosition]!!.id!!)
                        findNavController().navigate(R.id.action_home_fragment_to_product_details_fragment,args)
                    }
                }
            )
        }

    }

    private fun bannerRecyc(banner: List<HomeResponse.Data.Banner?>?) {
        viewPagerAdapter = HomeBannerAdapter(requireContext(),banner)
        binding.viewpager.adapter = viewPagerAdapter
        indicator = binding.indicator as CircleIndicator
        indicator.setViewPager(binding.viewpager)
    }

    private fun changeLanguage(languageCode: String) {
        LanguageManager.changeLanguage(requireActivity(), languageCode)

        // Set layout direction for the root view
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        LanguageManager.setLayoutDirection(rootView, languageCode)

        // You can also update UI elements or reload content if needed
        // For example, recreate the activity to apply the changes
//        recreate()
    }

}