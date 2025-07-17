package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentPaymentBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.network.Api.TAG
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel
import com.app.delmon.viewmodel.PaymentViewModel
import com.app.delmon.viewmodel.ProductViewModel
import com.fevziomurtekin.payview.Payview
import com.fevziomurtekin.payview.data.PayModel
import com.mastercard.gateway.android.sdk.GatewayAPI
import com.mastercard.gateway.android.sdk.GatewayCallback
import com.mastercard.gateway.android.sdk.GatewayMap
import com.mastercard.gateway.android.sdk.Session
import com.stripe.android.view.CardInputWidget
import org.json.JSONObject


class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var homeViewModel: PaymentViewModel
    private lateinit var homeFragViewModel: HomeViewModel
    var orderObject:JSONObject = JSONObject()
     var sessionPayment =""
    private lateinit var productViewModel: ProductViewModel
    var from =""
    var amt =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it.getString("from")!!
            amt = it.getString("amt")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        homeFragViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        getsessionData()
        var ordersubObject:JSONObject = JSONObject()
        ordersubObject.put("id","order123")
        ordersubObject.put("amount",amt)
        ordersubObject.put("currency","BHD")


        var authObject:JSONObject = JSONObject()
        authObject.put("acceptVersions","3DS2")
        authObject.put("channel","PAYER_APP")
        authObject.put("purpose","PAYMENT_TRANSACTION")
        orderObject.put("order",ordersubObject)
        orderObject.put("authentication",authObject)

        var cardname =""
        var cardnumber =""
        var cardexpirymonth =""
        var cardexpiryyear =""
        var cardcvv =""
        binding.payview.setOnDataChangedListener(object : Payview.OnChangelistener{
            override fun onChangelistener(payModel: PayModel?, isFillAllComponent: Boolean) {
                Log.d("PayView", "data : ${payModel?.cardOwnerName} \n " +
                        "is Fill all form component : ")
                cardname= payModel?.cardOwnerName!!
                cardnumber= payModel?.cardNo!!
                cardexpirymonth= payModel?.cardMonth.toString()!!
                cardexpiryyear= payModel?.cardYear.toString()!!
                cardcvv= payModel?.cardCv!!
            }

        })

        binding.payview.setPayOnclickListener {
            Log.d(
                "PayView ",
                " clicked. iss Fill all form Component : ${binding.payview.isFillAllComponents}"
            )
            val request = GatewayMap()
                .set("sourceOfFunds.provided.card.nameOnCard", cardname)
                .set("sourceOfFunds.provided.card.number", cardnumber)
                .set("sourceOfFunds.provided.card.securityCode", cardcvv)
                .set("sourceOfFunds.provided.card.expiry.month", cardexpiryyear)
                .set("sourceOfFunds.provided.card.expiry.year", cardexpirymonth)
            // example
            val session = Session(
                id = sessionPayment,
                amount = amt,
                currency = "BHD",
                apiVersion = "75", // api version used to create the session
                orderId = "21" // must match order id used on your server
            )
            GatewayAPI.updateSession(session, request, object : GatewayCallback {
                override fun onError(throwable: Throwable) {
                    Log.d(TAG, "onError: Invalid details")
                    UiUtils.showToast(requireContext(),"Payment Failed",true)
                }

                override fun onSuccess(response: GatewayMap) {
                    if (from=="order") {
                        DialogUtils.showLoader(requireContext())
                        productViewModel.placeOrder(
                            requireContext(),
                            CartFragment.cartonDiscountedCount,
                            CartFragment.cartId,
                            CartFragment.orderType,
                            CartFragment.orderData,
                            CartFragment.paymentType,
                            CartFragment.grandTotal,
                            CartFragment.vatAmount,
                            CartFragment.pickupAddress, CartFragment.notes,
                            CartFragment.loyaltyAmount,
                            CartFragment.isLoyaltyApply,
                            CartFragment.deliveryFullAddress, CartFragment.lat, CartFragment.lng,
                            CartFragment.couponId,
                            CartFragment.couponAmount,
                            CartFragment.deliveryCharge,
                            CartFragment.deliveryDate,
                            CartFragment.userCartonDiscount,
                            CartFragment.employeeCartonDiscount
                        ).observe(viewLifecycleOwner) { it ->
                            it?.let {
                                if (it.success!!) {
//                                    UiUtils.showSnack(binding.root, it.massage!!)
                                    DialogUtils.showDialog(requireContext(),
                                        "",
                                        it.massage!!,
                                        requireContext().resources.getString(R.string.ok),
                                        "",
                                        object :
                                            DialogCallBack {
                                            override fun onPositiveClick(toString: String) {
                                                findNavController().navigate(R.id.action_paymentFragment_to_order_fragment)

                                            }

                                            override fun onNegativeClick() {

                                            }

                                        })
                                } else {
                                    UiUtils.showSnack(binding.root, it.massage!!)
                                }
                                DialogUtils.dismissLoader()
                            }
                        }
                    }else if (from=="wallet"){
                        DialogUtils.showLoader(requireContext())
                        homeFragViewModel.addWalletAmount(requireContext(),amt.toDouble()).observe(viewLifecycleOwner) {
                            if (it.error!!) {
                                Log.d("Error", "addWallet: ${it.toString()} ")
                            } else {
                                findNavController().navigate(R.id.action_paymentFragment_to_walletFragment)
                                Constants.User.Wallet = it.walletAmount.toString()
                                UiUtils.showToast(requireContext(),it.message.toString(),true)

                            }
                            DialogUtils.dismissLoader()
                        }
                    }
                }

            })
        }


        /*  val nameOnCard = binding.
          val request = GatewayMap()
              .set("sourceOfFunds.provided.card.nameOnCard", nameOnCard)
              .set("sourceOfFunds.provided.card.number", cardNumber)
              .set("sourceOfFunds.provided.card.securityCode", cardCvv)
              .set("sourceOfFunds.provided.card.expiry.month", cardExpiryMM)
              .set("sourceOfFunds.provided.card.expiry.year", cardExpiryYY);
  */
        return binding.root
    }

    fun getsessionData(){
        DialogUtils.showLoader(requireContext())
        homeViewModel.getsessionData(requireContext())
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.result?.let { result ->
                        if (result=="SUCCESS") {
                            sessionPayment =it.session!!.id.toString()
                            updatesessionData(it.session!!.id.toString())
                            Log.d(TAG, "getsessionData:sucess  "+it.result+it.merchant+it.session.toString())
                        } else {
                            Log.d(TAG, "getsessionData: Failure")


                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    fun updatesessionData(sessionId: String){
        DialogUtils.showLoader(requireContext())
        homeViewModel.updatesessionData(requireContext(),sessionId,orderObject)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    Log.d(TAG, "updatesessionData:sucess  "+it.result+it.merchant+it.session.toString())

                    it.result?.let { result ->
                        if (result=="SUCCESS") {
                            Log.d(TAG, "getsessionData:sucess  "+it.result+it.merchant+it.session.toString())
                        } else {
                            Log.d(TAG, "getsessionData: Failure")
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }
}