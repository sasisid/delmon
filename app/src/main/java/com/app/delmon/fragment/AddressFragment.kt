package com.app.delmon.fragment

import android.content.ContentValues
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.AddressResponse
import com.app.delmon.R
import com.app.delmon.adapter.AddressAdapter
import com.app.delmon.databinding.FragmentAddressBinding
import com.app.delmon.interfaces.OnClickListnereWithType
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.AddressViewModel
import com.google.gson.Gson
import org.json.JSONObject
import java.util.ArrayList


class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private lateinit var addressViewModel: AddressViewModel
    private var pin :Int = 0
    private var areaName :String = ""
    private var zoneName : String = ""
    private var areaId :Int = 0
    private var zoneId :Int = 0
    var saveAs:String = "home"
    var mylist = ArrayList<Int>()
    var myZoneNamelist = ArrayList<String>()
    var myAreaNamelist = ArrayList<String>()
    var myZoneIdlist = ArrayList<Int>()
    var myAreaIdlist = ArrayList<Int>()

    companion object{
        var from:String = ""
        var area:String = ""
        var city:String = ""
        var edit:Boolean = false
        var aid:Int = 0

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it?.getString("from")!!
            area = it?.getString("area","")!!
            city = it?.getString("city","")!!
        }
    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.e("appSample", "onBackPressedCallback_CALLED");
            // Handle the back button event
        if (from=="cart") {
            Log.d("TAG", "handleOnBackPressed:oncart ")
            CartFragment.orderType = "PICKUP"
        }
          if (binding.saveeLayout.visibility==View.VISIBLE){
              binding.saveeLayout.visibility=View.GONE
              edit = false
          }else{
              findNavController().popBackStack()
          }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        addressViewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        requireActivity().apply {
            // Redirect system "Back" press to our dispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        }

        binding.apply {
            addAddress.setOnClickListener {
                var args = Bundle()
                args.putString("from","address")
                findNavController().navigate(R.id.action_addressFragment_to_addressMapFragment,args)
            }
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            addAddressBtn.setOnClickListener {
                var args = Bundle()
                args.putString("from","address")
                findNavController().navigate(R.id.action_addressFragment_to_addressMapFragment,args)
            }
            if (from=="cart"){
                addAddressBtn.visibility= View.GONE
                addAddress.visibility= View.GONE
            }
        }

        getAddress()
        getPincode()
        getZone()
        Log.d("TAG", "onCreateView: $from")
        if (from=="map"){
            binding.saveeLayout.visibility=View.VISIBLE
            binding.city.text = city.toString()
            binding.address.text = area.toString()
        }
        updateSaveas("home")
        binding.home.setOnClickListener {
            updateSaveas("home")
        }
        binding.office.setOnClickListener {
            updateSaveas("office")
        }
        binding.other.setOnClickListener {
            updateSaveas("other")
        }
        binding.confirm.setOnClickListener {
            val hno = binding.houseNo.text.toString()
            val bno = binding.blockNo.text.toString()
            val rno = binding.roadNo.text.toString()
            val flat = binding.buildName.text.toString()
            var notes = binding.landmark.text.toString()
            if (!hno.isNullOrEmpty() && !bno.isNullOrEmpty() && !rno.isNullOrEmpty() ) {
                var jsonObject: JSONObject = JSONObject()
                jsonObject.put("blockNo", bno)
                jsonObject.put("houseNo", hno)
                jsonObject.put("roadNo", rno)
                jsonObject.put("pin", pin)
                jsonObject.put("flat", flat)
                jsonObject.put("notes", notes)
                jsonObject.put("saveAs", saveAs)
                jsonObject.put("area", areaName)
                jsonObject.put("zoneId", zoneId)
                jsonObject.put("zoneName", zoneName)
//                jsonObject.put("latitude", "12.22")
//                jsonObject.put("longitude", "11.33")
                if(edit) {
                    DialogUtils.showLoader(requireContext())
                    editAddress(aid,jsonObject)
                } else {
                    DialogUtils.showLoader(requireContext())
                    saveAddress(jsonObject)
                }
            } else {
                UiUtils.showToast(requireContext(),"Enter All the Details",false)
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateSaveas(type:String) {
        when (type) {
            "home" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    saveAs = "home"
                    binding.home.setTextColor(requireContext().getColor(R.color.secondary_color))
                    binding.office.setTextColor(requireContext().getColor(R.color.grey))
                    binding.other.setTextColor(requireContext().getColor(R.color.grey))
                }
            }
            "office" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    saveAs = "office"
                    binding.office.setTextColor(requireContext().getColor(R.color.secondary_color))
                    binding.home.setTextColor(requireContext().getColor(R.color.grey))
                    binding.other.setTextColor(requireContext().getColor(R.color.grey))
                }
            }
            "other" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    saveAs = "other"
                    binding.other.setTextColor(requireContext().getColor(R.color.secondary_color))
                    binding.office.setTextColor(requireContext().getColor(R.color.grey))
                    binding.home.setTextColor(requireContext().getColor(R.color.grey))
                }
            }
        }
    }

    private fun  saveAddress(jsonObject: JSONObject) {
        addressViewModel.addAddress(requireContext(), jsonObject)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "onLoginClicked: $it")
                it?.let {
                    it.error?.let { error ->
                        if(error) {

                        } else {
                            UiUtils.showToast(requireContext(),"Address Saved Successfully",false)
                            binding.saveeLayout.visibility = View.GONE
                            getAddress()
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun  deleteAddress(id:Int) {
        addressViewModel.deleteAddress(requireContext(),id)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "onLoginClicked: $it")
                it?.let {
                    it.error?.let { error ->
                        if(error) {

                        } else {
//                            UiUtils.showToast(requireContext(),"Address Saved Successfully",false)
//                           binding.saveeLayout.visibility = View.GONE
                            getAddress()
                        }
                    }

                }

                DialogUtils.dismissLoader()
            }
    }
    private fun  editAddress(id:Int,jsonObject: JSONObject) {
        addressViewModel.editAddress(requireContext(),id, jsonObject)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "onLoginClicked: $it")
                it?.let {
                    it.error?.let { error ->
                        if(error) {

                        } else {
                            UiUtils.showToast(requireContext(),"Address Updated Successfully",false)
                           binding.saveeLayout.visibility = View.GONE
                            getAddress()
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun getAddress() {
        DialogUtils.showLoader(requireContext())
        addressViewModel.getAddress(requireContext())
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {

                        } else {
                            it.data?.let { data ->
                                if (data.isNullOrEmpty()){
                                    binding.addressLayout.visibility = View.GONE
                                    binding.addressListLayout.visibility = View.GONE
                                    binding.noAddressLayout.visibility = View.VISIBLE
                                }else{
                                    binding.addressLayout.visibility = View.VISIBLE
                                    binding.addressListLayout.visibility = View.VISIBLE
                                    binding.noAddressLayout.visibility = View.GONE
                                    addressRecyc(data)
                                }
                            }
                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    private fun addressRecyc(data: List<AddressResponse.Data?>) {
        binding.addressrecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.addressrecyc.scheduleLayoutAnimation();
        binding.addressrecyc.adapter = AddressAdapter(requireContext(),data,object :
            OnClickListnereWithType {
            override fun onClickItem(position: Int,type:String) {
                if (from=="cart"){
                    Log.d("TAG", "onClickItem:pickupAddress= ${data[position].toString()}")
                    CartFragment.orderType = "DELIVERY"
                    CartFragment.deliveryAddress = data[position]!!.area.toString()
                    CartFragment.deliveryCharge = data[position]!!.deliveryCharge
                    val jsonString = Gson().toJson(data[position]!!)

                    CartFragment.deliveryFullAddress = jsonString.toString()
                    findNavController().popBackStack()

                }
                if (type=="edit") {
                    binding.saveeLayout.visibility=View.VISIBLE
                    binding.city.text = city.toString()
                    binding.address.text = data[position]!!.area.toString()
                    binding.houseNo.setText(data[position]!!.houseNo.toString())
                    binding.roadNo.setText(data[position]!!.roadNo.toString())
                    binding.blockNo.setText(data[position]!!.blockNo.toString())
                    binding.buildName.setText(data[position]!!.flat.toString())
                    binding.landmark.setText(data[position]!!.notes.toString())
                    zoneId = data[position]!!.zoneId!!
                    zoneName = data[position]!!.zoneName!!
                    areaName = data[position]!!.area!!

                    pin = data[position]!!.pin!!
                    var index=mylist.indexOf(data[position]!!.pin!!)
                    binding.pincode.setSelection(index)
                    updateSaveas(data[position]!!.saveAs.toString())
                    edit = true
                    aid = data[position]!!.id!!
                    area = data[position]!!.area!!.toString()
                }
                if (type=="delete"){
                    DialogUtils.showLoader(requireContext())
                    deleteAddress( data[position]!!.id!!)
                }
            }
        })
    }

    fun getArea(zoneId: Int) {
        DialogUtils.showLoader(requireContext())
        addressViewModel.getFindArea(requireContext(), "" + zoneId)
            .observe(viewLifecycleOwner
            ) {
                it?.let {
                    myAreaNamelist.clear()
                    myAreaIdlist.clear()
                    for (item in it.data!!) {
                        // body of loop

                        myAreaNamelist.add(item!!.areaName!!); //this adds an element to the list.
                        myAreaIdlist.add(item.id!!)
                    }

                    loadAreaSpinner(myAreaNamelist)
                }
                DialogUtils.dismissLoader()
            }
    }

    fun getZone() {
        DialogUtils.showLoader(requireContext())
        addressViewModel.getZone(requireContext())
            .observe(viewLifecycleOwner
            ) {
                it?.let {response ->

                    if(response.data != null) {
                        zoneId = response.data!![0]!!.id!!
                        Log.e("appSample", "ZONEID: $zoneId")

                        for (item in it.data!!) {
                            // body of loop
                            myZoneNamelist.add(item!!.name!!); //this adds an element to the list.
                            myZoneIdlist.add(item.id!!)
                        }

                        loadZoneSpinner(myZoneNamelist)
                    }

//                    getArea(zoneId!!)
                }
                DialogUtils.dismissLoader()
            }
    }

    fun getPincode() {
        DialogUtils.showLoader(requireContext())
        addressViewModel.getPincodes(requireContext())
            .observe(viewLifecycleOwner
            ) {
                it?.let {

                    for (item in it.data!!) {
                        // body of loop
                        mylist.add(item!!.pin!!); //this adds an element to the list.

                    }

                    loadSpinner(mylist)
                }
                DialogUtils.dismissLoader()
            }
    }

    fun loadSpinner(mylist: ArrayList<Int>) {

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, mylist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pincode.adapter = adapter

        binding.pincode.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                pin = mylist[position]
//                Toast.makeText(requireContext(),
//                    "" + mylist[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    fun loadAreaSpinner(myArealist: ArrayList<String>) {

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, myArealist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.area.adapter = adapter

        binding.area.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                areaName = myArealist[position]
                areaId = myAreaIdlist[position]

//                Toast.makeText(requireContext(),
//                    "" + myArealist[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun loadZoneSpinner(myZonelist: ArrayList<String>) {
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, myZonelist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.zone.adapter = adapter
        binding.zone.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                zoneName =  myZonelist[position]
                zoneId = myZoneIdlist[position]

                Log.e("appSample", "zoneName: $zoneName")
                Log.e("appSample", "zoneId: $zoneId")
                getArea(zoneId!!)


//                Toast.makeText(requireContext(),
//                    "" + myZonelist[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
}