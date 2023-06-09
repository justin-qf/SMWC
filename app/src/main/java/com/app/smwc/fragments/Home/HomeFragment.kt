package com.app.smwc.fragments.Home

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.smwc.Activity.CompanyInfo.CompanyInfoActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Activity.OtpActivity.OtpActivity
import com.app.smwc.Activity.ScannerActivity.ScannerActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.FragmentHomeBinding
import com.app.smwc.fragments.BaseFragment
import kotlin.math.roundToInt

class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var homeAdapter: HomeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app!!.observer.value = Constant.OBSERVER_HOME_FRAGMENT_VISIBLE
        initViews()
        //wrapInCustomShadow(binding.wrapper, R.color.blue)
    }

    private val storeDetails: Unit
        get() {
            binding.rootRecyclerList.layoutManager = LinearLayoutManager(
                act,
                LinearLayoutManager.VERTICAL,
                false
            )
            val listClickListener =
                ListClickListener { _, _, _ ->
                    val intent = Intent(act, MainActivity::class.java)
                    act.startActivity(intent)
                    HELPER.slideEnter(act)
                }

            homeAdapter = HomeAdapter(
                act,
                listClickListener
            )
            binding.rootRecyclerList.adapter = homeAdapter
        }


    private fun wrapInCustomShadow(
        view: View,
        @ColorRes shadowColor: Int,
    ) {

        val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)
        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setTint(shadowColorValue)

        val shadowBlur = view.paddingBottom - 7.toDp(resources)
        shapeDrawable.paint.setShadowLayer(
            shadowBlur,
            0f,
            0f,
            getColorWithAlpha(shadowColorValue, 1.0f)
        )
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

        val radius = 10.toDp(view.context.resources)
        val outerRadius = floatArrayOf(
            radius, radius,
            radius, radius,
            radius, radius,
            radius, radius
        )
        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

        val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        val inset = view.paddingBottom
        drawable.setLayerInset(
            0,
            inset,
            inset,
            inset,
            inset
        )
        view.background = drawable
    }


    private fun getColorWithAlpha(color: Int, ratio: Float): Int {
        val alpha = (Color.alpha(color) * ratio).roundToInt()
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return Color.argb(alpha, r, g, b)
    }

    private fun Int.toDp(resources: Resources): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        )
    }

    private fun initViews() {
        binding.toolbar.ivQrcode.setOnClickListener(this)
        binding.toolbar.ivQrcode.setOnClickListener {
            val i = Intent(act, ScannerActivity::class.java)
            startActivity(i)
            HELPER.slideEnter(act)
        }
        binding.toolbar.ivProfile.setOnClickListener {
            app!!.observer.value = Constant.OBSERVER_PROFILE_VISIBLE_FROM_HOME
        }
        storeDetails
    }

    override fun onClick(view: View?) {
        when (requireView().id) {
            binding.toolbar.ivQrcode.id -> {
                val intent = Intent(act, MainActivity::class.java)
                act.startActivity(intent)
                HELPER.slideEnter(act)
            }
        }
    }
}