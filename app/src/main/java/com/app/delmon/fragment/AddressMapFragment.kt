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
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.FragmentAddressMapBinding
import com.app.delmon.network.Api.TAG
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.AddressViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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

    companion object {
        private const val REQUEST_LOCATION = 111

        val BAHRAIN_BOUNDS = LatLngBounds(
            LatLng(25.532284, 50.366286), // South West
            LatLng(26.315582, 50.831777)  // North East
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.setLatLngBoundsForCameraTarget(BAHRAIN_BOUNDS)
        map?.setMinZoomPreference(10.0f)
        map?.setOnCameraIdleListener(this)

        // Picked place from search: keep bundle lat/lng; do not replace with device location.
        if (from == "search") {
            if (checkPermissions()) {
                try {
                    map?.isMyLocationEnabled = true
                } catch (e: SecurityException) {
                    Log.e(TAG, "onMapReady: my location", e)
                }
            }
            locateMyLocation()
            return
        }

        if (checkPermissions()) {
            try {
                map?.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                Log.e(TAG, "onMapReady: my location", e)
            }
            getLocation()
        } else {
            requestPermissions()
            // Without this, the map stays at the default (0,0) until the user leaves and returns.
            locateMyLocation()
        }
    }

    private fun locateMyLocation() {
        if (map == null) return
        Log.d("TAG", "locateMyLocation: ${longitude} $lattitude")

        // Default to Manama if coordinates are invalid (0.0, 0.0)
        if (lattitude == 0.0 && longitude == 0.0) {
            lattitude = 26.2235
            longitude = 50.5876
        }
        
        val currentLatLng = LatLng(lattitude, longitude)
        
        if (BAHRAIN_BOUNDS.contains(currentLatLng)) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
            getaddress(lattitude, longitude)
        } else {
            // Default to Manama, Bahrain if outside bounds
            val defaultLat = 26.2235
            val defaultLng = 50.5876
            lattitude = defaultLat
            longitude = defaultLng
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(defaultLat, defaultLng), 14.0f))
            getaddress(defaultLat, defaultLng)
        }
    }

    private fun appLocale(): Locale = Locale(SharedHelper(requireContext()).language)

    @SuppressLint("SetTextI18n")
    private fun getaddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), appLocale())
        val list: List<Address> = try {
            geocoder.getFromLocation(latitude, longitude, 1) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "getaddress", e)
            emptyList()
        }
        Log.d("TAG", "getaddress: $list")
        binding.apply {
            if (list.isNotEmpty()) {
                val a = list[0]
                address.text = a.getAddressLine(0) ?: ""
                val locality = a.locality ?: a.subAdminArea ?: a.adminArea ?: ""
                city.text = locality
                cityy = locality
                addresss = (a.getAddressLine(0) ?: "").toString()
            }
        }
    }







    override fun onCameraIdle() {
        if (map == null) return
        Log.d(TAG, "onCameraIdle: ${map?.cameraPosition?.target?.longitude}")
        map?.cameraPosition?.target?.let {
            getaddress(it.latitude, it.longitude)
        }
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
        // Must use Fragment.requestPermissions so onRequestPermissionsResult runs on this fragment.
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            REQUEST_LOCATION,
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_LOCATION) return
        if (!checkPermissions()) return
        map?.let { m ->
            try {
                m.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                Log.e(TAG, "onRequestPermissionsResult", e)
            }
        }
        getLocation()
    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (!checkPermissions()) {
            requestPermissions()
            locateMyLocation()
            return
        }
        if (!isLocationEnabled()) {
            UiUtils.showToast(requireContext(), "Please turn on location", false)
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            locateMyLocation()
            return
        }
        mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
            val location: Location? = if (task.isSuccessful) task.result else null
            if (location != null) {
                lattitude = location.latitude
                longitude = location.longitude
                locateMyLocation()
            } else {
                // first launch / cold GPS: lastLocation is often null
                requestFreshCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestFreshCurrentLocation() {
        val cts = CancellationTokenSource()
        mFusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cts.token,
        ).addOnCompleteListener(requireActivity()) { task ->
            val loc = if (task.isSuccessful) task.result else null
            if (loc != null) {
                lattitude = loc.latitude
                longitude = loc.longitude
            }
            locateMyLocation()
        }
    }
}