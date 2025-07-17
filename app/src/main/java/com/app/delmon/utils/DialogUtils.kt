package com.app.delmon.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.app.delmon.R
import com.app.delmon.interfaces.DialogCallBack
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

import com.app.delmon.interfaces.SingleTapListener
import com.app.delmon.utils.Constants.RequestCode.Location_INTENT
import kotlin.math.roundToInt


object DialogUtils {

    var loaderDialog: Dialog? = null
    var noInternetDialog: Dialog? = null
    var paymentDialog: Dialog? = null
    private var isShowingDialog = false



    fun showPictureDialog(activity: Activity) {

        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle(activity.getString(R.string.choose_your_option))
        val items =
            arrayOf(activity.getString(R.string.gallery), activity.getString(R.string.camera),"Location")

        alertBuilder.setItems(items) { _, which ->
            when (which) {
                0 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(
                        Constants.Permission.READ_STORAGE_PERM_LIST,
                        Constants.Permission.READ_STORAGE_PERMISSIONS
                    )
                    BaseUtils.openGallery(activity)
                } else {
                    BaseUtils.openGallery(activity)
                }
                1 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(
                        Constants.Permission.CAMERA_PERM_LIST,
                        Constants.Permission.CAMERA_STORAGE_PERMISSIONS
                    )
                    BaseUtils.openCamera(activity)
                } else {
                    BaseUtils.openCamera(activity)
                }
                2 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(
                        Constants.Permission.LOCATION_PERMISSION_PERMISSON_LIST,
                        Constants.Permission.LOCATION_PERMISSION,
                    )
                    val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG)

                    // Start the autocomplete intent.
                    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(activity)
                    activity.startActivityForResult(intent, Location_INTENT)
                } else {

                    val fields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG)

                    // Start the autocomplete intent.
                    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(activity)
                    activity.startActivityForResult(intent, Location_INTENT)                }
            }
        }

        val alert = alertBuilder.create()
        val window = alert.window
        if (window != null) {
            window.attributes.windowAnimations = R.style.DialogAnimation
        }
        alert.show()
    }



  fun showAlert(
        context: Context,
        singleTapListener: SingleTapListener,
        content: String,
        headerTitle: String = "Alert"
    ) {
        var dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_alert_single)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.dialog_line
                )
            )
        )

        var contentView = dialog.findViewById<TextView>(R.id.content)
        var header = dialog.findViewById<TextView>(R.id.header)
        var ok = dialog.findViewById<TextView>(R.id.ok)

        contentView.text = content
        header.text = headerTitle

        ok.setOnClickListener {

            dialog.dismiss()
            singleTapListener.singleTap()
        }

        var width: Int = (context.resources.displayMetrics.widthPixels * 0.9).roundToInt()

        dialog.window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setOnCancelListener {
            singleTapListener.singleTap()
        }

        dialog.show()

    }

    fun showPopUp(
        context: Context,
        singleTapListener: SingleTapListener,
        content: String
    ) {
        var dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.show_popup)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.dialog_line
                )
            )
        )

        var contentView = dialog.findViewById<TextView>(R.id.content)
        var ok = dialog.findViewById<TextView>(R.id.ok)

        contentView.text = content

        ok.setOnClickListener {

            dialog.dismiss()
            singleTapListener.singleTap()
        }


        var width: Int = (context.resources.displayMetrics.widthPixels * 0.9).roundToInt()

        dialog.window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.setOnCancelListener {
            singleTapListener.singleTap()
        }

        dialog.show()

    }

    /*fun showAlertWithHeader(
        context: Context,
        singleTapListener: SingleTapListener,
        content: String,
        headerVal: String
    ) {
        var dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_alert_single)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.transparent
                )
            )
        )

        var contentView = dialog.findViewById<TextView>(R.id.content)
        var header = dialog.findViewById<TextView>(R.id.header)
        var ok = dialog.findViewById<TextView>(R.id.ok)

        contentView.text = content
        header.text = headerVal


        ok.setOnClickListener {

            dialog.dismiss()
            singleTapListener.singleTap()
        }

        var width: Int = (context.resources.displayMetrics.widthPixels * 0.7).roundToInt()

        dialog.window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        dialog.show()

    }
*/


    fun showInputDialog(
        context: Context,
        title: String,
        positiveText: String,
        negativeText: String,
        callBack: DialogCallBack
    ) {
        var dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_alert)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.transparent
                )
            )
        )

        var width: Int = (context.resources.displayMetrics.widthPixels * 0.8).roundToInt()
        var height: Int = (context.resources.displayMetrics.widthPixels * 0.6).roundToInt()
        dialog.window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)


        var headerTextView = dialog.findViewById<TextView>(R.id.header)
        var contentTextView = dialog.findViewById<EditText>(R.id.content)

        var cancel = dialog.findViewById<TextView>(R.id.cancel)
        var ok = dialog.findViewById<TextView>(R.id.ok)

        headerTextView.text = title
//        contentTextView.text = content

        cancel.text = negativeText
        ok.text = positiveText

        cancel.setOnClickListener {
            callBack.onNegativeClick()
            dialog.dismiss()
        }

        ok.setOnClickListener {
            if (contentTextView.text.toString().isNullOrEmpty()){
                UiUtils.showToast(context,context.resources.getString(R.string.please_add_details),false)
            }else{
                contentTextView.text.toString()
                callBack.onPositiveClick(contentTextView.text.toString())
                dialog.dismiss()

            }

        }



        dialog.show()

    }



    fun showDialog(
        context: Context,
        header: String,
        content: String,
        positiveText: String,
        negativeText: String,
        dialogCallback: DialogCallBack
    ) {

        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(header)
        alertDialog.setMessage(content)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(positiveText) { _, _ ->
            dialogCallback.onPositiveClick("")
        }
        alertDialog.setNegativeButton(negativeText) { _, _ ->
            dialogCallback.onNegativeClick()
        }
        alertDialog.show()
    }


    fun showLoader(context: Context) {

        if (loaderDialog != null) {
            if (loaderDialog!!.isShowing) {
                loaderDialog!!.dismiss()
            }
        }

        loaderDialog = Dialog(context)
        loaderDialog?.setCancelable(false)
        loaderDialog?.setCanceledOnTouchOutside(false)
        loaderDialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        loaderDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val inflater = LayoutInflater.from(context)
        val view = inflater?.inflate(R.layout.dialog_loader, null)

        if (view != null) {
            loaderDialog!!.setContentView(view)
            val anim_view: LottieAnimationView = view.findViewById(R.id.anim_view)
            anim_view.speed = 1.0f
            anim_view.playAnimation()
        }

        if (!loaderDialog?.isShowing!!) {
            loaderDialog?.show()
        }

    }


    fun dismissLoader() {
        if (loaderDialog != null) {
            if (loaderDialog!!.isShowing) {
                loaderDialog!!.dismiss()
            }
        }
    }



    //    fun noInternetDialog(context: Context, singleTapListener: SingleTapListener) {
//
//        if (noInternetDialog != null) {
//            if (isShowingDialog) {
//
//            }
//        }
//
//        if (!isShowingDialog) {
//            noInternetDialog = Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
//            noInternetDialog?.setCancelable(false)
//            noInternetDialog?.setCanceledOnTouchOutside(false)
//            noInternetDialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.MATCH_PARENT)
//
//            val inflater = LayoutInflater.from(context)
//            val view = inflater?.inflate(R.layout.dialog_no_internet, null)
//            if (view != null) {
//                noInternetDialog!!.setContentView(view)
//            }





}