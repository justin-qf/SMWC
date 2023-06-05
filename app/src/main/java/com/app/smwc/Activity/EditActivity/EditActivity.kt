package com.app.smwc.Activity.EditActivity

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.R
import com.app.smwc.databinding.ActivityEditBinding

class EditActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityEditBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_edit)
        Utils.makeStatusBarTransparent(this)
        initView()
    }

    private fun initView() {
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.title_edit)
        //Remove Errors
        CodeReUse.RemoveError(binding!!.firstNameEdt, binding!!.firstNameLayout)
        CodeReUse.RemoveError(binding!!.lastNameEdt, binding!!.lastNameLayout)
        CodeReUse.RemoveError(binding!!.emailMobileEdt, binding!!.emailMobileLayout)
        CodeReUse.RemoveError(binding!!.companyNameEdt, binding!!.companyNameLayout)
        CodeReUse.RemoveError(binding!!.companyEmailEdt, binding!!.companyEmailLayout)
        CodeReUse.RemoveError(binding!!.companyMobileEdt, binding!!.companyMobileLayout)
        CodeReUse.RemoveError(binding!!.companyAddressEdt, binding!!.companyAddressLayout)
        CodeReUse.RemoveError(binding!!.companyCityEdt, binding!!.companyCityLayout)
        CodeReUse.RemoveError(binding!!.zipCodeLayoutEdt, binding!!.zipCodeLayout)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {

            }
            binding!!.toolbar.submitLayout.id -> {
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
        val companyName = binding!!.companyNameEdt.text!!.toString().replace(" ", "")
        val companyEmail = binding!!.companyEmailEdt.text!!.toString().replace(" ", "")
        val companyMobile = binding!!.companyMobileEdt.text!!.toString().replace(" ", "")
        val companyAddress = binding!!.companyAddressEdt.text!!.toString().replace(" ", "")
        val companyCity = binding!!.companyCityEdt.text!!.toString().replace(" ", "")
        val companyZipcode = binding!!.zipCodeLayoutEdt.text!!.toString().replace(" ", "")


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
        if (companyName.trim().isEmpty()) {
            isError = true
            binding!!.companyNameLayout.error = getString(R.string.hint_company_name)
            if (!isFocus) {
                binding!!.companyNameEdt.requestFocus()
                isFocus = true
            }
        }
        if (companyEmail.trim().isEmpty()) {
            isError = true
            binding!!.companyEmailLayout.error = getString(R.string.hint_company_email)
            if (!isFocus) {
                binding!!.companyEmailEdt.requestFocus()
                isFocus = true
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(companyEmail.trim())
                .matches()
        ) {
            isError = true
            binding!!.companyEmailLayout.error = getString(R.string.valid_company_email)
            if (!isFocus) {
                binding!!.companyEmailEdt.requestFocus()
                isFocus = true
            }
        }
        if (companyMobile.trim().isEmpty()) {
            isError = true
            binding!!.companyMobileLayout.error = getString(R.string.hint_company_mobile_no)
            if (!isFocus) {
                binding!!.companyMobileEdt.requestFocus()
                isFocus = true
            }
        } else if (companyMobile.length < 10) {
            isError = true
            binding!!.companyMobileLayout.error = getString(R.string.valid_company_mobile_no)
            if (!isFocus) {
                binding!!.companyMobileEdt.requestFocus()
                isFocus = true
            }
        }
        if (companyAddress.trim().isEmpty()) {
            isError = true
            binding!!.companyAddressLayout.error = getString(R.string.hint_company_address)
            if (!isFocus) {
                binding!!.companyAddressEdt.requestFocus()
                isFocus = true
            }
        }
        if (companyCity.trim().isEmpty()) {
            isError = true
            binding!!.companyCityLayout.error = getString(R.string.hint_company_city)
            if (!isFocus) {
                binding!!.companyCityEdt.requestFocus()
                isFocus = true
            }
        }

        if (companyZipcode.trim().isEmpty()) {
            isError = true
            binding!!.zipCodeLayout.error = getString(R.string.hint_zip_code)
            if (!isFocus) {
                binding!!.zipCodeLayoutEdt.requestFocus()
                isFocus = true
            }
        }

        if (!isError) {
            //Loader.showProgress(act)
        }
    }

}