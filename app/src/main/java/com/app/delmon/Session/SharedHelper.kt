package com.app.delmon.Session

import android.Manifest.permission
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.app.delmon.R
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson


class SharedHelper(context: Context) {

    private var sharedPreference: SharedPref = SharedPref(context)
    var contextt: Context=context
    var locationManager: LocationManager? = null
    var editor: SharedPreferences.Editor? = null

    var token: String
        get() : String {
            return sharedPreference.getKey("token")
        }
        set(value) {
            sharedPreference.putKey("token", value)
        }

    var fcmToken: String
        get() : String {
            return sharedPreference.getKey("fcmToken")
        }
        set(value) {
            sharedPreference.putKey("fcmToken", value)
        }

    var imageUploadPath: String
        get() : String {
            return sharedPreference.getKey("imageUploadPath")
        }
        set(value) {
            sharedPreference.putKey("imageUploadPath", value)
        }

    var language: String
        get() : String {
            return if (sharedPreference.getKey("language") == "") {
                "ar"
            } else {
                sharedPreference.getKey("language")
            }

        }
        set(value) {
            sharedPreference.putKey("language", value)
        }

    var userType: String
        get() : String {
            return if (sharedPreference.getKey("userType") == "") {
                "USER"
            } else {
                sharedPreference.getKey("userType")
            }

        }
        set(value) {
            sharedPreference.putKey("userType", value)
        }

    var appType: String
        get() : String {
            return if (sharedPreference.getKey("appType") == "") {
                "POULTRY"
            } else {
                sharedPreference.getKey("appType")
            }

        }
        set(value) {
            sharedPreference.putKey("appType", value)
        }

    var id: Int
        get() : Int {
            return sharedPreference.getInt("id")
        }
        set(value) {
            sharedPreference.putInt("id", value)
        }
    var otp: String
        get() : String {
            return sharedPreference.getKey("otp")
        }
        set(value) {
            sharedPreference.putKey("otp", value)
        }

    var mobileNumber: String
        get() : String {
            return sharedPreference.getKey("mobileNumber")
        }
        set(value) {
            sharedPreference.putKey("mobileNumber", value)
        }

    var androidDeviceId: String
        get() : String {
            return sharedPreference.getKey("androidDeviceId")
        }
        set(value) {
            sharedPreference.putKey("androidDeviceId", value)
        }
    var countryCode: String
        get() : String {
            return sharedPreference.getKey("countryCode")
        }
        set(value) {
            sharedPreference.putKey("countryCode", value)
        }

