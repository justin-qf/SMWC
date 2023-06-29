package com.vovance.securemywillcall.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vovance.frimline.views.Utils
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.LoginActivity.LoginActivity
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.databinding.ActivitySplashBinding
import com.vovance.ssn.Common.Pref

class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    private var prefManager: Pref? = null
    var act: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this
        prefManager = Pref(this)
        binding = DataBindingUtil.setContentView(act!!, R.layout.activity_splash)
        Utils.makeStatusBarTransparent(this)
        setAppCache(true)
        binding!!.logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_animate))
        initView()
    }

    private fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (prefManager!!.getUser() != null) {
                val i =
                    Intent(act, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
                finish()
            } else {
                val i =
                    Intent(act, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                HELPER.slideEnter(act)
                finish()
            }
        }, Constant.DELAY_TIME)
    }

    private fun setAppCache(liveMode: Boolean) {
        try {
            if (liveMode) {
                val versionCode =
                    act!!.packageManager.getPackageInfo(act!!.packageName, 0).versionCode
                if (versionCode > prefManager!!.getPreviousCode()) {
                    prefManager!!.Logout()
                    HELPER.deleteCache(act)
                }
            } else {
                prefManager!!.Logout()
                HELPER.deleteCache(act)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}