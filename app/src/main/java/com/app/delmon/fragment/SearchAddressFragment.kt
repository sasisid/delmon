package com.app.delmon.fragment

import android.content.ContentValues
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.Session.SharedHelper
import com.app.delmon.databinding.FragmentSearchAddressBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.io.IOException
import java.util.*


class SearchAddressFragment : Fragment() {

    private lateinit var binding: FragmentSearchAddressBinding
    var sharedHelper: SharedHelper? = null
    var fcity:String =""
    var faddress:String =""
    var from:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            from = it!!.getString("from","")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchAddressBinding.inflate(inflater, container, false)
        autoPlaceSearch()

        binding.clocation.setOnClickListener {
            findNavController().navigate(R.id.action_searchAddressFragment_to_addressMapFragment,Bundle())
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun autoPlaceSearch(){
        if (!Places.isInitialized()) {
            Places.initialize(context, "AIzaSyDJkkEVjbiTj-xMGkCafmoE4DmXYdRLgS0", Locale.US);
        }
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i(ContentValues.TAG, "Place: ${place.address}, ${place.id}")

                val geocoder = Geocoder(requireContext())
                try {
                    val addresses = place.latLng!!.latitude.let {
                        place.latLng!!.longitude.let { it1 ->
                            geocoder.getFromLocation(
                                it,
                                it1, 1
                            )
                        }
                    }
                    if (addresses != null && addresses.size > 0) {
                        val address =
                            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        val city = addresses[0].locality

                        Log.d("whsduwshuwkanikaak",""+address.toString())

                        if (city!=null) fcity=city
                        faddress = address.toString()
                        fcity = addresses[0].locality.toString()
//                        sharedHelper!!.locationname= fcity as String


                    }

                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
                val args = Bundle()
                args.putDouble("lattitude", place.latLng!!.latitude)
                args.putDouble("longitude", place.latLng!!.longitude)
                args.putString("address", place.address)
                args.putString("city", fcity)
                args.putString("from", "search")
                findNavController().navigate(R.id.action_searchAddressFragment_to_addressMapFragment,args)
            }

            override fun onError(status: Status) {
                Log.i(ContentValues.TAG, "An error occurred: $status")
            }
        })
    }


}