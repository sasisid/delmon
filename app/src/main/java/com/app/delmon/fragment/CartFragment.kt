package com.app.delmon.fragment

import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.delmon.Model.CartResponseNew
import com.app.delmon.Model.CouponListModel
import com.app.delmon.Model.RecipesResponse
import com.app.delmon.R
import com.app.delmon.adapter.BillingAdapter
import com.app.delmon.adapter.CartAdapter
import com.app.delmon.adapter.CouponListAdapter
import com.app.delmon.databinding.FragmentCartBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.interfaces.SingleTapListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.ProductViewModel
import com.google.android.material.datepicker.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var productViewModel: ProductViewModel


    var min = 0
    var max = 0
    var employeDiscount = 0.0
    var CouponData: ArrayList<CouponListModel.Data> = ArrayList()

    companion object {
        var totamt = 0.0
        val cartPrice: MutableLiveData<Double> by lazy {
            MutableLiveData<Double>(0.0)
        }
        var orderType = "PICKUP"
        var pickupAddress = ""
        var deliveryAddress = ""
        var deliveryFullAddress = ""
        var deliveryCharge: Double = 0.0
        var loyaltyPoint: Double = 0.0
        var loyaltyAmount: Double = 0.0
        var walletAmount: Double = 0.0
        var totalAmount: Double = 0.0
        var vat: Double = 0.0
        var vatAmount: Double = 0.0
        var grandTotal: Double = 0.0
        var paymentType = "CASH"
        var isLoyaltyApply = 0
        var isCouponApply = 0
        var couponId = ""
        var couponAmount: Double = 0.0
        var cartId = JSONArray()
        var orderData = JSONArray()
        var cartDataList: List<CartResponseNew.Data?> = ArrayList()
        var deliveryDate = ""
        var adminDate = 0
        var cartoonDiscount = 0
        var cartonDiscountedCount = 0
        var maxCartonDiscountPerDay = 0
        var defaultCartonDiscount = 0
        var defaultMaxCartonDiscountPerDayUser = 0
        var MaxCartonDiscountPerDayUser = 0
        var defaultMaxCartonDiscountPerDayEmployee = 0
        var isCardPayment = 0
        var isCod = 0
        var isSelfPickup = 0
        var isDelivery = 0
        var lat = ""
        var lng = ""
        var notes = ""
        var userCartonDiscount = 0
        var empDefaultCartonDiscount = 0

        var empCartonDiscount = 0
        var employeeCartonDiscount = 0
        lateinit var holidays: ArrayList<String>

    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            couponAmount = 0.0
            isCouponApply = 0
            couponId = "0"
            isLoyaltyApply = 0
            loyaltyAmount = 0.0
            loadCalc()
            findNavController().popBackStack()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        Log.d("TAG", "cart: createview $orderType")
        requireActivity().apply {
            // Redirect system "Back" press to our dispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        }
        if (!Constants.User.isLoggedIn) {
            findNavController().navigate(R.id.action_cart_fragment_to_login_fragment)
        } else {
            DialogUtils.showLoader(requireContext())
            getCart()
            getcoupon()
        }
        binding.walletBalance.visibility = View.GONE
        binding.walletBalanceLable.visibility = View.GONE
        if (Constants.User?.usertype.equals("EMPLOYEE") || Constants.User.usertype == "MERCHANT") {
            binding.walletLayout.visibility = View.GONE
            binding.loyaltyLayout.visibility = View.GONE
            binding.loyaltyLable.visibility = View.GONE
            binding.couponLable.visibility = View.GONE
            binding.couponLayout.visibility = View.GONE
        }
        if (Constants.User?.usertype.equals("EMPLOYEE")) {
            binding.employeeDiscLabel.visibility = View.VISIBLE
            binding.employeeDiscCard.visibility = View.VISIBLE
        }
        binding.back.setOnClickListener {
            couponAmount = 0.0
            isCouponApply = 0
            couponId = "0"
            isLoyaltyApply = 0
            loyaltyAmount = 0.0
            loadCalc()
            findNavController().popBackStack()
        }

        binding.deliveryDateCard.setOnClickListener {
            openDatePicker(childFragmentManager, min, max)
            Log.d("TAG", "onCreateView:12")

        }

        binding.continuePurchaseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cart_fragment_to_home_fragment)
        }

        // Create the observer which updates the UI.
        cartPrice.observe(requireActivity()) {
            binding.totAmt.text = String.format(Locale.US,"%.3f", it) + " BD"
        }

        binding.pickup.setOnClickListener {
            binding.saveAs.visibility = View.GONE
            binding.deliveryLable.text =
                requireContext().resources.getString(R.string.pickup_address) //"PickUp Address"
            orderType = "PICKUP"
            binding.pickup.setBackgroundResource(R.drawable.rectangle_border)
            binding.doorDelivery.setBackgroundResource(R.drawable.rectangle_border_white)
            deliveryCharge = 0.0
            binding.address.text = pickupAddress

            loadCalc()
        }

        if (orderType == "PICKUP") {
            binding.pickup.performClick()
        } else {
            binding.deliveryLable.text = "Delivery Address"
            binding.doorDelivery.setBackgroundResource(R.drawable.rectangle_border)
            binding.pickup.setBackgroundResource(R.drawable.rectangle_border_white)
        }

        binding.doorDelivery.setOnClickListener {
            binding.saveAs.visibility = View.GONE
            orderType = "DELIVERY"
            binding.deliveryLable.text = "Delivery Address"
            binding.doorDelivery.setBackgroundResource(R.drawable.rectangle_border)
            binding.pickup.setBackgroundResource(R.drawable.rectangle_border_white)
            var args = Bundle()
            args.putString("from", "cart")
            findNavController().navigate(R.id.action_cart_fragment_to_addressFragment, args)

        }

        binding.employeeCouponSwitch.setOnClickListener {
            employeeCouponSwitchOnClick()
        }

        binding.loyaltySwitch.setOnClickListener {
            if (binding.loyaltySwitch.isChecked) {
                binding.coupon.isEnabled = false
                binding.couponLayout.isEnabled = false
                binding.couponApply.isEnabled = false
                binding.couponLayout.background.setTint(ContextCompat.getColor(requireContext(), R.color.shadow))

                DialogUtils.showLoader(requireContext())
                productViewModel.checkLoyaltyAmount(requireContext(), loyaltyPoint, grandTotal)
                    .observe(viewLifecycleOwner) { it ->
                        it?.let {
                            if (it.error!!) {
                                UiUtils.showSnack(binding.root, it.massage!!)
                            } else {
                                UiUtils.showSnack(binding.root, it.massage!!)
                                if (it.data!! != 0) {
                                    loyaltyAmount = it.data!!.toDouble()
                                    isLoyaltyApply = 1
                                    loadCalc()
                                } else {
                                    binding.loyaltySwitch.isChecked = false

                                    binding.coupon.isEnabled = true
                                    binding.couponApply.isEnabled = true
                                    binding.couponLayout.isEnabled = true
                                    binding.couponLayout.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))

                                }
                            }
                        }

                        DialogUtils.dismissLoader()
                    }
            } else {
                binding.coupon.isEnabled = true
                binding.couponApply.isEnabled = true
                binding.couponLayout.isEnabled = true
                binding.couponLayout.background.setTint(ContextCompat.getColor(requireContext(), R.color.white))

                loyaltyAmount = 0.0
                isLoyaltyApply = 0
                loadCalc()
            }
        }

        binding.couponApply.setOnClickListener {
            if (binding.coupon.text.toString().isNullOrEmpty()) {
                UiUtils.showSnack(binding.root, "Enter Coupon Code")
            } else {
                var data =
                    CouponData.filter { it.couponName.equals(binding.coupon.text.toString()) }
                if (data.isNullOrEmpty()) {
                    UiUtils.showSnack(binding.root, requireContext().resources.getString(R.string.coupon_already_used))
                } else {

                    if(isCouponApply == 1) {
                        isCouponApply = 0
                        binding.loyaltySwitch.isEnabled = true

                        binding.couponApply.text = "Apply"
                        binding.coupon.setText("")

//                        binding.couponApply.isEnabled = true
//                        binding.couponApply.isClickable = true
//                        binding.coupon.isEnabled = true
//                        binding.coupon.isClickable = true

                        couponId = ""
                        couponAmount = 0.0
                        loadCalc()
                    } else {
                        isCouponApply = 1
                        binding.loyaltySwitch.isEnabled = false

                        binding.couponApply.text = "Remove"
//                        binding.couponApply.isEnabled = false
//                        binding.couponApply.isClickable = false
//                        binding.coupon.isEnabled = false
//                        binding.coupon.isClickable = false

                        if (grandTotal <= data[0].maximumDiscount!!.toDouble()) {
                            couponAmount = grandTotal
                        } else {
                            couponAmount = data[0].maximumDiscount!!.toDouble()
                        }

                        couponId = data[0].id.toString()
                        loadCalc()
                    }

//                    binding.couponApply.text = "Applied"
//                    binding.couponApply.text = "Remove"
//                    binding.couponApply.isEnabled = false
//                    binding.couponApply.isClickable = false
//                    binding.coupon.isEnabled = false
//                    binding.coupon.isClickable = false

//                    if (grandTotal <= data[0].maximumDiscount!!.toDouble()) {
//                        couponAmount = grandTotal
//                    } else {
//                        couponAmount = data[0].maximumDiscount!!.toDouble()
//                    }
//
//                    isCouponApply = 1
//                    couponId = data[0].id.toString()
//                    loadCalc()
                }

            }
        }

        /*        binding.couponLayout.setOnClickListener {
                    binding.layoutCoupon.visibility = View.VISIBLE
        //            binding.coupon.text = getString(R.string.apply_coupon)
                    couponAmount = 0.0
                    isCouponApply = 0
                    couponId = ""
                    loadCalc()
                }*/

        binding.walletLayout.setOnClickListener {
            if (grandTotal > walletAmount) {
                UiUtils.showSnack(
                    binding.root,
                    requireContext().resources.getString(R.string.wallet_low)
                )
            } else {
                binding.walletBalance.visibility = View.VISIBLE
                binding.walletBalanceLable.visibility = View.VISIBLE
                paymentType = "WALLET"
                binding.codLayout.setBackgroundResource(R.drawable.rectangle_white_without_border)
                binding.walletLayout.setBackgroundResource(R.drawable.rectangle_border)
                binding.cardLayout.setBackgroundResource(R.drawable.rectangle_white_without_border)
            }
        }

        binding.codLayout.setOnClickListener {
            binding.walletBalance.visibility = View.GONE
            binding.walletBalanceLable.visibility = View.GONE
            paymentType = "CASH"
            binding.codLayout.setBackgroundResource(R.drawable.rectangle_border)
            binding.walletLayout.setBackgroundResource(R.drawable.rectangle_white_without_border)
            binding.cardLayout.setBackgroundResource(R.drawable.rectangle_white_without_border)
        }
        binding.cardLayout.setOnClickListener {
            if (grandTotal > 0.0) {
                binding.walletBalance.visibility = View.GONE
                binding.walletBalanceLable.visibility = View.GONE
                paymentType = "CARD"
                binding.codLayout.setBackgroundResource(R.drawable.rectangle_white_without_border)
                binding.walletLayout.setBackgroundResource(R.drawable.rectangle_white_without_border)
                binding.cardLayout.setBackgroundResource(R.drawable.rectangle_border)
            }

        }

        binding.book.setOnClickListener {
            DialogUtils.showDialog(
                requireContext(),
                "",
                "Are you Sure to Place an Order?",
                requireContext().resources.getString(R.string.ok),
                requireContext().resources.getString(R.string.cancel),
                object :
                    DialogCallBack {
                    override fun onPositiveClick(toString: String) {
                        if (cartDataList.isNotEmpty()) {

                            var cartoonproducts = cartDataList.filter { it!!.cartonActive == 1 }
                            Log.e("appSample", "CartoonCount: " + cartoonproducts.size)
                            userCartonDiscount = cartoonproducts.sumBy { it!!.quantity.toInt() }
//                            val userCartonDiscount = cartDataList.filter { it!!.cartonActive == 1 }.sumBy { it.quantity.toInt() }
                            Log.e("appSample", "userCartonDiscount: $userCartonDiscount")
                            Log.e("appSample", "maxCartonDiscountPerDay: $maxCartonDiscountPerDay")
                            Log.e("appSample", "defaultCartonDiscount: $defaultCartonDiscount")
                            Log.e("appSample", "defaultMaxCartonDiscountPerDayUser: $defaultMaxCartonDiscountPerDayUser")
                            Log.e("appSample", "defaultMaxCartonDiscountPerDayEmployee: $defaultMaxCartonDiscountPerDayEmployee")


//                            if (cartoonproducts.size > maxCartonDiscountPerDay) {

//                            if(maxCartonDiscountPerDay == 0 && )
                            cartonDiscountedCount = if (!Constants.User?.usertype.equals("EMPLOYEE") ) MaxCartonDiscountPerDayUser - userCartonDiscount else maxCartonDiscountPerDay-userCartonDiscount

                            if(Constants.User?.usertype.equals("EMPLOYEE") && maxCartonDiscountPerDay == 0) {

                                var discountType = if(Constants.User?.usertype.equals("EMPLOYEE") ) {
                                    defaultMaxCartonDiscountPerDayEmployee
                                } else {
                                    defaultMaxCartonDiscountPerDayUser
                                }

                                DialogUtils.showAlert(
                                    requireContext(),
                                    object :
                                        SingleTapListener {
                                        override fun singleTap() {

                                        }
                                    },
                                    requireContext().resources.getString(R.string.daily_limit_one)
                                            + " $discountType " + requireContext().resources.getString(R.string.daily_limit_two)
                                    + " $maxCartonDiscountPerDay " + requireContext().resources.getString(R.string.daily_limit_three),
                                    "Oops"
                                )

                            }
                            else if (!Constants.User?.usertype.equals("EMPLOYEE") && MaxCartonDiscountPerDayUser<=0) {

                                var discountType = if(Constants.User?.usertype.equals("EMPLOYEE") ) {
                                    defaultMaxCartonDiscountPerDayEmployee
                                } else {
                                    defaultMaxCartonDiscountPerDayUser
                                }

                                DialogUtils.showAlert(
                                    requireContext(),
                                    object :
                                        SingleTapListener {
                                        override fun singleTap() {

                                        }
                                    },

                                    requireContext().resources.getString(R.string.cart_limit_one) +
                                            " $userCartonDiscount " + requireContext().resources.getString(R.string.cart_limit_two) +
                                            " $maxCartonDiscountPerDay " + requireContext().resources.getString(R.string.cart_limit_three) +
                                            " $discountType " + requireContext().resources.getString(R.string.cart_limit_four),
                                    "Oops"
                                )
                            }
                            else {

                                var limitExceed = 0
                                var productName = ""
                                for (item in cartDataList) {
                                    val limit = item!!.quantity
                                    val stock = item.stock

                                    if(limit > stock) {
                                        limitExceed = 1;
                                        productName = item.enProductName!!
                                        break;
                                    }
                                }

                                if(limitExceed == 1) {
                                    DialogUtils.showAlert(
                                        requireContext(),
                                        object :
                                            SingleTapListener {
                                            override fun singleTap() {

                                            }
                                        },
                                        productName + " " + requireContext().resources.getString(R.string.out_of_stock_error_message),
                                        "Oops"
                                    )
                                } else {

                                    notes = binding.notes.text.toString()
                                    if (paymentType == "CARD") {
                                        val args = Bundle()
                                        args.putString("from", "order")
                                        args.putString("amt", grandTotal.toString())
                                        findNavController().navigate(
                                            R.id.action_cart_fragment_to_payment_fragment,
                                            args
                                        )
                                    } else {
                                        if (checkDeliveryDateIsHoliday(holidays, deliveryDate)) {
                                            // Present
                                            DialogUtils.showAlert(
                                                requireContext(),
                                                object :
                                                    SingleTapListener {
                                                    override fun singleTap() {
                                                    }
                                                },
                                                requireContext().resources.getString(R.string.holiday_alert),
                                                "Oops"
                                            )
                                        } else {
                                            // Not-Present

                                            if(binding.employeeCouponSwitch.isChecked) {
                                                // checked
                                                var cartoonDiscount = empCartonDiscount - userCartonDiscount
                                                employeeCartonDiscount = if (cartoonDiscount < 0) {
                                                    empCartonDiscount
                                                } else {
                                                    cartoonDiscount
                                                }
                                                cartonDiscountedCount = maxCartonDiscountPerDay - userCartonDiscount
//                                                employeeCartonDiscount = cartoonDiscount
//                                                cartonDiscountedCount = userCartonDiscount - maxCartonDiscountPerDay
                                            } else {
                                                // not Checked
                                                employeeCartonDiscount = empCartonDiscount
                                            }

                                            Log.e("appSample", "cartonDiscountedCount: $cartonDiscountedCount")
//                                            employeeCartonDiscount = cartonDiscountedCount - userCartonDiscount
                                            DialogUtils.showLoader(requireContext())
                                            productViewModel.placeOrder(
                                                requireContext(),
                                                cartonDiscountedCount,
                                                cartId,
                                                orderType,
                                                orderData,
                                                paymentType,
                                                grandTotal,
                                                vatAmount,
                                                pickupAddress, notes,
                                                loyaltyAmount,
                                                isLoyaltyApply,
                                                deliveryFullAddress, lat, lng,
                                                couponId,
                                                couponAmount,
                                                deliveryCharge,
                                                deliveryDate,
                                                userCartonDiscount,
                                                employeeCartonDiscount
                                            ).observe(viewLifecycleOwner) { it ->
                                                it?.let {
                                                    if (it.success!!) {
                                                        UiUtils.showSnack(binding.root, it.massage!!)
                                                        requireActivity().onBackPressed()
                                                    } else {
                                                        // UiUtils.showSnack(binding.root, it.massage!!)
                                                    }
                                                }

                                                DialogUtils.dismissLoader()
                                            }
                                        }
                                    }

                                }



                            }


                        }

                    }

                    override fun onNegativeClick() {

                    }
                })
        }

        return binding.root
    }

    private fun employeeCouponSwitchOnClick() {
        if (binding.employeeCouponSwitch.isChecked) {
            var totDisc = emplyeeDiscountca()
            if (totDisc == 0.0) {
                binding.employeeCouponSwitch.isChecked = false
                UiUtils.showToast(
                    requireContext(),
                    "No carton Products Added in the Cart",
                    false
                )
            } else {
                if (totDisc > totalAmount) {
                    employeDiscount = 0.0
                    cartonDiscountedCount = 0
                    DialogUtils.showAlert(requireContext(), object :
                        SingleTapListener {
                        override fun singleTap() {
//                            findNavController().navigate(R.id.action_login_fragment_self)
//                            DialogUtils
                        }

                    }, requireContext().resources.getString(R.string.your_amt_is_low))
                    binding.employeeCouponSwitch.isChecked = false
                } else {
                    loadCalc()
                }
            }
        } else {
            employeDiscount = 0.0
            cartonDiscountedCount = 0
            loadCalc()
//                UiUtils.showToast(requireContext(),"off",false)
        }
    }

    private fun checkDeliveryDateIsHoliday(
        holidays: ArrayList<String>,
        deliveryDate: String
    ): Boolean {
        val originalDateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.ENGLISH)
        val originalDate: Date = originalDateFormat.parse(deliveryDate) ?: Date()

        // Format the date into the desired format
        val desiredDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val formattedDate: String = desiredDateFormat.format(originalDate)

        return holidays.contains(formattedDate)
    }

    private fun getcoupon() {
        productViewModel.getCouponList(requireContext()).observe(viewLifecycleOwner) { it ->
            it?.let {
                if (it.success!!) {
                    if (it.data!!.isNotEmpty()) {
                        CouponData = it.data as ArrayList<CouponListModel.Data>
                        /*    binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
                            binding.recycler.adapter = CouponListAdapter(requireContext(),it.data!!,object : OnClickListener {
                                override fun onClickItem(position: Int) {
                                    binding.layoutCoupon.visibility = View.GONE
                                    binding.coupon.text = "Coupon Applied"
                                    couponAmount = it.data!![position].maximumDiscount!!.toDouble()
                                    isCouponApply = 1
                                    couponId =it.data!![position].id.toString()
                                    loadCalc()
                                }
                            })*/
                    }
                } else {
                    // UiUtils.showSnack(binding.root, it.massage!!)
                }
            }

            DialogUtils.dismissLoader()
        }

    }

    private fun getCart() {

        productViewModel.getCart(requireContext()).observe(viewLifecycleOwner) { it ->
            Log.d(ContentValues.TAG, "categorydata: $it")
            it?.let { it ->
                it.error?.let { error ->
                    if (error) {
                        it.message?.let { msg ->
                            UiUtils.showSnack(binding.root, msg)
                        }
                    } else {
                        if (it.data.isNullOrEmpty()) {
                            binding.apply {
                                nestedScrollView4.visibility = View.GONE
                                constraintLayout6.visibility = View.GONE
                                emptyLayout.visibility = View.VISIBLE

                            }
                        }
                        it.data?.let { data ->
                            if (it.totalCartCount != null) {
                                productViewModel.cartCount.value = it.totalCartCount!!.toInt()
                            } else {
                                productViewModel.cartCount.value = 0
                            }
//                            min = it.adminDate
                            // Todo - 02-11-2024 Kabil Update
                            deliveryDate = it.adminDate
                            max = it.maxDeliveryDateCanChoose

                            if (!it.canCalendarShowForDelivery!!) {
                                binding.deliveryDateCard.isEnabled = false
                                binding.deliveryDateCard.isClickable = false
                                binding.deliveryDateLable.text =
                                    resources.getString(R.string.delivery_date)
                            }

                            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            cal.add(Calendar.DATE, min)
                            val today = cal.timeInMillis
                            val dateFormatter = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.ENGLISH)
                            var date = dateFormatter.format(Date(today)).toString()
                            Log.d("TAG", "onCreateView:10 min= $min max= $max $date")
                            deliveryDate = date
                            binding.delveryDate.text = deliveryDate.toString()
                            binding.deliveryDateText.text = deliveryDate.toString()
                            Log.e("appSample", "Value: ${it.cartonDiscount.toString()}")
                            binding.employeeCoupon.text =
                                (resources.getString(R.string.you_have_pending)).toString().replace(
                                    "$",
                                    it.cartonDiscount.toString()
                                )

                            if(it.cartonDiscount.toString() == "0") {
                                binding.employeeDiscCard.visibility = View.GONE
                                binding.employeeDiscLabel.visibility = View.GONE
                            }

                            binding.walletBalance.text = it.walletBalance.toString() + " BD"
                            cartoonDiscount = it.cartonDiscount
//                            adminDate = it.adminDate
                            loyaltyPoint = it.loyaltyPoint
                            binding.loyaltyPoints.text = loyaltyPoint.toString()
                            Log.e("appSample", "Data: ${loyaltyPoint.toString()}")
                            walletAmount = it.walletBalance.toDouble()
                            vat = it.vat!!.toDouble()
                            maxCartonDiscountPerDay = it.maxCartonDiscountPerDay
                            defaultMaxCartonDiscountPerDayUser = it.defaultMaxCartonDiscountPerDayUser
                            defaultMaxCartonDiscountPerDayEmployee = it.defaultMaxCartonDiscountPerDayEmployee
                            isCardPayment = it.isCardPayment
                            isCod = it.isCod
                            isSelfPickup = it.isSelfPickup
                            isDelivery = it.isDelivery
                            defaultCartonDiscount = it.defaultCartonDiscount
                            holidays = ArrayList(it.holidayList)

                            Log.e("appSample", "defaultMaxCartonDiscountPerDayUser: ${defaultMaxCartonDiscountPerDayUser.toString()}")
                            Log.e("appSample", "defaultMaxCartonDiscountPerDayEmployee: ${defaultMaxCartonDiscountPerDayEmployee.toString()}")
                            Log.e("appSample", "defaultCartonDiscount: ${defaultCartonDiscount.toString()}")
                            Log.e("appSample", "maxCartonDiscountPerDay: $maxCartonDiscountPerDay")

                            /*for (holiday in holidays) {
                                Log.e("appSample", "Data: $holiday")
                            }*/
                            empDefaultCartonDiscount = it.defaultCartonDiscount
                            empCartonDiscount = it.cartonDiscount
                            MaxCartonDiscountPerDayUser = it.maxCartonDiscountPerDayUser
                            if(isDelivery == 0 && isSelfPickup == 0) {
                                binding.doorDelivery.visibility = View.GONE
                                binding.pickup.visibility = View.GONE
                                binding.pickupLayout.visibility = View.GONE
                                binding.deliveryTypeLable.visibility = View.GONE
                            }

                            if(isDelivery == 1) {
                                binding.doorDelivery.visibility = View.VISIBLE
                            } else {
                                binding.doorDelivery.visibility = View.GONE
                            }

                            if(isSelfPickup == 1) {
                                binding.pickup.visibility = View.VISIBLE
                            } else {
                                binding.pickup.visibility = View.GONE
                            }

                            if(isCod == 0 && isCardPayment == 0) {
                                binding.codLayout.visibility = View.GONE
                                binding.cardLayout.visibility = View.GONE
                                binding.paymentMethodLable.visibility = View.GONE
                            }

                            if(isCod == 1) {
                                binding.codLayout.visibility = View.VISIBLE
                            } else {
                                binding.codLayout.visibility = View.GONE
                            }

                            if(isCardPayment == 1) {
                                binding.cardLayout.visibility = View.VISIBLE
                            } else {
                                binding.cardLayout.visibility = View.GONE
                            }

                            if (it.address != null && orderType == "PICKUP") {
                                pickupAddress =
                                    it.address!!.address + "," + it.address!!.buildingName + "," + it.address!!.address + "," + it.address!!.pin
                                binding.address.text = pickupAddress
                                lat = it.address!!.latitude!!
                                lng = it.address!!.longitude!!
                            }

                            if (it.address != null && orderType == "DELIVERY") {
                                binding.address.text = deliveryAddress
                            }

                            if (it.data != null && it.data!!.isNotEmpty()) {
                                cartDataList = it.data!!
                                totalAmount = 0.0
                                vatAmount = 0.0
                                it.data!!.map {
                                    it!!.totprice = it.price * it.quantity
                                    Log.d("TAG", "getCart:${it.vat} ")
                                    it.vatPrice = it.totprice * (it.vat.toDouble() / 100)
                                    totalAmount += it.totprice
                                    vatAmount += it.vatPrice
                                }
                                productRecyc(data)
                            }

                            if(binding.employeeCouponSwitch.isChecked) {
                                binding.employeeCouponSwitch.performClick()
                                binding.employeeCouponSwitch.performClick()
                            }

                        }
                    }
                }
            }

            DialogUtils.dismissLoader()
        }
    }

    private fun updateCart(cartId: Int, quantity: Int) {
        productViewModel.updateCart(requireContext(), cartId, quantity)
            .observe(
                viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.massage?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            if (it.data != null || it.data!!.equals("{}"))
                                productViewModel.cartCount.value = it.totalCartCount

                            DialogUtils.showLoader(requireContext())
                            getCart()
                        }
                    }

                }
            }
    }

    private fun productRecyc(data: List<CartResponseNew.Data?>) {
        loadCalc()
        binding.productRecyc.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.productRecyc.scheduleLayoutAnimation()
        binding.productRecyc.adapter = CartAdapter(0, requireContext(),
            data as ArrayList<CartResponseNew.Data?>, ArrayList(), object :
                OnClickListnereWithType {
                override fun onClickItem(position: Int, type: String) {
                    if (type == "add") {
                        updateCart(data[position]!!.id!!, data[position]!!.quantity)
                    } else if (type == "sub") {
                        updateCart(data[position]!!.id!!, data[position]!!.quantity)
                    }
                }
            })
    }

    fun loadCalc() {
        grandTotal =
            (totalAmount) + (vatAmount) + (deliveryCharge) - (loyaltyAmount + couponAmount) - employeDiscount

        binding.totAmt.text = String.format(Locale.US,"%.2f", grandTotal) + " BD"

        cartId = JSONArray()
        orderData = JSONArray()
        for (items in cartDataList) {
            cartId.put(items!!.id!!.toString())
            var json = JSONObject()
            if (!items.weight.isNullOrEmpty()) {
                json.put("title", items.name!! + " " + items.weight)
            } else {
                json.put("title", items.name!!)
            }
            json.put("price", String.format(Locale.US,"%.2f", items.totprice) + " BD")
            orderData.put(json)
        }

        var json = JSONObject()
//        json.put("title", "Products Amount")
//        json.put("price", String.format("%.2f", totalAmount) + " BD")
//        orderData.put(json)

        if (vatAmount != 0.0) {
            json = JSONObject()
            json.put("title", getString(R.string.vat))
            json.put("price", String.format(Locale.US,"%.2f", vatAmount) + " BD")
            orderData.put(json)
        }


        if (deliveryCharge != 0.0) {
            json = JSONObject()
            json.put("title", getString(R.string.delivery_fees))
            json.put("price", String.format(Locale.US,"%.2f", deliveryCharge) + " BD")
            orderData.put(json)
        }

        if (isLoyaltyApply == 1) {
            json = JSONObject()
            json.put("title", getString(R.string.loyalty))
            json.put("price", "-" + String.format(Locale.US,"%.2f", loyaltyAmount) + " BD")
            orderData.put(json)
        }

        if (isCouponApply == 1) {
            json = JSONObject()
            json.put("title", getString(R.string.coupon_discount))
            json.put("price", "-" + String.format(Locale.US,"%.2f", couponAmount) + " BD")
            orderData.put(json)
        }
        if (employeDiscount != 0.0) {
            json = JSONObject()
            json.put("title", getString(R.string.employee_discount))
            json.put("price", "-" + String.format(Locale.US,"%.2f", employeDiscount) + " BD")
            orderData.put(json)
        }
        json = JSONObject()
        json.put("title", getString(R.string.total_amount))
        json.put("price", String.format(Locale.US,"%.2f", grandTotal) + " BD")
        orderData.put(json)
        Log.e("fghj", "" + orderData)

        binding.recyclerBilling.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerBilling.adapter = BillingAdapter(requireContext(), orderData)
    }

    private fun emplyeeDiscountca(): Double {
        var cartoonproducts = cartDataList.filter { it!!.cartonActive == 1 }
        Log.d("TAG", "emplyeeDiscountca: " + cartoonproducts)
        if (Constants.User?.usertype.equals("EMPLOYEE")) {
            if (!cartoonproducts.isNullOrEmpty()) {
                var current_cartoonDiscount = cartoonDiscount
                for (cartoonproduct in cartoonproducts) {
                    if (current_cartoonDiscount > 0) {
                       /* if (cartoonproduct!!.weight?.toDoubleOrNull() == null) {
                        } else {*/

                            var item =  cartoonproduct!!
                            var quantity = if (item.quantity > current_cartoonDiscount)  {
                                current_cartoonDiscount
                            } else {
                                item.quantity
                            }
                            current_cartoonDiscount -= quantity
                            val weight = item.weight!!.filter { it.isDigit() }

                            var totalWeight = weight.toDouble() * item.noOfPieces * quantity.toDouble()
                            var kilograms = totalWeight / 1000
                            var discountFills = kilograms * 100
                            var discountBD = discountFills / 1000
                            employeDiscount += discountBD

                            /*var totalWeight =
                                (cartoonproduct!!.weight!!.toDouble() * cartoonproduct.noOfPieces)
                            var kilograms = totalWeight / 1000
                            var discountFills = kilograms * 100
                            var discountBD = discountFills / 1000
                            employeDiscount += discountBD
                            current_cartoonDiscount -= 1
                            cartonDiscountedCount += 1*/
//                        }
                    } else {
                        break;
                    }

                }
                return employeDiscount

            } else {

                return 0.0

            }
        }
        return 0.0
    }

    fun openDatePicker(fragment: FragmentManager, min: Int, max: Int) {
        UiUtils.calendar.timeInMillis = UiUtils.today
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        cal.timeInMillis = today
        cal.set(cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE] + min)
        val from = cal.timeInMillis
        cal.timeInMillis = today
        cal.set(cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE] + (min + max))
        val to = cal.timeInMillis


        var date = ""
        val dateValidatorMin: CalendarConstraints.DateValidator =
            DateValidatorPointForward.from(from)
        val dateValidatorMax: CalendarConstraints.DateValidator =
            DateValidatorPointBackward.before(to)

        val listValidators = java.util.ArrayList<CalendarConstraints.DateValidator>()
        listValidators.add(dateValidatorMin)
        listValidators.add(dateValidatorMax)
        val validators = CompositeDateValidator.allOf(listValidators)
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(from)
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setStart(from).setEnd(to).setValidator(validators)
                        .build()
                )
                .setTitleText("Select date")
                .build()
        datePicker.show(fragment, "tag")
        datePicker.addOnPositiveButtonClickListener {
            // Respond to positive button click.
            val dateFormatter = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.ENGLISH)
            UiUtils.dateSelected = true
            date = dateFormatter.format(Date(it)).toString()
            Log.d("TAG", "onCreateView:11 $date")
            deliveryDate = date
            binding.delveryDate.text = deliveryDate.toString()
            binding.deliveryDateText.text = deliveryDate.toString()
        }
        datePicker.addOnNegativeButtonClickListener {
            // Respond to negative button click.
            datePicker.dismiss()
        }
    }


}