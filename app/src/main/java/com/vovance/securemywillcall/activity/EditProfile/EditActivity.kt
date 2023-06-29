package com.vovance.securemywillcall.activity.EditProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.securemywillcall.apiHandle.Apis
import com.vovance.securemywillcall.activity.BaseActivity
import com.vovance.securemywillcall.activity.LoginActivity.LoginActivity
import com.vovance.securemywillcall.common.CodeReUse
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.databinding.ActivityEditBinding
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.NetworkResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File

@AndroidEntryPoint
class EditActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityEditBinding? = null
    private val updateViewModel: UpdateProfileViewModel by viewModels()
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_edit)
        Utils.makeStatusBarTransparent(this)
        hideKeyboard(binding!!.mainLayout)
        initView()
    }

    private fun initView() {
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.editProfileLayout.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.title_edit)
        binding!!.toolbar.submitLayout.visibility = View.VISIBLE
        //Remove Errors
        CodeReUse.RemoveError(binding!!.firstNameEdt, binding!!.firstNameLayout)
        CodeReUse.RemoveError(binding!!.lastNameEdt, binding!!.lastNameLayout)
        CodeReUse.RemoveError(binding!!.emailMobileEdt, binding!!.emailMobileLayout)
        CodeReUse.RemoveError(binding!!.companyNameEdt, binding!!.companyNameLayout)
        CodeReUse.RemoveError(binding!!.companyEmailEdt, binding!!.companyEmailLayout)
        CodeReUse.RemoveError(binding!!.companyMobileEdt, binding!!.companyMobileLayout)
        CodeReUse.RemoveError(binding!!.companyAddressEdt, binding!!.companyAddressLayout)
        CodeReUse.RemoveError(binding!!.companyCityEdt, binding!!.companyCityLayout)
        CodeReUse.RemoveError(binding!!.zipCodeEdt, binding!!.zipCodeLayout)
        binding!!.toolbar.submitLayout.setOnClickListener(this)
        binding!!.toolbar.ivBack.setOnClickListener(this)
        setData()
        updateProfileResponse()
    }

    private fun setData() {
        //set Data
        val receivedList = intent.getStringArrayListExtra(Constant.PROFILE_DATA)
        if (receivedList != null) {
            binding!!.firstNameEdt.setText(receivedList[0])
            binding!!.lastNameEdt.setText(receivedList[1])
            binding!!.emailMobileEdt.setText(receivedList[2])
            binding!!.companyNameEdt.setText(receivedList[3])
            binding!!.companyEmailEdt.setText(receivedList[4])
            binding!!.companyMobileEdt.setText(receivedList[5])
            binding!!.companyAddressEdt.setText(receivedList[6])
            binding!!.companyCityEdt.setText(receivedList[7])
            binding!!.zipCodeEdt.setText(receivedList[8])
            if (receivedList[9].isNotEmpty()) {
                Glide.with(this)
                    .load(receivedList[9])
                    .placeholder(R.drawable.user_icon)
                    .error(R.drawable.user_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(binding!!.ivEdit)
            } else {
                Glide.with(this).load(R.drawable.user_icon)
                    .placeholder(R.drawable.user_icon).error(R.drawable.user_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(binding!!.ivEdit)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {
                onBackPressed()
            }
            binding!!.toolbar.submitLayout.id -> {
                validation()
            }
            binding!!.editProfileLayout.id -> {
                PubFun.getImageDialog(act, cameraListener = {
                    ImagePicker.with(this)
                        .cameraOnly()
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080,
                            1080
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }, galleryListener = {
                    ImagePicker.with(this)
                        .galleryOnly()
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                            1080,
                            1080
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }

                })
            }
        }
    }

    var uploadImageResponse: JSONObject? = null

    private fun uploadImageUsingVolley() {
        Loader.showProgress(act)
        val request =
            AndroidNetworking.upload(Apis.BASE + Apis.UPDATE_PROFILE).setTag("updateProfile")
                .setPriority(Priority.HIGH)
        if (fileUri != null) {
            request.addMultipartFile("image", createFileFromUri(fileUri!!))
        }
        request.addMultipartParameter("first_name", binding!!.firstNameEdt.text.toString().trim())
        request.addMultipartParameter("last_name", binding!!.lastNameEdt.text.toString().trim())
        request.addMultipartParameter(
            "email_mobile",
            binding!!.emailMobileEdt.text.toString().trim()
        )
        request.addMultipartParameter(
            "company_name",
            binding!!.companyNameEdt.text.toString().trim()
        )
        request.addMultipartParameter(
            "company_email",
            binding!!.companyEmailEdt.text.toString().trim()
        )
        request.addMultipartParameter(
            "company_mobile",
            binding!!.companyMobileEdt.text.toString().trim()
        )
        request.addMultipartParameter(
            "company_address",
            binding!!.companyAddressEdt.text.toString().trim()
        )
        request.addMultipartParameter(
            "company_city",
            binding!!.companyCityEdt.text.toString().trim()
        )
        request.addMultipartParameter(
            "company_zipcode",
            binding!!.zipCodeEdt.text.toString().trim()
        )

        request.addHeaders(
            "Authorization",
            if (prefManager.getUser()!!.token!!.isNotEmpty()) "Bearer " + prefManager.getUser()!!.token!! else ""
        )

        request.build().setUploadProgressListener { bytesUploaded, totalBytes ->
            // do anything with progress
        }.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject) {
                Loader.hideProgress()
                uploadImageResponse = response
                HELPER.print("IsUploadImage", gson.toJson(uploadImageResponse))
                if (uploadImageResponse?.get("status") == 1) {
                    PubFun.commonDialog(act,
                        getString(R.string.title_edit),
                        uploadImageResponse?.get("message").toString()
                            .ifEmpty { getString(R.string.serverErrorMessage) },
                        false,
                        clickListener = {
                            onBackPressed()
                            app!!.observer.value = Constant.OBSERVER_REFRESH_PROFILE_DATA
                        })

                } else if (uploadImageResponse?.get("status") == 2) {
                    PubFun.commonDialog(act,
                        getString(R.string.title_edit),
                        uploadImageResponse?.get("message").toString()
                            .ifEmpty { getString(R.string.serverErrorMessage) },
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
                    if (act != null && !act.isFinishing) {
                        PubFun.commonDialog(act,
                            getString(R.string.title_edit),
                            uploadImageResponse?.get("message").toString()
                                .ifEmpty { getString(R.string.serverErrorMessage) },
                            false,
                            clickListener = {
                                onBackPressed()
                            })
                    }
                }
                HELPER.print("response", response.toString())
            }

            override fun onError(error: ANError) {
                Loader.hideProgress()
                PubFun.commonDialog(act,
                    getString(R.string.title_edit),
                    getString(
                        R.string.errorMessage
                    ),
                    false,
                    clickListener = {
                    })
            }
        })

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            try {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        fileUri = data!!.data!!
                        Glide.with(this)
                            .load(fileUri)
                            .into(binding!!.ivEdit)
//                        Glide.with(this)
//                            .asBitmap()
//                            .load(result.uri)
//                            //.error(R.drawable.no_result)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(object : CustomTarget<Bitmap?>() {
//                                override fun onResourceReady(
//                                    resource: Bitmap,
//                                    @Nullable transition: Transition<in Bitmap?>?
//                                ) {
//                                    binding.ivEditProfile.setImageBitmap(resource)
//                                    binding.ivEditProfile.buildDrawingCache()
//                                }
//
//                                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
//                            })
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Loader.showToast(act, ImagePicker.getError(data))
                    }
                    else -> {
                        Loader.showToast(act, "Task Cancelled")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun createFileFromUri(uri: Uri): File? {
        return try {
            val stream = contentResolver.openInputStream(uri)
            val file = File.createTempFile(
                "${System.currentTimeMillis()}", ".jpg", cacheDir
            )
            FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun validation() {

        var isError = false
        var isFocus = false
        val firstName = PubFun.removeSpaceFromText(binding!!.firstNameEdt.text.toString())
        val lastName = PubFun.removeSpaceFromText(binding!!.lastNameEdt.text.toString())
        val email = PubFun.removeSpaceFromText(binding!!.emailMobileEdt.text.toString())
        val companyName = PubFun.removeSpaceFromText(binding!!.companyNameEdt.text.toString())
        val companyEmail = PubFun.removeSpaceFromText(binding!!.companyEmailEdt.text.toString())
        val companyMobile = PubFun.removeSpaceFromText(binding!!.companyMobileEdt.text.toString())
        val companyAddress = PubFun.removeSpaceFromText(binding!!.companyAddressEdt.text.toString())
        val companyCity = PubFun.removeSpaceFromText(binding!!.companyCityEdt.text.toString())
        val companyZipcode = PubFun.removeSpaceFromText(binding!!.zipCodeEdt.text.toString())

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
                binding!!.zipCodeEdt.requestFocus()
                isFocus = true
            }
        }

        if (!isError) {
            uploadImageUsingVolley()
        }
    }

    private fun updateProfileApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            val paymentParam = UpdateProfileData(
                emailMobile = binding!!.emailMobileEdt.text!!.toString().trim(),
                firstName = binding!!.firstNameEdt.text!!.toString().trim(),
                lastName = binding!!.lastNameEdt.text!!.toString().trim(),
                companyAddress = binding!!.companyAddressEdt.text!!.toString().trim(),
                companyCity = binding!!.companyCityEdt.text!!.toString().trim(),
                companyEmail = binding!!.companyEmailEdt.text!!.toString().trim(),
                companyMobile = binding!!.companyMobileEdt.text!!.toString().trim(),
                companyName = binding!!.companyNameEdt.text!!.toString().trim(),
                companyZipcode = binding!!.zipCodeEdt.text!!.toString().trim(),
            )
            val imageData =
                MultipartBody.Part.createFormData(
                    "fileName",
                    createFileFromUri(fileUri!!)!!.name
                )
            updateViewModel.updateProfile(
                paymentParam,
                imageData,
                if (prefManager.getUser()!!.token!!.isNotEmpty()) "Bearer " + prefManager.getUser()!!.token!! else ""
            )
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun updateProfileResponse() {
        try {
            updateViewModel.updateProfileResponseLiveData.observe(this) {
                Loader.hideProgress()
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data!!.status == 1) {
                            HELPER.print("GetOtpResponse::", gson.toJson(it.data))
                            PubFun.commonDialog(act,
                                getString(R.string.title_edit),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {

                                })
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(act,
                                getString(R.string.title_edit),
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
                            if (act != null && !act.isFinishing) {
                                PubFun.commonDialog(act,
                                    getString(R.string.title_edit),
                                    it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                    false,
                                    clickListener = {
                                    })
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        PubFun.commonDialog(act,
                            getString(R.string.title_edit),
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
}

