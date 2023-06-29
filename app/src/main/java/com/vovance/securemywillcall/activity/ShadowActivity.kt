package com.vovance.securemywillcall.activity

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt
import android.graphics.*
import android.util.TypedValue
import com.vovance.securemywillcall.R


class ShadowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow)

        val wrapper = findViewById<FrameLayout>(R.id.wrapper)
       // wrapInCustomShadowWithOffset(wrapper, R.color.teal_700)
        wrapInCustomShadow(wrapper, R.color.blue);
    }


    private fun wrapInCustomShadowWithOffset(
        view: View,
        @ColorRes shadowColor: Int,
    ) {

        val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)

        val shapeDrawable = ShapeDrawable()
        shapeDrawable.setTint(shadowColorValue)
        val shadowBlur = view.paddingBottom - 4.toDp(resources)
        val offset = 4.toDp(resources)
        shapeDrawable.paint.setShadowLayer(
            shadowBlur - offset, //blur
            offset, //dx
            offset, //dy
            getColorWithAlpha(shadowColorValue, 0.8f) //color
        )
        val filter = BlurMaskFilter(offset, BlurMaskFilter.Blur.OUTER)
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.paint)
        shapeDrawable.paint.maskFilter = filter

        val radius = 4.toDp(view.context.resources)
        val outerRadius = floatArrayOf(
            radius, radius, //top-left
            radius, radius, //top-right
            radius, radius, //bottom-right
            radius, radius  //bottom-left
        )
        shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

        val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
        val inset = view.paddingBottom
        drawable.setLayerInset(
            0,
            inset, //left
            inset, //top
            inset , //right
            inset  //bottom
        )
        view.background = drawable
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
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics)
    }
}