    var loggedIn: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("loggedIn")
        }
        set(value) {
            sharedPreference.putBoolean("loggedIn", value)
        }
    var firstLaunch: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("firstLaunch")
        }
        set(value) {
            sharedPreference.putBoolean("firstLaunch", value)
        }


    var name: String
        get() : String {
            return sharedPreference.getKey("name")
        }
        set(value) {
            sharedPreference.putKey("name", value)
        }
    var firstname: String
        get() : String {
            return sharedPreference.getKey("firstname")
        }
        set(value) {
            sharedPreference.putKey("firstname", value)
        }
    var lastname: String
        get() : String {
            return sharedPreference.getKey("lastname")
        }
        set(value) {
            sharedPreference.putKey("lastname", value)
        }

    var email: String
        get() : String {
            return sharedPreference.getKey("email")
        }
        set(value) {
            sharedPreference.putKey("email", value)
        }

    var dob: String
        get() : String {
            return sharedPreference.getKey("dob")
        }
        set(value) {
            sharedPreference.putKey("dob", value)
        }

    var userImage: String
        get() : String {
            return sharedPreference.getKey("userImage")
        }
        set(value) {
            sharedPreference.putKey("userImage", value)
        }

    var recentSearches: String
        get() : String {
            return sharedPreference.getKey("recentSearches")
        }
        set(value) {
            sharedPreference.putKey("recentSearches", value)
        }

    var isPortFolio: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isPortFolio")
        }
        set(value) {
            sharedPreference.putBoolean("isPortFolio", value)
        }
    var isCertificate: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isCertificate")
        }
        set(value) {
            sharedPreference.putBoolean("isCertificate", value)
        }


    var isProfileimgg: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isProfileimgg")
        }
        set(value) {
            sharedPreference.putBoolean("isProfileimgg", value)
        }




    var isPoster: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isposter")
        }
        set(value) {
            sharedPreference.putBoolean("isposter", value)
        }



    var isPost: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isPost")
        }
        set(value) {
            sharedPreference.putBoolean("isPost", value)
        }



    var isPreview: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isPreview")
        }
        set(value) {
            sharedPreference.putBoolean("isPreview", value)
        }


    var categoryList: ArrayList<String>
        get() : ArrayList<String> {
            val myType = object : TypeToken<List<String>>() {}.type
            val vsl = sharedPreference.getKey("categoryList")

            if (vsl == "") {
                return ArrayList()
            }
            val logs: List<String> = Gson().fromJson<List<String>>(vsl, myType)
            return logs as ArrayList<String>
        }
        set(value) {
            var jsonString = Gson().toJson(value)
            sharedPreference.putKey("categoryList", jsonString)
        }




    var categoryName: String
        get() : String {
            return sharedPreference.getKey("categoryName")
        }
        set(value) {
            sharedPreference.putKey("categoryName", value)
        }


    var settingservice: String
        get() : String {
            return sharedPreference.getKey("settingservice")
        }
        set(value) {
            sharedPreference.putKey("settingservice", value)
        }





    var issubcription: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isPreview")
        }
        set(value) {
            sharedPreference.putBoolean("isPreview", value)
        }



    var balance: Float
        get() : Float {
            return sharedPreference.getFloat("balance")
        }
        set(value) {
            sharedPreference.putFloat("balance", value)
        }

    fun isPermissionsEnabled(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(contextt, permission.ACCESS_FINE_LOCATION)
        val result2 = ContextCompat.checkSelfPermission(contextt, permission.WRITE_EXTERNAL_STORAGE)
        val result3 = ContextCompat.checkSelfPermission(contextt, permission.CAMERA)
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
    }



    fun displayManuallyEnablePermissionsDialog() {
        val builder = AlertDialog.Builder(contextt)
        builder.setMessage(
            """
            We need to access Location for performing necessary task. Please permit the permission through Settings screen.
            
            Select App Permissions -> Enable permission(Location,Storage)
            """.trimIndent()
        )
        builder.setCancelable(false)
        builder.setPositiveButton(contextt.getString(R.string.permit_manually),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", contextt.getPackageName(), null)
                intent.data = uri
                contextt.startActivity(intent)
            })
        builder.setNegativeButton(contextt.getString(R.string.cancel), null)
        builder.show()
    }



    /*private boolean haveNetworkWithwifi(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }*/
    fun isGpsEnabled(): Boolean {
        locationManager = contextt.getApplicationContext()
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            false
        } else {
            true
        }
    }
