package com.app.smwc.Activity.OtpActivity

import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.Utils.GenericTextWatcher
import com.app.smwc.databinding.ActivityOtpBinding
import java.util.*


class OtpActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityOtpBinding? = null
    var timeLeftFormeted: String? = null
    private var countDownTimer: CountDownTimer? = null
    private var mTimeLeftInMills: kotlin.Long = 10000
    private var resend: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp)
        HELPER.CHANGE_ACTIONBAR_COLOUR(act)
        hideKeyboard(findViewById(R.id.mainLayout))
        initView()
    }

    private fun initView() {
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.verifyCodeBtn.setOnClickListener(this)
        binding!!.resendCodeTxt.setOnClickListener {
            counter()
            updateCountDownText()
        }
        binding!!.toolbar.title.text = getString(R.string.code_verify_title)
        HELPER.setTextColour(binding!!.toolbar.title, act);
        HELPER.setImageColour(binding!!.toolbar.ivBack, act);
        otpView()
    }

    private fun otpView() {
        textChanger()
        otpViewBackgroundChangeListener()
    }

    private fun textChanger() {
        val edit =
            arrayOf(binding!!.otpOne, binding!!.otpTwo, binding!!.otpThree, binding!!.otpFour)
        binding!!.otpOne.addTextChangedListener(GenericTextWatcher(act, binding!!.otpOne, edit))
        binding!!.otpTwo.addTextChangedListener(GenericTextWatcher(act, binding!!.otpTwo, edit))
        binding!!.otpThree.addTextChangedListener(GenericTextWatcher(act, binding!!.otpThree, edit))
        binding!!.otpFour.addTextChangedListener(GenericTextWatcher(act, binding!!.otpFour, edit))
    }

    private fun otpViewBackgroundChangeListener() {
//        binding!!.otpOne.onFocusChangeListener =
//            View.OnFocusChangeListener { view, b ->
//                if (b) {
//                    binding!!.otpOne.setBackgroundResource(R.drawable.background_otp_focused)
//                } else if (!binding!!.otpOne.text.toString().isEmpty()) {
//                    binding!!.otpOne.setBackgroundResource(R.drawable.background_otp_active)
//                } else {
//                    binding!!.otpOne.setBackgroundResource(R.drawable.background_otp_inactive)
//                }
//            }
//        binding!!.otpTwo.onFocusChangeListener =
//            View.OnFocusChangeListener { view, b ->
//                if (b) {
//                    binding!!.otpTwo.setBackgroundResource(R.drawable.background_otp_focused)
//                } else if (!binding!!.otpTwo.text.toString().isEmpty()) {
//                    binding!!.otpTwo.setBackgroundResource(R.drawable.background_otp_active)
//                } else {
//                    binding!!.otpTwo.setBackgroundResource(R.drawable.background_otp_inactive)
//                }
//            }
//        binding!!.otpThree.onFocusChangeListener =
//            View.OnFocusChangeListener { view, b ->
//                if (b) {
//                    binding!!.otpThree.setBackgroundResource(R.drawable.background_otp_focused)
//                } else if (!binding!!.otpThree.text.toString().isEmpty()) {
//                    binding!!.otpThree.setBackgroundResource(R.drawable.background_otp_active)
//                } else {
//                    binding!!.otpThree.setBackgroundResource(R.drawable.background_otp_inactive)
//                }
//            }
//        binding!!.otpFour.onFocusChangeListener =
//            View.OnFocusChangeListener { view, b ->
//                if (b) {
//                    binding!!.otpFour.setBackgroundResource(R.drawable.background_otp_focused)
//                } else if (!binding!!.otpFour.text.toString().isEmpty()) {
//                    binding!!.otpFour.setBackgroundResource(R.drawable.background_otp_active)
//                } else {
//                    binding!!.otpFour.setBackgroundResource(R.drawable.background_otp_inactive)
//                }
//            }
        if (binding!!.otpOne.text.toString().isEmpty() && binding!!.otpTwo.text.toString()
                .isEmpty() && binding!!.otpThree.text.toString()
                .isEmpty() && binding!!.otpFour.text.toString().isEmpty()
        ) {
            binding!!.otpOne.clearFocus()
            binding!!.otpTwo.clearFocus()
            binding!!.otpThree.clearFocus()
            binding!!.otpFour.clearFocus()
        }
//        binding!!.otpOne.setBackgroundResource(R.drawable.background_otp_inactive)
//        binding!!.otpTwo.setBackgroundResource(R.drawable.background_otp_inactive)
//        binding!!.otpThree.setBackgroundResource(R.drawable.background_otp_inactive)
//        binding!!.otpFour.setBackgroundResource(R.drawable.background_otp_inactive)
    }

    private fun getOTP(): String {
        return binding!!.otpOne.text.toString().trim() + binding!!.otpTwo.text.toString()
            .trim() + binding!!.otpThree.text.toString().trim() + binding!!.otpFour.text.toString()
            .trim()
    }

    private fun updateCountDownText() {
        val minutes: Int = (mTimeLeftInMills / 1000).toInt() / 60
        val second: Int = (mTimeLeftInMills / 325).toInt() % 60
        timeLeftFormeted = String.format(Locale.getDefault(), "%02d", second)

        if (second == 0) {
            binding!!.resendCodeTxt.visibility = View.VISIBLE
            binding!!.resendCounterTxt.visibility = View.GONE
        } else {
            binding!!.resendCodeTxt.visibility = View.GONE
            binding!!.resendCounterTxt.visibility = View.VISIBLE
            binding!!.resendCounterTxt.text =
                "Didn't receive the OTP?\nRequest for a new one in $timeLeftFormeted seconds"
        }
    }


    private fun counter() {
        if (countDownTimer != null) {
            countDownTimer!!.start()
            //resendOtpEnableDisable(false)
        }
        countDownTimer = object : CountDownTimer(mTimeLeftInMills, 1100) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMills = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                // resendOtpEnableDisable(true)
            }
        }.start()
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {

            }
            binding!!.verifyCodeBtn.id->{
                val i = Intent(act,MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
            }

        }
    }
}