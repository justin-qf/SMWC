package com.vovance.omcsalesapp.Views

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.vovance.securemywillcall.R
import com.google.android.material.textview.MaterialTextView

/**
 * Created by MJ on 29/12/2022.
 */
class ImageToast(context: Activity) : Dialog(context) {
    var holder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.activity_image_dialog)
        holder = ViewHolder(window!!.decorView)
        val lp = WindowManager.LayoutParams()
        val manager = context.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        lp.copyFrom(window!!.attributes)
        val point = Point()
        manager.defaultDisplay.getSize(point)
        lp.width = point.x
        lp.height = point.y
        window!!.attributes = lp
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER)
    }

    inner class ViewHolder internal constructor(view: View) {
        var title: MaterialTextView = view.findViewById(R.id.tvDialogTitle)
        var okBtn: AppCompatButton = view.findViewById(R.id.btnDialogOk)
        var messageLayout: LinearLayoutCompat = view.findViewById(R.id.messageLayout)
        var btnDialogCancel: AppCompatButton = view.findViewById(R.id.btnDialogCancel)
        var bottomBtnLayout: ConstraintLayout = view.findViewById(R.id.bottomBtnLayout)
        var image: ImageView = view.findViewById(R.id.roundedImageView)
        var loader: ProgressBar = view.findViewById(R.id.loader)

    }
}