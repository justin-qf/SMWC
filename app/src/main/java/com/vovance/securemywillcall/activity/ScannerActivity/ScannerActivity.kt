package com.vovance.securemywillcall.activity.ScannerActivity

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.*
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.omcsalesapp.Common.PubFun.Companion.permissionDialog
import com.vovance.omcsalesapp.Common.PubFun.Companion.qrRedirectDialog
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.BaseActivity
import com.vovance.securemywillcall.activity.LoginActivity.LoginActivity
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.databinding.ActivityScannerBinding
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.Loader.showToast
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScannerActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityScannerBinding? = null
    private lateinit var codeScanner: CodeScanner
    private var isGranted: Boolean = false
    private var isGrantedCameraPermission: Boolean = false
    private val scannerViewModel: ScannerViewModel by viewModels()
    private var scanResult: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_scanner)
        checkPermissionAndOpenCamera()
    }

    private fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            isGranted = false
            requestPermission()
        } else {
            isGranted = true
            initView()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isGranted = true
                initView()
            } else {
                isGranted = false
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionDialog(act, "You need to allow access permissions", listener = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                isGrantedCameraPermission = true
                                //requestPermission()
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
                        })
                    }
                }
            }
        }
    }

    private fun apiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            val scannerParam = ScannerResponseData(
                qrId = scanResult,
            )
            scannerViewModel.createToken(
                scannerParam,
                if (prefManager.getUser()!!.token!!.isNotEmpty()) "Bearer " + prefManager.getUser()!!.token!! else ""
            )
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun setScannerResponse() {
        try {
            scannerViewModel.scannerResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            HELPER.print("Response::", gson!!.toJson(it.data))
                            PubFun.commonDialog(
                                act,
                                getString(R.string.scan_title),
                                it.data.message!!.ifEmpty { "Server Error" },
                                false,
                                clickListener = {
                                    onBackPressed()
                                    app!!.observer.value = Constant.OBSERVER_REFRESH_DASHBOARD_DATA
//                                    qrRedirectDialog(act, scanResult, true, openListener = {
//                                        binding!!.title.text = ""
//                                        //copy Text
//                                        (act.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
//                                            ClipData.newPlainText(
//                                                null,
//                                                scanResult
//                                            )
//                                        )
//                                       // showToast(act, act.getString(R.string.copied_to_clipboard))
//                                        codeScanner.startPreview()
//                                    }, copyListener = {
//                                        binding!!.title.text = ""
//                                        codeScanner.startPreview()
//                                    }, closeListener = {
//                                        binding!!.title.text = ""
//                                        codeScanner.startPreview()
//                                    })

                                })

                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.scan_title),
                                it.data.message!!.ifEmpty { "Server Error" },
                                false,
                                clickListener = {
                                    prefManager!!.Logout()
                                    val i = Intent(act, LoginActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    act.startActivity(i)
                                    act.finish()
                                    HELPER.slideEnter(act)
                                })
                        } else {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.scan_title),
                                it.data.message!!.ifEmpty { "Server Error" },
                                false,
                                clickListener = {
                                })
                        }
                    }
                    is NetworkResult.Error -> {
                        Loader.hideProgress()
                        PubFun.commonDialog(
                            act,
                            getString(R.string.scan_title),
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


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            Constant.PERMISSION_REQUEST_CODE
        )
    }

    private fun initView() {
        binding!!.toolbar.title.text = getString(R.string.scan_title)
        HELPER.setTextColour(binding!!.toolbar.title, act)
        HELPER.setImageColour(binding!!.toolbar.ivBack, act)
        HELPER.setLayoutBgColour(binding!!.toolbar.mainToolLayout, act)
        binding!!.toolbar.ivBack.visibility = View.VISIBLE
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.title.setOnClickListener(this)

        codeScanner = CodeScanner(this, binding!!.scannerView)
        if (isGrantedCameraPermission) {
            codeScanner.startPreview()
        }
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                binding!!.title.text = it.text
                scanResult = it.text
                if (Patterns.WEB_URL.matcher(it.text).matches()) {
                    qrRedirectDialog(act, it.text, false, openListener = {
                        // Open URL
                        val browserIntent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(binding!!.title.text.toString())
                            )
                        startActivity(browserIntent)
                        binding!!.title.text = ""
                        codeScanner.startPreview()
                    }, copyListener = {
                        binding!!.title.text = ""
                        //copy Text
                        (act.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                            ClipData.newPlainText(
                                null,
                                it.text
                            )
                        )
                        showToast(act, act.getString(R.string.copied_to_clipboard))
                        codeScanner.startPreview()
                    },
                        closeListener = {
                            binding!!.title.text = ""
                            codeScanner.startPreview()
                        })
                } else {
                    apiCall()
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                showToast(act, "Camera initialization error: ${it.message}")
            }
        }
        binding!!.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        setScannerResponse()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {
                onBackPressed()
            }
            binding!!.title.id -> {
                if (Patterns.WEB_URL.matcher(binding!!.title.text).matches()) {
                    // Open URL
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(binding!!.title.text.toString()))
                    startActivity(browserIntent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGrantedCameraPermission) {
            if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                act.onBackPressed()
                return
            } else {
                initView()
            }

        } else {
            if (isGranted) {
                codeScanner
                    .startPreview()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isGrantedCameraPermission && isGranted) {
            codeScanner.releaseResources()
        }
    }
}