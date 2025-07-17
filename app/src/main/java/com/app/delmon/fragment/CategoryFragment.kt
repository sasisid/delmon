package com.app.delmon.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.BasketNameResponse
import com.app.delmon.Model.CategoryResponse
import com.app.delmon.Model.ProductResponse
import com.app.delmon.R
import com.app.delmon.adapter.BasketNameListAdapter
import com.app.delmon.adapter.CategoryAdapter
import com.app.delmon.adapter.CategoryProductAdapter
import com.app.delmon.adapter.WeightChildAdapter
import com.app.delmon.databinding.FragmentCategoryBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel
import com.app.delmon.viewmodel.ProductViewModel


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var productViewModel: ProductViewModel
    var catId = 0
    private var catIdPosition = 0
    var basketProductId = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button event
            if (binding.basketFrame.visibility==View.VISIBLE) {
              binding.basketFrame.visibility == View.GONE
            }
            else{
                 findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        arguments?.let {
            catId = it.getInt("catId")
            catIdPosition = it.getInt("catIdPosition",0)
            catIdPosition += 1
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.grid.setOnClickListener {
            binding.grid.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
            binding.catProductRecyc.layoutManager = GridLayoutManager(requireContext(),2,
                GridLayoutManager.VERTICAL,false)
            binding.catProductRecyc.adapter?.notifyDataSetChanged()

        }
        binding.list.setOnClickListener {
            binding.grid.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
            binding.catProductRecyc.layoutManager = GridLayoutManager(requireContext(),1,
                GridLayoutManager.VERTICAL,false)
            binding.catProductRecyc.adapter?.notifyDataSetChanged()
        }


        binding.cart.setOnClickListener {
            if (Constants.User.token.isNullOrEmpty()){
                findNavController().navigate(R.id.action_category_fragment_to_login_fragment)
            }else{
                findNavController().navigate(R.id.action_category_fragment_to_cart_fragment)
            }
        }
        if (Constants.User.apptype==1) {
            getcatData()
        }

        // Create the observer which updates the UI.
        val cartCOuntObserver = Observer<Int> { newCount ->
            // Update the UI, in this case, a TextView.
            if (newCount==0){
                binding.cartCount.visibility = View.GONE
            }else{
                binding.cartCount.visibility = View.VISIBLE
                binding.cartCount.text = newCount.toString()
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        productViewModel.cartCount.observe(requireActivity(), cartCOuntObserver)

        binding.newBasket.setOnClickListener {
            if (Constants.User.token.isNullOrEmpty()){
                findNavController().navigate(R.id.action_category_fragment_to_login_fragment)
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
        getProductData(catId)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            // Redirect system "Back" press to our dispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        }
    }

    fun getcatData(){
        DialogUtils.showLoader(requireContext())
        homeViewModel.getCategoryData(requireContext())
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
                                catRecyc(data)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getProductData(catId: Int) {
        DialogUtils.showLoader(requireContext())
        homeViewModel.getProductData(requireContext(),catId)
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
                                productViewModel.cartCount.value = it.totalCartCount
//                                if (!data.isNullOrEmpty()){
                                    catProductRecyc(data as ArrayList<ProductResponse.Data>)
//                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun getBasketData() {
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

    private fun basketRecyc(data: ArrayList<BasketNameResponse.Data?>) {
        binding.basketFrame.visibility = View.VISIBLE
        binding.basketRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.basketRecyc.scheduleLayoutAnimation();
        binding.basketRecyc.adapter = BasketNameListAdapter(requireContext(),"details",data,object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type:String) {
                createBasketProduct(data[position]!!.id!!,basketProductId)
                binding.basketFrame.visibility = View.GONE
//                binding.basketRecyc.adapter?.notifyDataSetChanged()
            }
        },0)
        binding.catRecyc.scrollToPosition(0)
    }

    private fun addToCart(productId: Int,price:String) {
        DialogUtils.showLoader(requireContext())
        productViewModel.addToCart(requireContext(),productId, price)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.massage?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            productViewModel.cartCount.value = it.totalCartCount
                            UiUtils.showToast(requireContext(),"Added to Cart",false)
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
    private fun updateFavourites(productId: Int,isfavourites:Int) {
        DialogUtils.showLoader(requireContext())
        productViewModel.updateFavorites(requireContext(),productId, isfavourites)
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
//                            UiUtils.showToast(requireContext(),"Added to Cart",false)
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
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
                           getBasketData()
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
    private fun createBasketProduct(basketId: Int,productId: Int) {
        DialogUtils.showLoader(requireContext())
        productViewModel.createBasketproduct(requireContext(),basketId,productId)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showToast(requireContext(), it.message!!,false)
                                UiUtils.showSnack(binding!!.root, msg)
                            }
                        } else {
//                            it.data?.let {
//                                getBasketData()
                                UiUtils.showToast(requireContext(),"Product Added To Basket",false)
//                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


    private fun catRecyc(data: List<CategoryResponse.Data?>) {
        binding.catRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.HORIZONTAL,false)
        binding.catRecyc.scheduleLayoutAnimation();
        binding.catRecyc.adapter = CategoryAdapter(requireContext(),data,object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                getProductData(data[position]!!.id!!)
                catId =data[position]!!.id!!
                binding.catProductRecyc.adapter?.notifyDataSetChanged()

            }
        },catIdPosition)
        binding!!.catRecyc.scrollToPosition(catIdPosition)

    }

    private fun  catProductRecyc(data: ArrayList<ProductResponse.Data>) {
        binding.catProductRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.catProductRecyc.scheduleLayoutAnimation();
        binding.catProductRecyc.adapter = CategoryProductAdapter(requireContext(),data,object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type:String) {
                if (type=="weight"){
                 /*   binding.weightCard.root.visibility = View.VISIBLE
                    UiUtils.showToast(requireContext(),"clicked",false)
                    weightRecyc()*/
                }else if (type=="add"){
                    if (Constants.User.token.isNullOrEmpty()){
                        findNavController().navigate(R.id.action_category_fragment_to_login_fragment)
                    }else{
                        addToCart(data[position]!!.id!!,data[position]!!.price!!.toString())
                    }
                }
                else if (type=="fav"){
                    if (Constants.User.token.isNullOrEmpty()){
                        findNavController().navigate(R.id.action_category_fragment_to_login_fragment)
                    }else{
                        updateFavourites(data[position]!!.id!!,1)
                    }
                }
                else if (type=="basket"){
                    if (Constants.User.token.isNullOrEmpty()){
                        findNavController().navigate(R.id.action_category_fragment_to_login_fragment)
                    }else{
                        basketProductId = data[position]!!.id!!
                        binding.basketFrame.visibility = View.VISIBLE
                        getBasketData()
                    }

                }

            }
        },object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args= Bundle()
                args.putInt("productId", data[position]!!.id!!)
              findNavController().navigate(R.id.action_categoryFragment_to_productDetailFragment,args)

            }
        },"category")
    }

    private fun weightRecyc(){
        binding.weightCard.catWeightRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.weightCard.catWeightRecyc.scheduleLayoutAnimation();
        binding.weightCard.catWeightRecyc.adapter = WeightChildAdapter(requireContext(),object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                binding.weightCard.root.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        // It is optional to remove since our dispatcher is lifecycle-aware. But it wouldn't hurt to just remove it to be on the safe side.
        onBackPressedCallback.remove()
        super.onDestroyView()
    }

}