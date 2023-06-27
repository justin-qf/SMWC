package com.app.smwc.Activity.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Activity.OtpActivity.OtpActivity
import com.app.smwc.Activity.OtpActivity.OtpData
import com.app.smwc.Activity.SignUpActivity.SignUpActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityLoginBinding
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import com.app.ssn.ui.login.SignUpViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityLoginBinding? = null
    private val otpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login)
        Utils.makeStatusBarTransparent(this)
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        hideKeyboard(binding!!.mainLayout)
        loginResponse()
        initView()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                HELPER.print("Fetching FCM registration token failed", task.exception.toString())
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            HELPER.print("FirebaseToken",token.toString())
        })
    }

    private fun initView() {
        binding!!.getOtpBtn.setOnClickListener(this)
        binding!!.signUpBtn.setOnClickListener(this)
        //Remove Errors
        CodeReUse.RemoveError(binding!!.emailNoEdt, binding!!.emailNoLayout)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.getOtpBtn.id -> {
                checkValidation()
            }

            binding!!.signUpBtn.id -> {
                val i = Intent(act, SignUpActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
            }
        }
    }

    private fun checkValidation() {
        var flag = false
        if (binding!!.emailNoEdt.text!!.trim().isEmpty()) {
            binding!!.emailNoLayout.error = getString(R.string.email_mobile_error_msg)
            binding!!.emailNoEdt.requestFocus()
        } else {
            if (binding!!.emailNoEdt.text!!.toString().trim().matches("[0-9]+".toRegex())) {
                if (binding!!.emailNoEdt.text!!.toString().length != 10) {
                    binding!!.emailNoLayout.error = getString(R.string.valid_mobile_error_msg)
                    binding!!.emailNoEdt.requestFocus()
                } else {
                    flag = true
                }
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding!!.emailNoEdt.text!!).matches()) {
                    binding!!.emailNoLayout.error = getString(R.string.valid_email_error_msg)
                    binding!!.emailNoEdt.requestFocus();
                } else {
                    flag = true
                }
            }
        }
        if (flag) {
            getOtpApiCall()
        }
    }

    private fun loginResponse() {
        try {
            otpViewModel.setOtpLiveData.observe(this) {
                Loader.hideProgress()
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data!!.status == 1 && it.data.otp != null) {
                            HELPER.print("OtpResponse::", gson.toJson(it.data))
                            val i = Intent(act, OtpActivity::class.java)
                            i.putExtra(
                                "email_mobile",
                                binding!!.emailNoEdt.text.toString().trim()
                            )
                            i.putExtra("intentFrom", true)
                            i.putExtra(
                                "otp",
                                it.data.otp!!.toString().trim()
                            )
                            startActivity(i)
                            HELPER.slideEnter(act)
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(act,
                                getString(R.string.login),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    prefManager.Logout()
                                    val i = Intent(act, LoginActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    act.startActivity(i)
                                    act.finish()
                                    HELPER.slideEnter(act)
                                })
                        } else {
                            if (act != null && !act.isFinishing) {
                                PubFun.commonDialog(act,
                                    getString(R.string.login),
                                    it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                    false,
                                    clickListener = {
                                    })
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        PubFun.commonDialog(act,
                            getString(R.string.login),
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

    private fun getOtpApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            val paymentParam = OtpData(
                emailMobile = binding!!.emailNoEdt.text!!.toString().trim(),
                isLogin = Constant.IS_LOGIN
            )
            otpViewModel.getOtp(paymentParam)
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }
}