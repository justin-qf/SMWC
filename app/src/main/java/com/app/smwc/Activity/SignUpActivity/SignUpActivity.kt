package com.app.smwc.Activity.SignUpActivity

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.R
import com.app.smwc.databinding.ActivitySignUpBinding
import com.app.ssn.Utils.Loader

class SignUpActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_sign_up)
        act?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        hideKeyboard(binding!!.rootLayout)
        Utils.makeStatusBarTransparent(this)
        initView()
    }

    private fun initView() {
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.getOtpBtn.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.sign_up_title)

        //Remove Errors
        CodeReUse.RemoveError(binding!!.firstNameEdt, binding!!.firstNameLayout)
        CodeReUse.RemoveError(binding!!.lastNameEdt, binding!!.lastNameLayout)
        CodeReUse.RemoveError(binding!!.emailMobileEdt, binding!!.emailMobileLayout)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {
            }
            binding!!.getOtpBtn.id -> {
                validation()
            }
        }
    }

    private fun validation() {
        var isError = false
        var isFocus = false
        val firstName = binding!!.firstNameEdt.text!!.toString().replace(" ", "")
        val lastName = binding!!.lastNameEdt.text!!.toString().replace(" ", "")
        val email = binding!!.emailMobileEdt.text!!.toString().replace(" ", "")

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
            binding!!.emailMobileLayout.error = getString(R.string.email_error_msg)
            if (!isFocus) {
                binding!!.emailMobileEdt.requestFocus()
                isFocus = true
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim())
                .matches()
        ) {
            isError = true
            binding!!.emailMobileLayout.error = getString(R.string.valid_email_error_msg)
            if (!isFocus) {
                binding!!.emailMobileEdt.requestFocus()
                isFocus = true
            }
        }

        if (!isError) {
            //Loader.showProgress(act)
        }
    }

}