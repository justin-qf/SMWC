package com.vovance.omcsalesapp.Common;

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.messaging.FirebaseMessaging
import com.vovance.omcsalesapp.Views.DialogToast
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


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

        fun permissionDialog(act: Activity, userId: String?, listener: () -> Unit) {
            val builder = AlertDialog.Builder(act, R.style.RoundShapeTheme)
            val customLayout = act.layoutInflater.inflate(R.layout.logout_dialog, null)
            val loggedUserId = customLayout.findViewById<TextView>(R.id.userId)
            val cancelBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogCancel)
            val logoutBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogLogout)
            loggedUserId.text = userId
            builder.setView(customLayout)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent_color)
            dialog.setCancelable(false)
            if (!dialog.isShowing)
                dialog.show()
            cancelBtn.setOnClickListener { view: View? ->
                act.onBackPressed()
                dialog.dismiss()
            }
            logoutBtn.setOnClickListener { view: View? ->
                HELPER.print("IS_CLICK", "INNER")
                listener()
                dialog.dismiss()
            }
        }

        fun getFirebaseToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    HELPER.print("FirebaseToken", token.toString())
                } else {
                    HELPER.print(
                        "Fetching FCM registration token failed",
                        task.exception.toString()
                    )
                    return@OnCompleteListener
                }
            })
        }

        fun qrRedirectDialog(
            act: Activity,
            mainMessage: String?,
            isText: Boolean?,
            openListener: () -> Unit,
            copyListener: () -> Unit,
            closeListener: () -> Unit
        ) {
            val builder = AlertDialog.Builder(act, R.style.RoundShapeTheme)
            val customLayout = act.layoutInflater.inflate(R.layout.common_dialog_layout, null)
            val loggedUserId = customLayout.findViewById<TextView>(R.id.userId)
            val closeLayout = customLayout.findViewById<ImageView>(R.id.close)
            val title = customLayout.findViewById<MaterialTextView>(R.id.tvDialogTitle)
            val cancelBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogCancel)
            val logoutBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogLogout)
            if (isText == true) {
                cancelBtn.visibility = View.GONE
            } else {
                cancelBtn.visibility = View.VISIBLE
            }
            loggedUserId.text = mainMessage
            title.text = act.getString(R.string.scan_result)
            cancelBtn.text =
                if (isText == true) act.getString(R.string.close) else act.getString(R.string.copy)
            logoutBtn.text =
                if (isText == true) act.getString(R.string.copy) else act.getString(R.string.open)
            builder.setView(customLayout)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent_color)
            dialog.setCancelable(false)
            dialog.show()
            closeLayout.setOnClickListener {
                closeListener()
                dialog.dismiss()
            }
            cancelBtn.setOnClickListener {
                copyListener()
                dialog.dismiss()
            }
            logoutBtn.setOnClickListener {
                openListener()
                dialog.dismiss()
            }
        }

        fun getImageDialog(
            act: Activity,
            cameraListener: () -> Unit,
            galleryListener: () -> Unit
        ) {
            val builder = AlertDialog.Builder(act, R.style.RoundShapeTheme)
            val customLayout = act.layoutInflater.inflate(R.layout.common_dialog_layout, null)
            val closeLayout = customLayout.findViewById<ImageView>(R.id.close)
            val loggedUserId = customLayout.findViewById<TextView>(R.id.userId)
            val title = customLayout.findViewById<MaterialTextView>(R.id.tvDialogTitle)
            val cancelBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogCancel)
            val logoutBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogLogout)
            loggedUserId.text = act.getString(R.string.chooseSource)
            title.text = act.getString(R.string.choose)
            cancelBtn.text =
                act.getString(R.string.camera)
            logoutBtn.text =
                act.getString(R.string.gallery)
            builder.setView(customLayout)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent_color)
            dialog.setCancelable(false)
            dialog.show()
            cancelBtn.setOnClickListener {
                cameraListener()
                dialog.dismiss()
            }
            logoutBtn.setOnClickListener {
                galleryListener()
                dialog.dismiss()
            }
            closeLayout.setOnClickListener {
                dialog.dismiss()
            }
        }

        fun logoutDialog(
            act: Activity?,
            clickListener: () -> Unit,
        ) {
            val builder = AlertDialog.Builder(act, R.style.RoundShapeTheme)
            val customLayout = act!!.layoutInflater.inflate(R.layout.logout_dialog, null)
            val loggedUserId = customLayout.findViewById<TextView>(R.id.userId)
            val title = customLayout.findViewById<MaterialTextView>(R.id.tvDialogTitle)
            val cancelBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogCancel)
            val logoutBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogLogout)
            loggedUserId.text = act.getString(R.string.logoutMessage)
            title.text = act.getString(R.string.logout)
            cancelBtn.text = act.getString(R.string.cancel)
            logoutBtn.text = act.getString(R.string.ok)
            builder.setView(customLayout)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent_color)
            dialog.setCancelable(false)
            dialog.show()
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
            logoutBtn.setOnClickListener {
                clickListener()
                dialog.dismiss()
            }
        }

        fun commonDialog(
            act: Activity?,
            dialogTitle: String?,
            message: String?,
            isError: Boolean?,
            clickListener: () -> Unit,
        ) {
            val builder = AlertDialog.Builder(act, R.style.RoundShapeTheme)
            val customLayout = act!!.layoutInflater.inflate(R.layout.logout_dialog, null)
            val loggedUserId = customLayout.findViewById<TextView>(R.id.userId)
            val title = customLayout.findViewById<MaterialTextView>(R.id.tvDialogTitle)
            val cancelBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogCancel)
            val logoutBtn = customLayout.findViewById<AppCompatButton>(R.id.btnDialogLogout)
            loggedUserId.text = message
            title.text = dialogTitle
            cancelBtn.visibility = View.GONE
            logoutBtn.visibility = View.GONE
            builder.setView(customLayout)
            val dialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent_color)
            dialog.setCancelable(false)
            dialog.show()
            object : CountDownTimer(
                if (isError == true) Constant.autoDialogDismissTimeInMlSecLow.toLong() else Constant.autoDialogDismissTimeInMlSec.toLong(),
                1000
            ) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    if (dialog.isShowing) {
                        clickListener()
                        dialog.dismiss()
                    }
                }
            }.start()
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
            return text.replace(" ", "").trim()
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

        fun apiResponseErrorDialog(
            act: Activity?,
            title: String?,
            msg: String?,
            isMessage: Boolean?,
            listener: () -> Unit
        ) {
            try {
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
                if (isMessage == true)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialogPermission.dismiss()
                    }, 3000)

            } catch (e: Exception) {
                e.printStackTrace()
            }
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

        fun isNull(str: String?, defStr: String): String {
            if (str != null && str.isNotBlank()) {
                return str
            }
            return defStr
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