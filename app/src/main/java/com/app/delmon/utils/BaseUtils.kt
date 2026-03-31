package com.app.delmon.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.app.delmon.Session.SharedHelper
import com.app.delmon.R


import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class BaseUtils {

    companion object {

        @SuppressLint("ObsoleteSdkInt", "NewApi")
         fun setLocale(lang: String,activity: Activity) {
            Log.d("TAG", "setLocale:splash $lang")

            val language = Locale(lang)
            val res: Resources = activity.resources
            val dm: DisplayMetrics = res.displayMetrics
            val conf: Configuration = res.configuration
            conf.locale = language
            res.updateConfiguration(conf, dm)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                res.configuration.setLayoutDirection(language)
                (when (lang) {
                    "ar" -> {
                        View.LAYOUT_DIRECTION_RTL
                    }
                    "hi" -> {
                        View.LAYOUT_DIRECTION_LTR
                    }
                    else -> {
                        View.LAYOUT_DIRECTION_LTR
                    }
                }).also { activity.window.decorView.layoutDirection = it }
            }
        }

        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidMobile(phone: String): Boolean {
            return Patterns.PHONE.matcher(phone).matches()
        }

        fun fromHtml(str: String): String {
            return if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
                .toString() else Html.fromHtml(str).toString()
        }

        fun setMerginToviews(view: View, topMergin: Int, width: Int, height: Int) {
            val layoutParams = LinearLayout.LayoutParams(width, height)
            layoutParams.setMargins(40, topMergin, 40, 0)
            view.layoutParams = layoutParams
        }

        fun setMerginToviews(view: View) {
            val layoutParams = GridLayout.LayoutParams(
              GridLayout.spec(0,3),
                GridLayout.spec(0,3)
            )
            layoutParams.setMargins(40, 20, 40, 0)
            view.layoutParams = layoutParams
        }

        fun method(str: String?): String? {
            var str = str
            if (str != null && str.isNotEmpty()) {
                str = str.substring(0, str.length - 1)
            }
            return str
        }

        fun getCustomColorStateList(context: Context): ColorStateList {
            return ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    ContextCompat.getColor(context, R.color.primary_color),//disabled
                    ContextCompat.getColor(context, R.color.teal_200) //enabled
                )
            )
        }

        fun getCurrentDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }

        fun getDateStringToShow(date: Date, format: String): String {
            try {
                val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH);
                return simpleDateFormat.format(date)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
                return ""
            }
        }

        fun getDateFromString(date: String, format: String): Date? {
            Log.i("StringDate", date)
            val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            try {
                val formatedDate = simpleDateFormat.parse(date)
                Log.i("ParsedDate", formatedDate.toString())
                return formatedDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun setSwitchColor(v: SwitchCompat, context: Context) {
            // thumb color of your choice
            val thumbColor = ContextCompat.getColor(context, R.color.teal_200)

            // trackColor is the thumbColor with 30% transparency (77)
            val trackColor = Color.argb(
                77, Color.red(thumbColor),
                Color.green(thumbColor),
                Color.blue(thumbColor)
            )

            // setting the thumb color
            DrawableCompat.setTintList(
                v.thumbDrawable, ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
                        thumbColor,
                        Color.WHITE
                    )
                )
            )

            // setting the track color
            DrawableCompat.setTintList(
                v.trackDrawable, ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
                        trackColor,
                        Color.parseColor("#4D000000") // full black with 30% transparency (4D)
                    )
                )
            )
        }

        fun isValidEmailAddress(emailAddress: String): Boolean {
            val emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
            val p = Pattern.compile(emailPattern)
            val m = p.matcher(emailAddress)
            return m.matches()
        }

        fun isValidTelephoneNumber(telephoneNumber: String): Boolean {
            return telephoneNumber.length == 9
        }

        fun numberFormat(value: Int): String {
            return "%02d".format(value)
        }

        fun numberFormat(value: Double): String {
            return String.format(Locale.US,"%.2f", value)
        }

        fun getTimeFromTimeStamp(value: String): String {

            var date = Date(value.toLong())
            val simpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)

            return simpleDateFormat.format(date)

        }

        fun navigateToFragment(
            id: Int,
            fragmentName: Fragment,
            fragmentManager: FragmentManager
        ) {
            val fragmentTransaction =
                fragmentManager.beginTransaction()
            fragmentTransaction.replace(id, fragmentName)
            fragmentTransaction.addToBackStack(fragmentName.javaClass.simpleName)
            fragmentTransaction.commit()
        }

        fun openGallery(activity: Activity) {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activity.startActivityForResult(i, Constants.RequestCode.GALLERY_INTENT)
        }

        fun openCamera(activity: Activity) {
            val sharedHelper = SharedHelper(activity)
            val file = getFileTostoreImage(activity)
            val uri: Uri

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(activity.packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            getFileTostoreImage(activity)
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                activity,
                                "com.lia.yello.bilby.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            activity.startActivityForResult(takePictureIntent,
                                Constants.RequestCode.CAMERA_INTENT
                            )
                        }
                    }
                }

            } else {

                Log.d("ertyuio", "poiunmn ")
                uri = Uri.fromFile(file)
                sharedHelper.imageUploadPath = file.absolutePath
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                activity.startActivityForResult(takePictureIntent,
                    Constants.RequestCode.CAMERA_INTENT
                )
            }
        }


        private fun getFileTostoreImage(activity: Activity): File {
            val sharedHelper = SharedHelper(activity)

            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
            val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                Log.d("loliuytre", "zcvxc")

                // Save a file: path for use with ACTION_VIEW intents
                sharedHelper.imageUploadPath = absolutePath
                Log.d(TAG, "getFileTostoreImage: " + sharedHelper.imageUploadPath)
            }
        }

        @SuppressLint("NewApi")
        fun getRealPathFromUriNew(context: Context, uri: Uri): String? {

            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    //  handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)

            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        private fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor =
                    context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
            val result: String?
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(contentURI, filePathColumn, null, null, null)
           Log.d("uygfdcj",""+cursor)
            if (cursor == null) {
                result = contentURI.path
            } else {
                cursor.moveToFirst()
                val idx = cursor
                    .getColumnIndex(filePathColumn[0])

                Log.d("uytdfghjk",""+idx)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
        }

/*
    fun getTimeFromTimeStamp(value: String): String {

        var date = Date(value.toLong())
        val simpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)

        return simpleDateFormat.format(date)

    }

    fun getUtcTime(date: String, dateFormat: String): String {

        var spf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }

        newDate?.let {
            var simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return simpleDateFormat.format(newDate)
        }

        return ""
    }



    fun getFormatedDate(date: String, inputFormat: String, outputFormat: String): String {

        var spf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: ParseException1) {
            e.printStackTrace()
        }


        spf = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        return if (newDate != null)
            spf.format(newDate)
        else
            ""

    }*/

        fun getFormatedDateUtc(
            date: String,
            inputFormat: String?,
            outputFormat: String?
        ): String? {
            var spf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            spf.timeZone = TimeZone.getTimeZone("UTC")
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)


            } catch (e: ParseException) {
                e.printStackTrace()

            }


            spf = SimpleDateFormat(outputFormat, Locale.ENGLISH)

            Log.d("kakakak", "" + spf.format(newDate))
            return spf.format(newDate)

        }

        fun getFormatedDate(date: String, inputFormat: String, outputFormat: String): String {

            var spf = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }


            spf = SimpleDateFormat(outputFormat, Locale.ENGLISH)
            return if (newDate != null)
                spf.format(newDate)
            else
                ""

        }


    }



}




