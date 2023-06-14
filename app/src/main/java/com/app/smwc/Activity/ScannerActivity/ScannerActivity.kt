package com.app.smwc.Activity.ScannerActivity

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.omcsalesapp.Common.PubFun.Companion.permissionDialog
import com.app.omcsalesapp.Common.PubFun.Companion.qrRedirectDialog
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityScannerBinding
import com.app.ssn.Utils.Loader.showToast
import com.budiyev.android.codescanner.*

class ScannerActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityScannerBinding? = null
    private lateinit var codeScanner: CodeScanner
    private var isGranted: Boolean = false
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
                                requestPermission()
                            }
                        })
                    }
                }
            }
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
                    qrRedirectDialog(act, it.text, true, openListener = {
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
                    }, copyListener = {
                        binding!!.title.text = ""
                        codeScanner.startPreview()
                    }, closeListener = {
                        binding!!.title.text = ""
                        codeScanner.startPreview()
                    })
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
        if (isGranted)
            codeScanner
                .startPreview()
    }

    override fun onPause() {
        super.onPause()
        if (isGranted)
            codeScanner.releaseResources()
    }
}