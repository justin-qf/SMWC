package com.vovance.securemywillcall.activity.LoginActivity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.BaseActivity
import com.vovance.securemywillcall.activity.OtpActivity.OtpActivity
import com.vovance.securemywillcall.activity.OtpActivity.OtpData
import com.vovance.securemywillcall.activity.SignUpActivity.SignUpActivity
import com.vovance.securemywillcall.common.CodeReUse
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.databinding.ActivityLoginBinding
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.NetworkResult
import com.vovance.ssn.ui.login.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityLoginBinding? = null
    private val otpViewModel: SignUpViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login)
        Utils.makeStatusBarTransparent(this)
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        hideKeyboard(binding!!.mainLayout)
        loginResponse()
        initView()
    }

    private fun initView() {
        binding!!.getOtpBtn.setOnClickListener(this)
        binding!!.signUpBtn.setOnClickListener(this)
        //Remove Errors
        CodeReUse.RemoveError(binding!!.emailNoEdt, binding!!.emailNoLayout)
        setNotificationHandle()
        setNotificationPermission()
    }

    private fun setNotificationHandle() {
        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    //showDummyNotification()
                } else {
                    PubFun.permissionDialog(
                        act,
                        getString(R.string.notificationPermissionRequiredMsg),
                        listener = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts(
                                    "package",
                                    packageName,
                                    null
                                )
                                intent.data = uri
                                act.startActivityForResult(intent, 0)
                            }
                        },false)
                }
            }
    }

    private fun setNotificationPermission() {

        if (ContextCompat.checkSelfPermission(act,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //showDummyNotification()
            HELPER.print("POST_NOTIFICATIONS","DONE")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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
                                    PubFun.openLoginScreen(act, prefManager)
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


    companion object {
        const val CHANNEL_ID = "dummy_channel"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Important Notification Channel",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "This notification contains important announcement, etc."
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun showDummyNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Congratulations! 🎉🎉🎉")
            .setContentText("You have post a notification to Android 13!!!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    act,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}