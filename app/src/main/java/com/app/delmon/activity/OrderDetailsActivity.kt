package com.app.delmon.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.delmon.R
import com.app.delmon.adapter.BillingAdapter
import com.app.delmon.adapter.CartAdapter
import com.app.delmon.databinding.ActivityOrderDetailsBinding
import com.app.delmon.interfaces.DialogCallBack
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.BaseUtils
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel
import org.json.JSONArray
import org.json.JSONObject


class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var homeViewModel: HomeViewModel
    var id = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

         id  = intent.getStringExtra("key")!!
        binding.back.setOnClickListener {
            onBackPressed()
        }

        getorder()

    }

    private fun getorder(){
        homeViewModel.getOrder(this,id!!).observe(this) {
            if (it.success!!) {
                if (it.data!!.isNotEmpty()) {
                    binding.productRecyc.layoutManager = GridLayoutManager(this,1, GridLayoutManager.VERTICAL,false)
                    binding.productRecyc.scheduleLayoutAnimation()
                    binding.productRecyc.adapter = CartAdapter(1,this,ArrayList(), it.productDetails!!,object : OnClickListnereWithType {
                        override fun onClickItem(position: Int,type:String) {

                        }
                    })

                    if(it.data!![0].deliveryType == "PICKUP") {
                        binding.address.text = it.data!![0].pickupAddress
                        binding.deliveryLable.text = resources.getString(R.string.pickup_address) //"PickUp Address"
                    }
                    else{
//                        var obj = it.data!![0].deliveryAddress
//                        val jObjResponse =
//                            JSONObject(java.lang.String.valueOf(it.data!![0].deliveryAddress.getJSONObject()))

                        binding.address.text = it.data!![0].deliveryAddress?.area ?: "" +"  "+it.data!![0].deliveryAddress?.address ?: ""

                        binding.deliveryLable.text = "Delivery Address"
                    }

                    if(it.data!![0].paymentTypeId == "CASH"){
                        binding.paymentmethod.text = it.data!![0].paymentTypeId
                    }
                    else{
                        binding.paymentmethod.text = it.data!![0].paymentTypeId
                    }
                    if(it.data!![0].orderStatus=="PLACED" || it.data!![0].orderStatus=="APPROVE"){
                        binding.rejectBtn.visibility = View.VISIBLE
                    }else{
                        binding.rejectBtn.visibility = View.GONE
                    }

                    if(it.data!![0].orderStatus=="COMPLETED"){
                        var orderIdInvoice = it.data!![0].orderId
                        binding.reOrderBtn.visibility = View.VISIBLE
                        binding.invoiceLable.visibility = View.VISIBLE
                        binding.invoiceView.visibility = View.VISIBLE
                        binding.view1.visibility = View.VISIBLE
                        binding.dateLayout.visibility = View.VISIBLE
                        binding.date.text = BaseUtils.getFormatedDateUtc(it.data!![0].updatedAt!!,"yyyy-MM-dd'T'HH:mm:ss.SSSZZZ","EEEE, MMM d, yyyy")
                        binding.invoiceView.setOnClickListener {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("http://15.184.181.76:8080/v1/user/getInvoice/"+orderIdInvoice))
                            startActivity(browserIntent)
                        }

                   }
                        binding.rejectBtn.setOnClickListener {
                            DialogUtils.showDialog(this,"","Are you Sure to Cancel the Order?",this.resources.getString(
                                R.string.ok),this.resources.getString(R.string.cancel),object :
                                DialogCallBack {
                                override fun onPositiveClick(toString: String) {
                                    orderUpdate(id,"USERREJECTED",0)
                                }

                                override fun onNegativeClick() {
                                }
                            })
                    }
                    binding.reOrderBtn.setOnClickListener {
                        DialogUtils.showDialog(this,"","Are you sure to Re Order",this.resources.getString(
                            R.string.ok),this.resources.getString(R.string.cancel),object :
                            DialogCallBack {
                            override fun onPositiveClick(toString: String) {
                                reOrder(id)
                            }

                            override fun onNegativeClick() {
                            }

                        })
                    }

                    val orderData = JSONArray()
                    for(items in it.data!![0].order!!){
                        val map = items as Map<String,String>
                        val json = JSONObject()
                        json.put("title", map["title"]!!)
                        json.put("price", map["price"]!!)
                        orderData.put(json)
                    }
                    binding.recyclerBilling.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                    binding.recyclerBilling.adapter = BillingAdapter(this,orderData)
                }
            } else {
                // UiUtils.showSnack(binding.root, it.message!!)
            }
        }

    }

    private fun orderUpdate(orderId:String, status:String, position: Int){
        homeViewModel.orderupdateStatus(this,orderId,status)
            .observe(this
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.success?.let { error ->
                        if (!error) {
                            it.massage?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            getorder()
                            UiUtils.showSnack(binding.root, it.massage!!)

                        }
                    }

                }
            }
    }
    private fun reOrder(orderId:String){
        homeViewModel.reOrder(this,orderId)
            .observe(this
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.success?.let { error ->
                        if (!error) {
                            it.massage?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            it.massage?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        }
                    }

                }
            }
    }

}