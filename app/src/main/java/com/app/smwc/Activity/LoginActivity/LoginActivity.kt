package com.app.smwc.Activity.LoginActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Activity.OtpActivity.OtpActivity
import com.app.smwc.Activity.SignUpActivity.SignUpActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityCompanyInfoBinding
import com.app.smwc.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login)
        Utils.makeStatusBarTransparent(this)
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        hideKeyboard(binding!!.mainLayout)
        initView()
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
                val i = Intent(act, OtpActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
            }
            binding!!.signUpBtn.id -> {
                val i = Intent(act, SignUpActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
            }
        }
    }
}