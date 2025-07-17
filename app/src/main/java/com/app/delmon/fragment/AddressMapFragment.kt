package com.app.delmon.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentAddressMapBinding
import com.app.delmon.network.Api.TAG
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.AddressViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.util.*


class AddressMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    private lateinit var binding: FragmentAddressMapBinding
    private lateinit var addressViewModel: AddressViewModel

    private var lattitude :Double =0.0
    private var longitude :Double =0.0
    private var address :String = ""
    var map: GoogleMap? = null
    var saveAs:String = "home"
    var from:String = ""
    var cityy:String = ""
    var addresss:String = ""
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            lattitude = it?.getDouble("lattitude",0.0)!!
            longitude = it?.getDouble("longitude",0.0)!!
            address = it?.getString("address","")!!
            from = it?.getString("from","")!!
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddressMapBinding.inflate(inflater, container, false)
        addressViewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        binding.changeLayout.visibility = View.VISIBLE

        if (from=="search"){
            locateMyLocation()
        }else{
            getLocation()
        }

        binding.confirm.setOnClickListener {
/*
                val hno = binding.houseNo.text.toString()
                val bname = binding.buildName.text.toString()
                var landmark = binding.landmark.text.toString()
            if (!hno.isNullOrEmpty()&&!bname.isNullOrEmpty()&&!landmark.isNullOrEmpty()) {
                var jsonObject: JSONObject = JSONObject()
                jsonObject.put("address", hno)
                jsonObject.put("buildingName", bname)
                jsonObject.put("landmark", landmark)
                jsonObject.put("address", address)
                jsonObject.put("pin", pin)
                jsonObject.put("saveAs", saveAs)
                jsonObject.put("latitude", "12.22")
                jsonObject.put("longitude", "11.33")
                saveAddress(jsonObject)
            }else{
                UiUtils.showToast(requireContext(),"Please Enter The Details",false)
            }*/
            var args = Bundle()
            args.putString("from","map")
            args.putString("area",addresss.toString())
            args.putString("city",cityy.toString())
            findNavController().navigate(R.id.action_addressMapFragment_to_addressFragment,args)
        }

        binding.search.setOnClickListener {
            findNavController().navigate(R.id.action_addressMapFragment_to_searchAddressFragment,
                Bundle()
            )
        }


        // Inflate the layout for this fragment
        return binding.root
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions()
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map?.isMyLocationEnabled = true
        map?.setOnCameraIdleListener(this)
        locateMyLocation()
    }
    private fun locateMyLocation() {

        Log.d("TAG", "locateMyLocation: ${longitude} $lattitude")
        lattitude.let { lat ->
            longitude.let { lng ->
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 14.0f))
                Log.d(TAG, "locateMyLocation: ${lattitude}")
                getaddress(lattitude,longitude)
            }
        }

    }

    private  fun getaddress(latitude:Double,longitude:Double){
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val list: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
        Log.d("TAG", "getaddress: $list")
        binding.apply {
            if (!list.isNullOrEmpty()){
                address.text = list[0].getAddressLine(0)
                city.text = list[0].locality
                cityy = list[0].locality
                addresss = list[0].getAddressLine(0).toString()
            }

        }
    }







    override fun onCameraIdle() {
        Log.d(TAG, "onCameraIdle: ${map?.cameraPosition?.target?.longitude}")
        getaddress(map?.cameraPosition?.target?.latitude!!,
            map?.cameraPosition?.target?.longitude!!
        )
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            111
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 111) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        lattitude= location.latitude
                        longitude= location.longitude
                        locateMyLocation()
                 /*       mainBinding.apply {
                            tvLatitude.text = "Latitude\n${list[0].latitude}"
                            tvLongitude.text = "Longitude\n${list[0].longitude}"
                            tvCountryName.text = "Country Name\n${list[0].countryName}"
                            tvLocality.text = "Locality\n${list[0].locality}"
                            tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
                        }*/
                    }
                }
            } else {
                UiUtils.showToast(requireContext(),"Please turn on location",false)
//                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

}