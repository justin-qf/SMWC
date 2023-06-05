package com.app.omcsalesapp.Common;

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.InputFilter
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.HorizontalScrollView
import android.widget.TextView
import com.app.omcsalesapp.Views.DialogToast
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException


class PubFun {
    companion object {

        fun getUsernameValidate(edEmail: TextInputEditText): String {
            val number = edEmail.text!!.toString().toLongOrNull()
            if (number == null && !Patterns.EMAIL_ADDRESS.matcher(edEmail.text!!).matches()) {
                return "Please enter a valid email id"
            } else if (number != null && !Patterns.PHONE.matcher(edEmail.text!!).matches()) {
                return "Please enter a valid mobile number"
            } else if (number != null && (edEmail.text!!.length < 10 || edEmail.text!!.length > 10)) {
                return "Please enter a valid mobile number"
                //return "Your mobile number cannot be less or more than 10 digits"
            }
            return "";
        }

        var numFormatNew: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

        fun formatDate(
            date: String, initDateFormat: String,
            endDateFormat: String
        ): String? {
            var parsedDate: String? = null
            val initDate: Date
            try {
                initDate = SimpleDateFormat(initDateFormat, Locale.getDefault()).parse(date)
                val formatter = SimpleDateFormat(endDateFormat, Locale.getDefault())
                parsedDate = formatter.format(initDate)

            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }

            return parsedDate
        }


        fun getList(list: ArrayList<Long>): String {
            var listId = ""
            return if (list.isEmpty()) {
                ""
            } else {
                list.forEach {
                    listId += ",${it}"
                }
                listId.removePrefix(",")
            }
        }


        fun removeSpaceFromText(text: String): String {
            return text.replace(" ", "").trim().lowercase()
        }

        fun confirmationDialog(
            act: Activity,
            approveRequest: String,
            listener: () -> Unit
        ) {
            val dialogPermission = DialogToast(act)
            dialogPermission.show()
            dialogPermission.holder!!.messageLayout.visibility = View.VISIBLE

            dialogPermission.holder!!.bottomBtnLayout.visibility = View.VISIBLE
            dialogPermission.holder!!.btnDialogGet.visibility = View.GONE
            Objects.requireNonNull(dialogPermission.holder)!!.btnDialogCancel.setOnClickListener { dialogPermission.dismiss() }
            dialogPermission.holder!!.btnDialogLogout.setOnClickListener { _: View? ->
                listener()
                dialogPermission.dismiss()
            }
        }

        fun getCurrentDate(): String? {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            return formatter.format(Date())
        }

        fun getCurrentWithDate(): String? {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            return formatter.format(Date())
        }

        fun getCurrentApiPassDate(): String? {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            return formatter.format(Date())
        }

        fun getTotalAmount(
            packageAmountEdt: TextInputEditText,
            otiChangeEdt: TextInputEditText
        ): Int {
            var totalAmount: Int = 0
            try {
                if (packageAmountEdt.text.toString()
                        .isNotEmpty() && packageAmountEdt.text.toString()
                        .toInt() != 0 && otiChangeEdt.text.toString()
                        .isNotEmpty() && otiChangeEdt.text.toString().toInt() != 0
                ) {
                    totalAmount = (packageAmountEdt.text.toString()
                        .toInt()).plus((otiChangeEdt.text.toString().toInt())) * 100
                } else if (packageAmountEdt.text.toString()
                        .isEmpty() && otiChangeEdt.text.toString()
                        .isNotEmpty() && otiChangeEdt.text.toString().toInt() != 0
                ) {
                    totalAmount = otiChangeEdt.text.toString().toInt() * 100
                } else if (packageAmountEdt.text.toString()
                        .isNotEmpty() && packageAmountEdt.text.toString()
                        .toInt() != 0 && otiChangeEdt.text.toString()
                        .isEmpty()
                ) {
                    totalAmount = packageAmountEdt.text.toString().toInt() * 100
                } else {
                    totalAmount = 1 * 100
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            return totalAmount
        }


        fun getTotalByListAmount(
            packageAmountEdt: Int,
            otiChangeEdt: Int
        ): Int {
            var totalAmount: Int = 0
            try {
                if (packageAmountEdt != null && packageAmountEdt.toString()
                        .isNotEmpty() && otiChangeEdt != null && otiChangeEdt.toString()
                        .isNotEmpty()
                ) {
                    totalAmount = (packageAmountEdt.toString()
                        .toInt()).plus((otiChangeEdt.toString().toInt()))
                } else if (packageAmountEdt != null && packageAmountEdt.toString()
                        .isEmpty() && otiChangeEdt != null && otiChangeEdt.toString()
                        .isNotEmpty()
                ) {
                    totalAmount = otiChangeEdt
                } else if (packageAmountEdt != null && packageAmountEdt.toString()
                        .isNotEmpty() && otiChangeEdt != null && otiChangeEdt.toString()
                        .isEmpty()
                ) {
                    totalAmount = packageAmountEdt.toString().toInt()
                } else {
                    totalAmount = 0
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            return totalAmount
        }

        fun addDate(): String {
            val date = Date()
            var df = SimpleDateFormat("dd-MM-yyyy")
            val c1 = Calendar.getInstance()
            val currentDate = df.format(date) // get current date here
            // now add 30 day in Calendar instance
            c1.add(Calendar.DAY_OF_YEAR, 30)
            df = SimpleDateFormat("dd-MM-yyyy")
            val resultDate = c1.time
            val dueDate = df.format(resultDate)
            // print the result
            HELPER.print("CurrentDate::", currentDate)
            HELPER.print("DueDate::", dueDate)
            return dueDate
        }

        fun addPassingDate(currentDate: String, addDay: String): String {

            var sdf = SimpleDateFormat("dd-MM-yyyy")
            val c = Calendar.getInstance()
            val date = Date()
            val currentDates = sdf.format(date)
            try {
                if (currentDate.isNotEmpty()) {
                    c.time = sdf.parse(currentDate)
                } else {
                    c.time = sdf.parse(currentDates)
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (addDay.isNotEmpty()) {
                c.add(Calendar.DAY_OF_YEAR, addDay.toInt())
            } else {
                c.add(Calendar.DAY_OF_YEAR, 30)
            }
            sdf = SimpleDateFormat("dd-MM-yyyy")
            val resultDate = c.time
            val dueDate = sdf.format(resultDate)

            HELPER.print("DueDate::", dueDate)
            return dueDate
        }

        fun getTotalByLayoutAmount(
            packageAmountEdt: TextInputEditText,
            otiChangeEdt: TextInputEditText
        ): Int {
            var totalAmount: Int = 0
            try {
                if (packageAmountEdt.text != null && packageAmountEdt.text.toString()
                        .isNotEmpty() && otiChangeEdt.text != null && otiChangeEdt.text.toString()
                        .isNotEmpty()
                ) {
                    totalAmount = (packageAmountEdt.text.toString()
                        .toInt()).plus((otiChangeEdt.text.toString().toInt()))
                } else if (packageAmountEdt.text != null && packageAmountEdt.text.toString()
                        .isEmpty() && otiChangeEdt.text != null && otiChangeEdt.text.toString()
                        .isNotEmpty()
                ) {
                    totalAmount = otiChangeEdt.text.toString().toInt()
                } else if (packageAmountEdt.text != null && packageAmountEdt.text.toString()
                        .isNotEmpty() && otiChangeEdt.text != null && otiChangeEdt.text.toString()
                        .isEmpty()
                ) {
                    totalAmount = packageAmountEdt.text.toString().toInt()
                } else {
                    totalAmount = 0
                }
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            return totalAmount
        }

        fun apiResponseErrorDialog(
            act: Activity?,
            title: String?,
            msg: String?,
            listener: () -> Unit
        ) {
            val dialogPermission = DialogToast(act!!)

            if (dialogPermission != null && dialogPermission.isShowing) {
                return
            }
            if (act != null && !dialogPermission.isShowing) {
                dialogPermission.show()
            }

            dialogPermission.holder!!.tvTitle.text = title
            dialogPermission.holder!!.okBtn.visibility = View.VISIBLE
            dialogPermission.holder!!.messageLayout.visibility = View.VISIBLE
            dialogPermission.holder!!.btnDialogLogout.visibility = View.GONE
            dialogPermission.holder!!.btnDialogCancel.visibility = View.GONE
            dialogPermission.holder!!.tvMessage.text = msg
            dialogPermission.holder!!.tvMessage.gravity = Gravity.CENTER
            dialogPermission.holder!!.bottomBtnLayout.visibility = View.VISIBLE
            dialogPermission.holder!!.btnDialogGet.visibility = View.GONE
            dialogPermission.holder!!.btnDialogLogout.setText(R.string.delete)
            dialogPermission.holder!!.okBtn.setOnClickListener {
                dialogPermission.dismiss()
                listener()
            }
//            Handler(Looper.getMainLooper()).postDelayed({
//                dialogPermission.dismiss()
//            }, 2500)
        }

        fun showDialog(context: Activity, title: String, msg: String, listener: () -> Unit) {
            val builder =
                AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
            builder.setTitle(title)
            builder.setMessage(msg)
            val positiveText = context.getString(android.R.string.ok)

            builder.setPositiveButton(
                positiveText
            ) { dialog, which ->
                // positive button logic
                listener()
            }
            val negativeText = context.getString(android.R.string.cancel)
            builder.setNegativeButton(
                negativeText
            ) { dialog, which ->
                // negative button logic
            }
            val dialog = builder.create()
            // display dialog
            dialog.show()
//            Handler(Looper.getMainLooper()).postDelayed({
//                dialog.dismiss()
//            }, 1000)
        }

        fun dpToPx(dp: Float, displayMetrics: DisplayMetrics): Int =
            (dp * displayMetrics.density).toInt()

        fun hideKeyboard(activity: Activity) {
            try {
                val view = activity.currentFocus
                if (view != null) {
                    val imm =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

            } catch (e: Exception) {
                e.stackTrace
            }
        }

        fun showKeyboard(activity: Activity) {
            try {
                val view = activity.currentFocus
                if (view != null) {
                    val imm =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }

        fun getSendDateFormat(date: String): String {
            var senDate = Date()
            var temDate = ""
            val input = SimpleDateFormat("dd MMM yyyy")
            val output = SimpleDateFormat("dd/MM/yyyy")
            try {
                senDate = input.parse(date) // parse input
                temDate = (output.format(senDate)) // format output
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        fun isNull(str: String?, defStr: String): String {
            if (str != null && str.isNotBlank()) {
                return str
            }
            return defStr
        }

        fun addTimeInCurrentTime(
            returnDateFormat: SimpleDateFormat,
            type: Int,
            timeValue: Int
        ): String {
            try {
                val cal = Calendar.getInstance()
                cal.add(type, timeValue)
                return returnDateFormat
                    .format(
                        SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.getDefault())
                            .parse(cal.time.toString())
                    )
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

        fun parseDate(
            time: String?,
            inputPattern: String?,
            outputPattern: String?
        ): String? {
//        String inputPattern = "yyyy-MM-dd";
//        String outputPattern = "dd-MM-yyyy";
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
            val date: Date?
            var str: String? = ""
            try {
                date = inputFormat.parse(time)
                if (date != null) {
                    str = outputFormat.format(date)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return str
        }

        public fun toCamelCase(s: String): String? {
            if (s.length == 0) {
                return s
            }
            val parts = s.split(" ".toRegex()).toTypedArray()
            var camelCaseString = ""
            for (part in parts) {
                camelCaseString = camelCaseString + toProperCase(part) + " "
            }
            return camelCaseString
        }

        public fun toProperCase(s: String): String {
            return s.substring(0, 1).uppercase() +
                    s.substring(1).lowercase()
        }

        fun isInternetConnection(context: Context): Boolean {
            try {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val nw = connectivityManager.activeNetwork ?: return false
                    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                    return when {
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        //for other device how are able to connect with Ethernet
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        //for check internet over Bluetooth
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                        else -> false
                    }
                } else {
                    val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                    return nwInfo.isConnected
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        fun filterEmojisFromEditText(): Array<InputFilter> {
            val emojiFilter =
                InputFilter { source, start, end, dest, dstart, dend ->
                    for (index in start until end) {
                        val type = Character.getType(source[index])
                        if (type == Character.SURROGATE.toInt()
                            || type == Character.NON_SPACING_MARK.toInt()
                            || type == Character.OTHER_SYMBOL.toInt()
                        ) {
                            return@InputFilter ""
                        }
                    }
                    null
                }
            return arrayOf(emojiFilter)
        }

        fun getReadableDate(date: String): String {
            var suffix = "th"
            when (date) {
                "01", "21", "31" -> suffix = "st"
                "02", "22" -> suffix = "nd"
                "03", "23" -> suffix = "rd"
            }
            return date + suffix
        }

        private fun getMonthString(month: Int): String {
            val result = when (month) {
                0 -> "January"
                1 -> "February"
                2 -> "March"
                3 -> "April"
                4 -> "May"
                5 -> "June"
                6 -> "July"
                7 -> "August"
                8 -> "September"
                9 -> "October"
                10 -> "November"
                11 -> "December"
                else -> {
                    "April"
                }
            }
            return result
        }

        fun scrollToFirst(scrollView: HorizontalScrollView, textView: TextView) {
            scrollView.scrollTo(0, textView.selectionStart)
        }
    }


    fun getPixelValue(context: Activity, dimenId: Int): Int {
        val resources: Resources = context.getResources()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dimenId.toFloat(),
            resources.getDisplayMetrics()
        ).toInt()
    }
}