//    fun gpsEnableRequest() {
//        val locationRequest = LocationRequest.create()
//        locationRequest.interval = 10000
//        locationRequest.fastestInterval = 5000
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//        val settingsClient = LocationServices.getSettingsClient(Activity())
//        val task = settingsClient.checkLocationSettings(builder.build())
//        task.addOnSuccessListener(
//            Activity(),
//            OnSuccessListener<LocationSettingsResponse?> { // Add polylines to the map.
//                // Polylines are useful to show a route or some other connection between points.
//                // Position the map's camera near Alice Springs in the center of Australia,
//                // and set the zoom factor so most of Australia shows on the screen.
//                setIsGps(true)
//            })
//        task.addOnFailureListener(Activity(), OnFailureListener { e ->
//            if (e is ResolvableApiException) {
//                try {
//                    setIsGps(false)
//                    /* if(isgps1() == false){
//                            nogps1(activity);
//                        }*/e.startResolutionForResult(Activity(), UrlHelper.GPS_REQUEST_CODE)
//                } catch (e1: SendIntentException) {
//                    e1.printStackTrace()
//                }
//            }
//        })
//    }
//
//
//    fun setIsGps(isGps: Boolean) {
//        editor!!.putBoolean(UrlHelper.IS_GPS, isGps)
//        editor!!.commit()
//    }




    var istoday: String
        get() : String {
            return sharedPreference.getKey("istoday")
        }
        set(value) {
            sharedPreference.putKey("istoday", value)
        }



    var iseditpost: String
        get() : String {
            return sharedPreference.getKey("iseditpost")
        }
        set(value) {
            sharedPreference.putKey("iseditpost", value)
        }



    var PostId: Int
        get() : Int {
            return sharedPreference.getInt("PostId")
        }
        set(value) {
            sharedPreference.putInt("PostId", value)
        }



    var couponprice: Int
        get() : Int {
            return sharedPreference.getInt("couponprice")
        }
        set(value) {
            sharedPreference.putInt("couponprice", value)
        }

    var couponcode: String
        get() : String {
            return sharedPreference.getKey("couponcode")
        }
        set(value) {
            sharedPreference.putKey("couponcode", value)
        }



    var gst: Int
        get() : Int {
            return sharedPreference.getInt("gst")
        }
        set(value) {
            sharedPreference.putInt("gst", value)
        }


    var location: String
        get() : String {
            return sharedPreference.getKey("location")
        }
        set(value) {
            sharedPreference.putKey("location", value)
        }



    var postLat: String
        get() : String {
            return sharedPreference.getKey("postLat")
        }
        set(value) {
            sharedPreference.putKey("postLat", value)
        }

    var postLng: String
        get() : String {
            return sharedPreference.getKey("postLng")
        }
        set(value) {
            sharedPreference.putKey("postLng", value)
        }




    var currentLat: String
        get() : String {
            return sharedPreference.getKey("currentLat")
        }
        set(value) {
            sharedPreference.putKey("currentLat", value)
        }

    var currentLng: String
        get() : String {
            return sharedPreference.getKey("currentLng")
        }
        set(value) {
            sharedPreference.putKey("currentLng", value)
        }




    var taskdescripLat: String
        get() : String {
            return sharedPreference.getKey("taskdescripLat")
        }
        set(value) {
            sharedPreference.putKey("taskdescripLat", value)
        }

    var taskdescripLng: String
        get() : String {
            return sharedPreference.getKey("taskdescripLng")
        }
        set(value) {
            sharedPreference.putKey("taskdescripLng", value)
        }



    var isposttrue: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isposttrue")
        }
        set(value) {
            sharedPreference.putBoolean("isposttrue", value)
        }

    var isBillingExist: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isBillingExist")
        }
        set(value) {
            sharedPreference.putBoolean("isBillingExist", value)
        }


    var emailtosend: String
        get() : String {
            return sharedPreference.getKey("emailtosend")
        }
        set(value) {
            sharedPreference.putKey("emailtosend", value)
        }




    var locationname: String
        get() : String {
            return sharedPreference.getKey("loactionname")
        }
        set(value) {
            sharedPreference.putKey("loactionname", value)
        }



    var locationselected: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("locationselected")
        }
        set(value) {
            sharedPreference.putBoolean("locationselected", value)
        }



    var imagecount: Int
        get() : Int {
            return sharedPreference.getInt("imagecount")
        }
        set(value) {
            sharedPreference.putInt("imagecount", value)
        }



    var isimage: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isimage")
        }
        set(value) {
            sharedPreference.putBoolean("isimage", value)
        }





    var editSellLat: String
        get() : String {
            return sharedPreference.getKey("editSellLat")
        }
        set(value) {
            sharedPreference.putKey("editSellLat", value)
        }

    var editSellLng: String
        get() : String {
            return sharedPreference.getKey("editSellLng")
        }
        set(value) {
            sharedPreference.putKey("editSellLng", value)
        }



    var isService: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("isService")
        }
        set(value) {
            sharedPreference.putBoolean("isService", value)
        }

    var mytaskback: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("mytaskback")
        }
        set(value) {
            sharedPreference.putBoolean("mytaskback", value)
        }



    var ispostsuccess: Boolean
        get() : Boolean {
            return sharedPreference.getBoolean("ispostsuccess")
        }
        set(value) {
            sharedPreference.putBoolean("ispostsuccess", value)
        }



    var isclassiefiedtrue: Boolean
        get() : Boolean {

            return sharedPreference.getBoolean("isclassiefiedtrue")
        }
        set(value) {

            sharedPreference.putBoolean("isclassiefiedtrue", value)

        }


}


