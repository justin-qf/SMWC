package com.vovance.securemywillcall.activity.SignUpActivity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.BaseActivity
import com.vovance.securemywillcall.activity.OtpActivity.OtpActivity
import com.vovance.securemywillcall.activity.OtpActivity.OtpData
import com.vovance.securemywillcall.common.CodeReUse
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.databinding.ActivitySignUpBinding
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.NetworkResult
import com.vovance.ssn.ui.login.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivitySignUpBinding? = null
    private val otpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_sign_up)
        act?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        hideKeyboard(binding!!.rootLayout)
        Utils.makeStatusBarTransparent(this)
        initView()
    }

    private fun initView() {
        binding!!.toolbar.ivBack.visibility = View.VISIBLE
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.getOtpBtn.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.sign_up_title)

        //Remove Errors
        CodeReUse.RemoveError(binding!!.firstNameEdt, binding!!.firstNameLayout)
        CodeReUse.RemoveError(binding!!.lastNameEdt, binding!!.lastNameLayout)
        CodeReUse.RemoveError(binding!!.emailMobileEdt, binding!!.emailMobileLayout)
        signUpResponse()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {
                onBackPressed()
            }
            binding!!.getOtpBtn.id -> {
                validation()
            }
        }
    }

    private fun validation() {
        var isError = false
        var isFocus = false
        val firstName = PubFun.removeSpaceFromText(binding!!.firstNameEdt.text.toString())
        val lastName = PubFun.removeSpaceFromText(binding!!.lastNameEdt.text.toString())
        val email = PubFun.removeSpaceFromText(binding!!.emailMobileEdt.text.toString())

        if (firstName.trim().isEmpty()) {
            isError = true
            binding!!.firstNameLayout.error = getString(R.string.name_error_msg)
            if (!isFocus) {
                binding!!.firstNameEdt.requestFocus()
                isFocus = true
            }
        }
        if (lastName.trim().isEmpty()) {
            isError = true
            binding!!.lastNameLayout.error = getString(R.string.last_name_error_msg)
            if (!isFocus) {
                binding!!.lastNameEdt.requestFocus()
                isFocus = true
            }
        }
        if (email.trim().isEmpty()) {
            isError = true
            binding!!.emailMobileLayout.error = getString(R.string.email_mobile_error_msg)
            if (!isFocus) {
                binding!!.emailMobileEdt.requestFocus()
                isFocus = true
            }
        } else {
            if (email.trim().matches("[0-9]+".toRegex())) {
                if (binding!!.emailMobileEdt.text!!.toString().length != 10) {
                    isError = true
                    binding!!.emailMobileLayout.error = getString(R.string.valid_mobile_error_msg)
                    if (!isFocus) {
                        binding!!.emailMobileEdt.requestFocus()
                        isFocus = true
                    }
                }
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding!!.emailMobileEdt.text!!).matches()) {
                    isError = true
                    binding!!.emailMobileLayout.error = getString(R.string.valid_email_error_msg)
                    if (!isFocus) {
                        binding!!.emailMobileEdt.requestFocus()
                        isFocus = true
                    }
                }
            }
        }
        if (!isError) {
            getOtpApiCall()
        }
    }

    private fun signUpResponse() {
        try {
            otpViewModel.setOtpLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data!!.status == 1 && it.data.otp != null) {
                            HELPER.print("SignUpResponse::", gson.toJson(it.data))
                            val i = Intent(act, OtpActivity::class.java)
                            i.putExtra("name", binding!!.firstNameEdt.text.toString())
                            i.putExtra("lastName", binding!!.lastNameEdt.text.toString())
                            i.putExtra("email_mobile", binding!!.emailMobileEdt.text.toString())
                            i.putExtra("intentFrom", false)
                            i.putExtra(
                                "otp",
                                it.data.otp!!.toString().trim()
                            )
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(i)
                            HELPER.slideEnter(act)
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(act,
                                getString(R.string.sign_up_title),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    PubFun.openLoginScreen(act, prefManager)
                                })
                        } else {
                            if (act != null && !act.isFinishing) {
                                PubFun.commonDialog(act,
                                    getString(R.string.sign_up_title),
                                    it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                    false,
                                    clickListener = {
                                    })
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        Loader.hideProgress()
                        PubFun.commonDialog(act,
                            getString(R.string.sign_up_title),
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
                emailMobile = binding!!.emailMobileEdt.text.toString().trim(),
                isLogin = Constant.IS_SIGNUP
            )
            otpViewModel.getOtp(paymentParam)
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }
}