package com.app.smwc.Activity.CompanyInfo

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.omcsalesapp.Common.PubFun.Companion.removeSpaceFromText
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityCompanyInfoBinding
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CompanyInfoActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityCompanyInfoBinding? = null
    private val addCompanyViewModel: CompanyInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_company_info)
        Utils.makeStatusBarTransparent(this)
        hideKeyboard(binding!!.mainLayout)
        initView()
    }

    private fun initView() {

        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.toolbar.skip.visibility = View.VISIBLE
        binding!!.toolbar.skip.setOnClickListener(this)
        binding!!.getOtpBtn.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.company_info_title)
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //Remove Errors
        CodeReUse.RemoveError(binding!!.companyNameEdt, binding!!.companyNameLayout)
        CodeReUse.RemoveError(binding!!.companyEmailEdt, binding!!.companyEmailLayout)
        CodeReUse.RemoveError(binding!!.companyMobileEdt, binding!!.companyMobileLayout)
        CodeReUse.RemoveError(binding!!.companyAddressEdt, binding!!.companyAddressLayout)
        CodeReUse.RemoveError(binding!!.companyCityEdt, binding!!.companyCityLayout)
        CodeReUse.RemoveError(binding!!.zipCodeEdt, binding!!.zipCodeLayout)
        addCompanyResponse()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {
                onBackPressed()
            }
            binding!!.getOtpBtn.id -> {
                validation()
            }
            binding!!.toolbar.skip.id -> {
                val i = Intent(act, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
            }
        }
    }

    private fun addCompanyResponse() {
        try {
            addCompanyViewModel.companyResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data!!.status == 1 && it.data.message!!.isNotEmpty()) {
                            HELPER.print("AddCompanyResponse::", gson.toJson(it.data))
                            PubFun.commonDialog(
                                act,
                                getString(R.string.company_info_title),
                                it.data.message!!.ifEmpty {getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    val i = Intent(act, MainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(i)
                                    HELPER.slideEnter(act)
                                })

                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.company_info_title),
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
                            PubFun.commonDialog(
                                act,
                                getString(R.string.company_info_title),
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
                            getString(R.string.company_info_title),
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

    private fun apiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            val addCompanyParam = CompanyData(
                address = binding!!.companyAddressEdt.text.toString().trim(),
                city = binding!!.companyCityEdt.text.toString().trim(),
                name = binding!!.companyNameEdt.text.toString().trim(),
                zipcode = binding!!.zipCodeEdt.text.toString().trim(),
                mobile = binding!!.companyMobileEdt.text.toString().trim(),
                email = binding!!.companyEmailEdt.text.toString().trim(),
            )
            addCompanyViewModel.addCompany(
                addCompanyParam,
                if (prefManager.getUser()!!.token!!.isNotEmpty()) "Bearer " + prefManager.getUser()!!.token!! else ""
            )
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }


    private fun validation() {
        var isError = false
        var isFocus = false
        val companyName = removeSpaceFromText(binding!!.companyNameEdt.text.toString())
        val email = removeSpaceFromText(binding!!.companyEmailEdt.text.toString())
        val companyMobile = removeSpaceFromText(binding!!.companyMobileEdt.text.toString())
        val companyAddress = removeSpaceFromText(binding!!.companyAddressEdt.text.toString())
        val companyCity = removeSpaceFromText(binding!!.companyCityEdt.text.toString())
        val companyZipcode = removeSpaceFromText(binding!!.zipCodeEdt.text.toString())

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
                binding!!.zipCodeEdt.requestFocus()
                isFocus = true
            }
        }
        if (!isError) {
            apiCall()
        }
    }
}