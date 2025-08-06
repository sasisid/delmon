package com.app.delmon.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


object UiUtils {
    val today = MaterialDatePicker.todayInUtcMilliseconds()
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    var dateSelected = false
    var tag = javaClass.simpleName

    fun showSnack(view: View, content: String) {
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show()
    }

    fun showLog(TAG: String, content: String) {
        Log.d(TAG, content)
    }
    fun showToast( context: Context , content: String,long: Boolean) {
        if (long){
            Toast.makeText(context,content,Toast.LENGTH_LONG).show()
            Log.d(tag, "showToast: $content ")
        }else{
            Toast.makeText(context,content,Toast.LENGTH_SHORT).show()
            Log.d(tag, "showToast: $content ")
        }
    }


    fun loadImage(imageView: ImageView?, imageUrl: String?, placeHolder: Drawable) {
        if (imageUrl == null || imageView == null) {
            return
        }

        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(placeHolder)
                    .error(placeHolder)
            )
            .into(imageView)

    }

    fun showProgressBar(){

    }

    fun convertDpToPixel(dp: Int, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
    fun openDatePicker(fragment: FragmentManager) : String{
        calendar.timeInMillis = today
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        cal.timeInMillis = today
        cal.set(cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DATE]+ 3)
        val from = cal.timeInMillis
        cal.timeInMillis = today
        cal.set(cal[Calendar.YEAR] , cal[Calendar.MONTH], cal[Calendar.DATE]+8)
        val to = cal.timeInMillis


        var date = ""
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.from(from))
        val dateValidatorMin: DateValidator = DateValidatorPointForward.from(from)
        val dateValidatorMax: DateValidator =
            DateValidatorPointBackward.before(to)

        val listValidators = ArrayList<DateValidator>()
        listValidators.add(dateValidatorMin)
        listValidators.add(dateValidatorMax)
        val validators = CompositeDateValidator.allOf(listValidators)
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(from)
                .setCalendarConstraints(CalendarConstraints.Builder().setStart(from).setEnd(to).setValidator(validators).build())
                .setTitleText("Select date")
                .build()
        datePicker.show(fragment, "tag");
        datePicker.addOnPositiveButtonClickListener {
            // Respond to positive button click.
            val dateFormatter = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.ENGLISH)
            dateSelected = true
            date = dateFormatter.format(Date(it)).toString()
            Log.d("TAG", "onCreateView:11 $date")
        }
        datePicker.addOnNegativeButtonClickListener {
            // Respond to negative button click.
            datePicker.dismiss()
        }
        return date
    }

}
