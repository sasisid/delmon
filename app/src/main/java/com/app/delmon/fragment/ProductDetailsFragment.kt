package com.app.delmon.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.BasketNameResponse
import com.app.delmon.Model.ProductDetailResponse
import com.app.delmon.Model.RecipesResponse
import com.app.delmon.R
import com.app.delmon.adapter.BasketNameListAdapter
import com.app.delmon.adapter.DetailsPageRecipiesAdapter
import com.app.delmon.adapter.ProductDetailSizeAdapter
import com.app.delmon.databinding.FragmentProductDetailsBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.interfaces.SingleTapListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.ProductViewModel
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productViewModel: ProductViewModel
    var sizeArrayData: ArrayList<ProductDetailResponse.Data.RelatedProduct> = ArrayList()
    var filteredsizeArrayData: ArrayList<ProductDetailResponse.Data.RelatedProduct> = ArrayList()

    var productId = 0
    var MainproductId = 0
    var price = 0.0
    var isStock = 0
    var offerPrice = 0.0
    var normalPrice = 0.0
    var quantity = 0
    var cartId = 0
    var selectedType = 1
    var basketProductId = 0
    var isFav = 0

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button event
            if (binding.basketFrame.visibility==View.VISIBLE) {
                binding.basketFrame.visibility = View.GONE
                binding.constraintLayout6.visibility = View.VISIBLE
            }
            else{
                if (Constants.User.apptype==1) {
                    findNavController().popBackStack()
                }else{
                    findNavController().popBackStack()
                }
            }
        }
    }

    companion object{
        var recipiesData: ArrayList<ProductDetailResponse.Data.Recipies> = ArrayList()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            // Redirect system "Back" press to our dispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        binding.strikeAmt.showStrikeThrough(true)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        arguments?.let {
            productId = it.getInt("productId")
            MainproductId = productId
        }
        binding.cart.setOnClickListener {
            findNavController().navigate(R.id.action_product_details_fragment_to_cart_fragment)
        }

        getProductData(productId)

        binding.basketImg.setOnClickListener {
            if (Constants.User.token.isNullOrEmpty()){
                findNavController().navigate(R.id.action_product_details_fragment_to_login_fragment)
            }else if (productId==0){
                UiUtils.showToast(requireContext(),"Select size of the Product",true)
            }
            else{
                basketProductId = productId
                binding.basketFrame.visibility = View.VISIBLE
                binding.constraintLayout6.visibility = View.GONE
                getBasketData()
            }
        }
        binding.addWishlist.setOnClickListener {
        if (isFav==0)    {
            isFav = 1
        }else{
            isFav = 0
        }
            updateFavourites(MainproductId,isFav)
        }
        binding.newBasket.setOnClickListener {
            if (Constants.User.token.isNullOrEmpty()){
                findNavController().navigate(R.id.action_product_details_fragment_to_login_fragment)
            }else {
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

        binding.apply {
            updateCartLayout(0)

            addCart.setOnClickListener {
                if (Constants.User.token.isNullOrEmpty()){
                    findNavController().navigate(R.id.action_product_details_fragment_to_login_fragment)
                }else if (productId==0){
                    UiUtils.showToast(requireContext(),"Select size of the Product",true)
                } else if(isStock <= 0) {
                    DialogUtils.showAlert(
                        requireContext(),
                        object :
                            SingleTapListener {
                            override fun singleTap() {

                            }
                        },
                        requireContext().resources.getString(R.string.out_of_stock),
                        "Oops"
                    )
                } else {
                    addToCart(productId, price.toString())
                }
            }

            plus.setOnClickListener {
                quantity += 1
                var tot  = price * quantity
                cartCount.text = quantity.toString()
                amt.text =String.format("%.3f", tot) + " BD"
                updateCart(cartId,quantity)
            }

            minus.setOnClickListener {
                quantity -= 1
                if (quantity>=1) {
                    var tot = price * quantity
                    cartCount.text = quantity.toString()
                    amt.text = String.format("%.2f", tot) + " BD"
                }
                if (quantity >= 0){
                    updateCart(cartId,quantity)
                }
                if (quantity==0){
                    updateCartLayout(0)
                }
            }

            piecesBtn.setOnClickListener {
                piecesBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.secondary_color))
                piecesBtn.setTextColor(resources.getColor(R.color.white))
                cartonBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.btn_grey))
                cartonBtn.setTextColor(resources.getColor(R.color.black))
                filteredsizeArrayData = sizeArrayData.filter { it -> it.soldType==1 } as ArrayList<ProductDetailResponse.Data.RelatedProduct>
                binding.basketImg.visibility = View.VISIBLE
                sizeRecyc(filteredsizeArrayData)
            }

            cartonBtn.setOnClickListener {
                cartonBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.secondary_color))
                cartonBtn.setTextColor(resources.getColor(R.color.white))
                piecesBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.btn_grey))
                piecesBtn.setTextColor(resources.getColor(R.color.black))
                filteredsizeArrayData = sizeArrayData.filter { it -> it.soldType==2 } as ArrayList<ProductDetailResponse.Data.RelatedProduct>
                binding.basketImg.visibility = View.GONE
                sizeRecyc(filteredsizeArrayData)
            }

            viewCart.visibility = View.GONE
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    @SuppressLint("SetTextI18n")
    private fun getProductData(productId: Int) {
        DialogUtils.showLoader(requireContext())
        Log.d("TAG", "sizeRecycData: ${productId}")
        productViewModel.getProductData(requireContext(),productId)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "sizeRecycData: " + it)
                Log.e("appSample", "sizeRecycData: " + it)
                it?.let {
                    Log.e("appSample", "Error: ${it.error}")
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            it.data?.let { data ->
                                binding.apply {

                                    if(data.isStock == null) {
                                        Log.e("appSample", "NULL")
                                        return@apply;
                                    }

                                    if(data.image != null) {
                                        Log.e("appSample", "IMAGE: ${data.image!![0]}")
                                        Glide.with(requireContext()).load(data.image!![0]).placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image).into(pimage)
                                    }

                                    title.text = data.name.toString()
                                    weight.text = data.weight.toString()
                                    Log.e("appSample", "STOCK: ${data.isStock}")
                                    /*if (data.isStock == 1) {
                                        stock.text = resources.getString(R.string.out_of_stock)
                                        stock.setBackgroundColor(requireContext().resources.getColor(R.color.delmon_red))
                                    }*/

                                    if(data.isStock != null) {
                                        if (data.isStock!! > 0) {
                                            stock.text = resources.getString(R.string.instock)
                                            stock.setBackgroundColor(requireContext().resources.getColor(R.color.secondary_feeding_color))
                                        } else {
                                            stock.text = resources.getString(R.string.out_of_stock)
                                            stock.setBackgroundColor(requireContext().resources.getColor(R.color.delmon_red))
                                        }
                                        isStock = data.isStock!!
                                    }

                                    if(data.price != null) {
                                        price = data.price!!
                                    }

                                    noOfPcs.text = resources.getString(R.string.no_of_picies)+" "+data.noOfPieces.toString()
                                    serves.text = resources.getString(R.string.serves)+" "+data.serves.toString()
                                    descTxt.text = data.description
                                    if (data.quantity!! > 0) {
                                        amt.text =  (data.quantity!! * price).toString() + " BD"
                                        Log.e("appSample", "ONE: " + (data.quantity!! * price).toString() + " BD")
                                    } else {
                                        amt.text = "$price BD"
                                        Log.e("appSample", "TWO: $price")
                                    }

                                    Log.e("appSample", "DATA: " + amt.text.toString())

                                    if ((data.cartonActive==1 && data.piecesActive == 1 )|| data.piecesActive==1 && data.cartonActive==0){
                                        selectedType = 1
                                        binding.basketImg.visibility = View.VISIBLE
                                    }else if (data.piecesActive==0 && data.cartonActive==1){
                                        selectedType = 2
                                        binding.basketImg.visibility = View.GONE
                                    }
                                    if (data.piecesActive==1){
                                        binding.piecesBtn.visibility=View.VISIBLE
                                    }else{
                                        binding.piecesBtn.visibility=View.GONE

                                    }
                                    Log.d("TAG", "getProductData: piesces ${data.piecesActive} == ${data.cartonActive} ")
                                    if (data.cartonActive==1){
                                        binding.cartonBtn.visibility=View.VISIBLE
                                    }else{
                                        binding.cartonBtn.visibility=View.GONE
                                    }
                                    cartCount.text = data.quantity.toString()
                                    quantity = data.quantity!!
                                    cartId = data.cartId!!.toInt()
                                    if (data.isFavorites == 0){
                                        isFav = 0
                                        Glide.with(requireContext()).load(R.drawable.wishlist).placeholder(R.drawable.placeholder_image).into(binding.addWishlist)
                                    }else{
                                        isFav = 1
                                        Glide.with(requireContext()).load(R.drawable.selected_wishlist).placeholder(R.drawable.placeholder_image).into(binding.addWishlist)
                                    }
//                                    updateCartLayout(data.quantity!!)
                                    sizeArrayData = data.relatedProduct as ArrayList<ProductDetailResponse.Data.RelatedProduct>
                                    Log.d("TAG", "getProductData: ${sizeArrayData.toString()}")
                                    filteredsizeArrayData = if (selectedType==1){
                                        sizeArrayData.filter { it -> it.soldType==1 } as ArrayList<ProductDetailResponse.Data.RelatedProduct>
                                    }else{
                                        sizeArrayData.filter { it -> it.soldType==2 } as ArrayList<ProductDetailResponse.Data.RelatedProduct>
                                    }
                                    Log.d("TAG", "getProductData: 2 ${filteredsizeArrayData.toString()}")
//                                    if (!filteredsizeArrayData.isNullOrEmpty()) {
                                    sizeRecyc(filteredsizeArrayData)
//                                    }
                                    if (!data.recipiesList.isNullOrEmpty()) {
                                        recipesListRecyc(data.recipiesList)
                                        recipiesData =
                                            data.recipiesList as ArrayList<ProductDetailResponse.Data.Recipies>
                                    }
                                }
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
        binding.constraintLayout6.visibility = View.GONE
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

    private fun updateCartLayout(quantity:Int){
        binding.apply {
            if (quantity!=0){
                binding.cartCount.text = quantity.toString()
                cartCount.visibility = View.VISIBLE
                plus.visibility = View.VISIBLE
                minus.visibility = View.VISIBLE
                addCart.visibility = View.GONE
            }else{
                cartCount.visibility = View.GONE
                plus.visibility = View.GONE
                minus.visibility = View.GONE
                addCart.visibility = View.VISIBLE
            }

        }
    }

    private fun recipesListRecyc(data: List<ProductDetailResponse.Data.Recipies?>?) {
        binding.recipiesRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.recipiesRecyc.scheduleLayoutAnimation();
        binding.recipiesRecyc.adapter = DetailsPageRecipiesAdapter(requireContext(),data,"second",object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args = Bundle()
                args.putInt("position",position)
                args.putString("from","details")
                findNavController().navigate(R.id.action_product_details_fragment_to_recipiesDetailsFragment,args)

            }
        })
        binding.recipiesRecyc.adapter!!.notifyDataSetChanged()
    }

    private fun sizeRecyc(dataList: ArrayList<ProductDetailResponse.Data.RelatedProduct>) {
        Log.d("TAG", "sizeRecycData: ${dataList.toString()}")

        val data = dataList.sortedBy {
            it.weight!!.replace("g", "").toInt()
        }

        binding.sizeRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.HORIZONTAL,false)
        binding.sizeRecyc.scheduleLayoutAnimation()
        binding.sizeRecyc.adapter = ProductDetailSizeAdapter(requireContext(),data,productId,0,object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type:String) {
                Log.d("TAG", "sizeRecycData: 1 ${position.toString()} == ${data.toString()}")
                productId = (data ?: throw NullPointerException("Expression 'data' must not be null"))[position]!!.id!!
                price = data[position].price!!
                binding.amt.text = "$price BD"
                binding.weight.text = data[position].weight!!
                //                getProductData(data[position]!!.id!!)
//                    updateCartLayout(data[position].quantity!!)
                updateCartLayout(0)

            }
        })

        if (!filteredsizeArrayData.isNullOrEmpty()) {
            productId = data[0].id!!
            price = data[0].price!!
            normalPrice = data[0].normalPrice!!
            offerPrice = data[0].offerPrice!!
            binding.amt.text = "$price BD"
            Log.e("appSample", "isNullOrEmpty: $price")
            Log.e("appSample", "normalPrice: $normalPrice")
            Log.e("appSample", "offerPrice: $offerPrice")
            binding.weight.text = data[0].weight

            if(offerPrice > 0.0) {
                binding.strikeAmt.visibility = View.VISIBLE
                binding.strikeAmt.text = "$normalPrice BD"

                binding.amt.visibility = View.VISIBLE
                binding.amt.text = "$offerPrice BD"
            } else {
                binding.strikeAmt.visibility = View.GONE
                binding.strikeAmt.text = "$normalPrice BD"

                binding.amt.visibility = View.VISIBLE
                binding.amt.text = "$normalPrice BD"
            }

            /*if(data[0].normalPrice!! > data[0].offerPrice!! &&
                        data[0].normalPrice!! > data[0].price!!) {
                binding.strikeAmt.visibility = View.VISIBLE
                binding.strikeAmt.text = "$normalPrice BD"
            } else {
                binding.strikeAmt.visibility = View.INVISIBLE
            }*/

            if (data[0].cartId!=0){
//                updateCartLayout(data[0].quantity!!)
                updateCartLayout(0)
            }else{
                updateCartLayout(0)
            }
        } else {
            productId = 0
            price = 0.0
            binding.amt.text = "$price BD"
            Log.e("appSample", "isNullOrEmpty_ELSE: $price")
            binding.weight.text ="0"
            updateCartLayout(0)
        }

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
                            cartId = it.data!!.id!!
                            quantity = 1
                            productViewModel.cartCount.value = it.totalCartCount
                            updateCartLayout(0)
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
                            if (isFav==0){
                                Glide.with(requireContext()).load(R.drawable.wishlist).placeholder(R.drawable.placeholder_image).into(binding.addWishlist)
                            }else{
                                Glide.with(requireContext()).load(R.drawable.selected_wishlist).placeholder(R.drawable.placeholder_image).into(binding.addWishlist)
                            }
//                            UiUtils.showToast(requireContext(),"Added to Cart",false)
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun updateCart(cartId:Int,quantity:Int){
        DialogUtils.showLoader(requireContext())
        productViewModel.updateCart(requireContext(),cartId,quantity)
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
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    override fun onDestroyView() {
        // It is optional to remove since our dispatcher is lifecycle-aware. But it wouldn't hurt to just remove it to be on the safe side.
        onBackPressedCallback.remove()
        super.onDestroyView()
    }
}