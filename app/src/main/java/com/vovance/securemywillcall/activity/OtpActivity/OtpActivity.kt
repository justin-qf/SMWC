package com.vovance.securemywillcall.activity.OtpActivity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.BaseActivity
import com.vovance.securemywillcall.activity.CompanyInfo.CompanyInfoActivity
import com.vovance.securemywillcall.activity.LoginActivity.LoginData
import com.vovance.securemywillcall.activity.MainActivity
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.databinding.ActivityOtpBinding
import com.vovance.securemywillcall.utils.GenericTextWatcher
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.NetworkResult
import com.vovance.ssn.ui.login.LoginViewModel
import com.vovance.ssn.ui.login.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OtpActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityOtpBinding? = null
    private var timeLeftFormatted: String? = null
    private var countDownTimer: CountDownTimer? = null
    private var mTimeLeftInMills: Long = 10000
    private var name = ""
    private var lastName = ""
    private var emailMobile = ""
    private var otp = ""
    private val otpViewModel: SignUpViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private var isFromLogin: Boolean = false
    private var deviceToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp)
        HELPER.CHANGE_ACTIONBAR_COLOUR(act)
        hideKeyboard(findViewById(R.id.mainLayout))
        //act?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        getFirebaseToken()
        initView()
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                deviceToken = task.result.toString()
                HELPER.print("FirebaseToken", deviceToken)
            } else {
                HELPER.print(
                    "Fetching FCM registration token failed",
                    task.exception.toString()
                )
                return@OnCompleteListener
            }
        })
    }

    private fun initView() {

        // getText From Intent
        name = intent.getStringExtra("name").toString()
        lastName = intent.getStringExtra("lastName").toString()
        emailMobile = intent.getStringExtra("email_mobile").toString()
        otp = intent.getStringExtra("otp").toString()
        isFromLogin = intent.getBooleanExtra("intentFrom", false)

        if (emailMobile.trim().matches("[0-9]+".toRegex())) {
            binding!!.titleTxt.text = getString(R.string.sent_otp)
            binding!!.numberTxt.text = if (emailMobile.isNotEmpty()) "+91 $emailMobile" else ""
        } else {
            binding!!.titleTxt.text = getString(R.string.sent_otp_email)
            binding!!.numberTxt.text = emailMobile.ifEmpty { "" }
        }

        //set OnCLickListener with visibility and colour
        binding!!.toolbar.ivBack.visibility = View.VISIBLE
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.verifyCodeBtn.setOnClickListener(this)
        binding!!.resendCodeTxt.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.code_verify_title)
        HELPER.setTextColour(binding!!.toolbar.title, act)
        HELPER.setImageColour(binding!!.toolbar.ivBack, act)
        otpView()
        verifyOtpResponse()
        loginResponse()
        Handler(Looper.getMainLooper()).postDelayed({
            getOtpFromApi()
        }, 1000)
    }

    private fun getOtpFromApi() {
        PubFun.apiResponseErrorDialog(
            act,
            getString(R.string.otp),
            if (otp.isNotEmpty()) "Your OTP: $otp" else getString(R.string.serverErrorMessage),
            false,
            listener = {
                binding!!.otpOne.requestFocus()
                binding!!.otpOne.isFocusableInTouchMode = true
                binding!!.otpOne.requestFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding!!.otpOne, InputMethodManager.SHOW_IMPLICIT)
            })
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
        if (binding!!.otpOne.text.toString().isEmpty() && binding!!.otpTwo.text.toString()
                .isEmpty() && binding!!.otpThree.text.toString()
                .isEmpty() && binding!!.otpFour.text.toString().isEmpty()
        ) {
            binding!!.otpOne.clearFocus()
            binding!!.otpTwo.clearFocus()
            binding!!.otpThree.clearFocus()
            binding!!.otpFour.clearFocus()
        }
    }

    private fun getOTP(): String {
        return binding!!.otpOne.text.toString().trim() + binding!!.otpTwo.text.toString()
            .trim() + binding!!.otpThree.text.toString().trim() + binding!!.otpFour.text.toString()
            .trim()
    }

    private fun updateCountDownText() {
        val minutes: Int = (mTimeLeftInMills / 1000).toInt() / 60
        val second: Int = (mTimeLeftInMills / 325).toInt() % 60
        timeLeftFormatted = String.format(Locale.getDefault(), "%02d", second)

        if (second == 0) {
            binding!!.resendCodeTxt.visibility = View.VISIBLE
            binding!!.resendCounterTxt.visibility = View.GONE
        } else {
            binding!!.resendCodeTxt.visibility = View.GONE
            binding!!.resendCounterTxt.visibility = View.VISIBLE
            binding!!.resendCounterTxt.text =
                "Didn't receive the OTP?\nRequest for a new one in $timeLeftFormatted seconds"
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
                onBackPressed()
            }
            binding!!.verifyCodeBtn.id -> {
                if (getOTP().length == 4) {
                    if (isFromLogin) {
                        loginApiCall()
                    } else {
                        verifyOtpApiCall()
                    }
                } else {
                    PubFun.commonDialog(
                        act,
                        getString(R.string.otp),
                        getString(R.string.error_otp),
                        true,
                        clickListener = {
                        })
                }
            }
            binding!!.resendCodeTxt.id -> {
                counter()
                updateCountDownText()
            }
        }
    }

    private fun verifyOtpResponse() {
        try {
            otpViewModel.verifyOtpLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data != null && it.data.status == 1 && it.data.data != null) {
                            HELPER.print("VERIFY_OTP_RESPONSE", gson.toJson(it.data.data!!))
                            prefManager.saveUser(it.data.data!!)
                            PubFun.commonDialog(
                                act,
                                getString(R.string.verifyOtp),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    val i = Intent(act, CompanyInfoActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(i)
                                    finish()
                                    finishAffinity()
                                    HELPER.slideEnter(act)
                                })
                        } else if (it.data!!.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.verifyOtp),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    PubFun.openLoginScreen(act, prefManager)
                                })
                        } else {
                            if (act != null && !act.isFinishing) {
                                PubFun.commonDialog(
                                    act,
                                    getString(R.string.verifyOtp),
                                    it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                    false,
                                    clickListener = {
                                    })
                            }
                            return@observe
                        }
                    }
                    is NetworkResult.Error -> {
                        Loader.hideProgress()
                        PubFun.commonDialog(
                            act,
                            getString(R.string.verifyOtp),
                            getString(
                                R.string.errorMessage
                            ),
                            false,
                            clickListener = {
                            })
                        HELPER.print("Network", "Error")
                    }
                    is NetworkResult.Loading -> {
                        Loader.showProgress(act)
                        HELPER.print("Network", "loading")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun verifyOtpApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            val paymentParam = OtpData(
                lastName = lastName,
                deviceToken = deviceToken,
                deviceType = Constant.DEVICE_TYPE,
                firstName = name,
                otp = getOTP(),
                emailMobile = emailMobile.trim(),
            )
            otpViewModel.verifyOtp(paymentParam)
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun loginResponse() {
        try {
            loginViewModel.loginResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data != null && it.data.status == 1 && it.data.data != null) {
                            HELPER.print("VerifyOtpResponse::", gson.toJson(it.data))
                            prefManager.saveUser(it.data.data!!)
                            PubFun.commonDialog(
                                act,
                                getString(R.string.verifyOtp),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    val i = Intent(act, MainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(i)
                                    HELPER.slideEnter(act)
                                })

                        } else if (it.data!!.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.verifyOtp),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    PubFun.openLoginScreen(act, prefManager)
                                })
                        } else {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.verifyOtp),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                })
                        }
                    }
                    is NetworkResult.Error -> {
                        Loader.hideProgress()
                        PubFun.commonDialog(
                            act,
                            getString(R.string.verifyOtp),
                            getString(
                                R.string.errorMessage
                            ),
                            false,
                            clickListener = {
                            })
                        HELPER.print("Network", "Error")
                    }
                    is NetworkResult.Loading -> {
                        Loader.showProgress(act)
                        HELPER.print("Network", "loading")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            val loginParam = LoginData(
                emailMobile = emailMobile.trim(),
                otp = getOTP(),
                deviceType = Constant.DEVICE_TYPE,
                deviceToken = deviceToken
            )
            loginViewModel.login(loginParam)
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }
}


