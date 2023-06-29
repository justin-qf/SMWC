package com.vovance.frimline.views

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    var intentFromHome: Int = 0
    var numFormatNew: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    fun getCurrentDate(): Date? {
        val cal = Calendar.getInstance()
        return cal.time
    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: java.lang.Exception) {
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

    fun addDay(date: Date?, i: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DAY_OF_YEAR, i)
        return cal.time
    }

    fun addWeek(date: Date?, i: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.WEEK_OF_YEAR, i)
        return cal.time
    }

    fun setBackgroundColor(activity: Activity, textView: TextView,drawable: Int) {
        return textView.setBackground(
            ContextCompat.getDrawable(
                activity,
                drawable
            )
        )
    }
    fun setBackgroundBtnColor(activity: Activity, textView: Button,drawable: Int) {
        return textView.setBackground(
            ContextCompat.getDrawable(
                activity,
                drawable
            )
        )
    }

    fun setImageBackgroundColor(activity: Activity, textView: ImageView,drawable: Int) {
        return textView.setBackground(
            ContextCompat.getDrawable(
                activity,
                drawable
            )
        )
    }

    fun setLayoutBackgroundColor(activity: Activity, textView: LinearLayout,drawable: Int) {
        return textView.setBackground(
            ContextCompat.getDrawable(
                activity,
                drawable
            )
        )
    }
    fun setRelativeLayoutBackgroundColor(activity: Activity, textView: RelativeLayout, drawable: Int) {
        return textView.setBackground(
            ContextCompat.getDrawable(
                activity,
                drawable
            )
        )
    }

    fun setBtnTextColour(activity: Activity, textView: Button, int: Int) {
        return textView.setTextColor(ContextCompat.getColor(activity, int))
    }
    fun setTextColour(activity: Activity, textView: TextView, int: Int) {
        return textView.setTextColor(ContextCompat.getColor(activity, int))
    }
    fun addMonth(date: Date?, i: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, i)
        return cal.time
    }

    fun addYear(date: Date?, i: Int): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, i)
        return cal.time
    }

    fun getNextDate(date: Date?, type: Int): Date {
        //count is for days
        //type for DAY_OF_MONTH, or DAY_OF_WEEK
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(type, 1)

        return calendar.time
    }


    fun getVideoDuration(
        time: String?,
    ): String? {

        var result: String = ""
        val date: Pair<String, Date>? = parseTime(time)
        return try {
            if (date != null) {
                val cal = Calendar.getInstance()

                cal.time = date.second
                val hours = cal[Calendar.HOUR]
                val minute = cal[Calendar.MINUTE]
                val second = cal[Calendar.SECOND]
                if (hours != 0)
                    result = result + hours + "hr "

                if (minute != 0) {
                    result = result + minute + " Mins "
                }

                if (hours == 0 && second != 0) {
                    result = result + second + " Sec "
                }

            }
            result

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun parseTime(
        time: String?,
    ): Pair<String, Date>? {
        val result: Pair<String, Date>?
        val inputFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        val date: Date?
        return try {
            date = inputFormat.parse(time)
            result = Pair("time", date)
            result

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    fun totalSecond(duration: String): Int {
        val result: Pair<String, Date>? = parseTime(duration)
        if (result != null) {
            val cal = Calendar.getInstance()
            cal.time = result.second
            val hours = cal[Calendar.HOUR]
            var minute = cal[Calendar.MINUTE]
            var second = cal[Calendar.SECOND]
            minute += hours * 60
            second += minute * 60
            return (second)
        }
        return 0
    }

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        var output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return output.format(date)
    }

    fun getYearFromDate(time: String): String {
        //val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = sdf.parse(time)
        val cal = Calendar.getInstance()
        cal.time = date
        val hours = cal[Calendar.YEAR]
        return hours.toString()
    }


    fun parseDate(time: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(time)
        return date
    }


    internal fun getPixels(context: Context, valueInDp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            valueInDp.toFloat(),
            r.displayMetrics
        )
        return px.toInt()
    }

    fun hasNetwork(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
    fun goToAppSettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun isUrl(qrResult: String?): Boolean {
        val url = qrResult ?: return false
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun openUrl(qrResult: String?, context: Context) {
        val url = qrResult ?: return
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun copyToClipboard(qrResult: String?, context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("QR Result", qrResult)
        clipboard.setPrimaryClip(clip)
    }

    fun shareResult(qrResult: String?, context: Context) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, qrResult)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun noNetwork(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = false
        return isConnected
    }

    fun makeStatusBarTransparent(act: Activity) {
        act.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }

    internal fun getPixels(context: Context, valueInDp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, r.displayMetrics)
        return px.toInt()
    }

    internal fun getPixelsSp(context: Context, valueInSp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            valueInSp.toFloat(),
            r.displayMetrics
        )
        return px.toInt()
    }

    internal fun getPixelsSp(context: Context, valueInSp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, valueInSp, r.displayMetrics)
        return px.toInt()
    }

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
                imm.hideSoftInputFromWindow(view.windowToken, 1)
            }
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun shareIntent(act: Activity, message: String) {
        try {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.type = "text/plain"
            act.startActivity(sendIntent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
