package com.app.smwc.Activity.EditProfile

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.APIHandle.Apis
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Common.CodeReUse
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityEditBinding
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import com.bumptech.glide.Glide
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
    private var firstName = ""
    private var lastName = ""
    private var emailMobile = ""
    private var companyName = ""
    private var companyEmail = ""
    private var companyMobile = ""
    private var companyAddress = ""
    private var companyCity = ""
    private var zipcode = ""
    private var imageUrl = ""
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
//        firstName = intent.getStringExtra("firstName").toString()
//        lastName = intent.getStringExtra("lastName").toString()
//        emailMobile = intent.getStringExtra("emailMobile").toString()
//        companyName = intent.getStringExtra("companyName").toString()
//        companyEmail = intent.getStringExtra("companyEmail").toString()
//        companyMobile = intent.getStringExtra("companyMobile").toString()
//        companyAddress = intent.getStringExtra("companyAddress").toString()
//        companyCity = intent.getStringExtra("companyCity").toString()
//        zipcode = intent.getStringExtra("zipcode").toString()

        binding!!.firstNameEdt.setText(intent.getStringExtra("firstName").toString())
        binding!!.lastNameEdt.setText(intent.getStringExtra("lastName").toString())
        binding!!.emailMobileEdt.setText(intent.getStringExtra("emailMobile").toString())
        binding!!.companyNameEdt.setText(intent.getStringExtra("companyName").toString())
        binding!!.companyEmailEdt.setText(intent.getStringExtra("companyEmail").toString())
        binding!!.companyMobileEdt.setText(intent.getStringExtra("companyMobile").toString())
        binding!!.companyAddressEdt.setText(intent.getStringExtra("companyAddress").toString())
        binding!!.companyCityEdt.setText(intent.getStringExtra("companyCity").toString())
        binding!!.zipCodeEdt.setText(intent.getStringExtra("zipcode").toString())
        imageUrl = intent.getStringExtra("image").toString()
        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding!!.ivEdit)
        } else {
            return
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {
                onBackPressed()
            }
            binding!!.toolbar.submitLayout.id -> {
                validation()
                //validation()
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

        HELPER.print("PassingData", request.toString())
        request.build().setUploadProgressListener { bytesUploaded, totalBytes ->
            // do anything with progress
        }.getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject) {
                Loader.hideProgress()
                uploadImageResponse = response
                HELPER.print("IsUploadImage", gson.toJson(uploadImageResponse))
                if (uploadImageResponse?.get("status") == 1) {
                    PubFun.commonDialog(act,
                        getString(R.string.login),
                        uploadImageResponse?.get("message").toString().ifEmpty { "Server Error" },
                        false,
                        clickListener = {
                            onBackPressed()
                            app!!.observer.value = Constant.OBSERVER_REFRESH_PROFILE_DATA
                        })

                } else if (uploadImageResponse?.get("status") == 2) {
                    PubFun.commonDialog(act,
                        getString(R.string.login),
                        uploadImageResponse?.get("message").toString().ifEmpty { "Server Error" },
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
                            getString(R.string.login),
                            uploadImageResponse?.get("message").toString()
                                .ifEmpty { "Server Error" },
                            false,
                            clickListener = {
                                onBackPressed()
                            })
                    }
                }
//                {"nameValuePairs":{"status":1,"message":"Profile updated successfully",
//                    "data":{"nameValuePairs":{"id":24,"first_name":"Maulik","last_name":"Patel","email_mobile":"maulik2001@gmail.com",
//                        "device_token":"android","device_type":"android","company_id":0,"is_verify":1,
//                        "image":"https://dev.securemywillcall.com/public/profile/1686904105.jpg","company_name":"",
//                        "company_email":"","company_mobile":"","company_city":"","company_zipcode":"","company_address":""}}}}

                HELPER.print("response", response.toString())
                HELPER.print("response", gson.toJson(response))
            }

            override fun onError(error: ANError) {
                Loader.hideProgress()
                PubFun.commonDialog(act,
                    getString(R.string.login),
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
                                getString(R.string.login),
                                it.data.message!!.ifEmpty { "Server Error" },
                                false,
                                clickListener = {

                                })
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(act,
                                getString(R.string.login),
                                it.data.message!!.ifEmpty { "Server Error" },
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
                                    getString(R.string.login),
                                    it.data.message!!.ifEmpty { "Server Error" },
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

}

private fun ContentResolver.getFileName(fileUri: Uri): String {

    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name;
}

