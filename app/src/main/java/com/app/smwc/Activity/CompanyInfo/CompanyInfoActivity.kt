package com.app.smwc.Activity.CompanyInfo

import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.R
import com.app.smwc.databinding.ActivityCompanyInfoBinding


class CompanyInfoActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityCompanyInfoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_company_info)
        Utils.makeStatusBarTransparent(this)
        hideKeyboard(binding!!.mainLayout)
        initView()
    }

    private fun initView() {

        setScrolView()
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.getOtpBtn.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.company_info_title)
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //Remove Errors
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
                onBackPressed()
            }
            binding!!.getOtpBtn.id -> {
                validation()
            }
        }
    }


    private fun setScrolView(){
        binding!!.scrollView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding!!.scrollView.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding!!.scrollView.rootView.height
            val keypadHeight: Int = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is showing
                binding!!.scrollView.post(Runnable {
                    binding!!.scrollView.smoothScrollTo(
                        0,
                        binding!!.scrollView.bottom
                    )
                })
            } else {
                // Keyboard is hidden
                binding!!.scrollView.post(Runnable { binding!!.scrollView.smoothScrollTo(0, 0) })
            }
        }

    }
    private fun validation() {
        var isError = false
        var isFocus = false
        val companyName = binding!!.companyNameEdt.text!!.toString().replace(" ", "")
        val email = binding!!.companyEmailEdt.text!!.toString().replace(" ", "")
        val companyMobile = binding!!.companyMobileEdt.text!!.toString().replace(" ", "")
        val companyAddress = binding!!.companyAddressEdt.text!!.toString().replace(" ", "")
        val companyCity = binding!!.companyCityEdt.text!!.toString().replace(" ", "")
        val companyZipcode = binding!!.zipCodeLayoutEdt.text!!.toString().replace(" ", "")

        if (companyName.trim().isEmpty()) {
            isError = true
            binding!!.companyNameLayout.error = getString(R.string.hint_company_name)
            if (!isFocus) {
                binding!!.companyNameEdt.requestFocus()
                isFocus = true
            }
        }
        if (email.trim().isEmpty()) {
            isError = true
            binding!!.companyEmailLayout.error = getString(R.string.hint_company_email)
            if (!isFocus) {
                binding!!.companyEmailEdt.requestFocus()
                isFocus = true
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim())
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