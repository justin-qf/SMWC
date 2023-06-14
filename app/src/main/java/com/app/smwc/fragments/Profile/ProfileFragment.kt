package com.app.smwc.fragments.Home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.Activity.EditProfile.EditActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.R
import com.app.smwc.databinding.FragmentProfileBinding
import com.app.smwc.fragments.BaseFragment
import com.app.ssn.Common.Pref
import com.app.ssn.Utils.Loader.showToast
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*

class ProfileFragment : BaseFragment<FragmentProfileBinding>(), View.OnClickListener {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SWCApp.getInstance().observer.value = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
        initViews()
    }

    private fun initViews() {
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.toolbar.editLayout.setOnClickListener {
            val i = Intent(act, EditActivity::class.java)
            act.startActivity(i)
            HELPER.slideEnter(act)
            //setPublic(EditFragment(), "Edit")
        }
        binding.ivEditProfile.setOnClickListener {
//            logoutDialog(act, clickListener = {
//                Pref(act).Logout()
//                val i = Intent(act, LoginActivity::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                act.startActivity(i)
//                act.finish()
//                HELPER.slideEnter(act)
//            })
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
//        binding.toolbar.ivBack.setOnClickListener {
//            app!!.observer.value = Constant.OBSERVER_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE
//        }
        binding.toolbar.title.text = getString(R.string.title_profile)
        binding.toolbar.editLayout.visibility = View.VISIBLE
        binding.toolbar.submitLayout.visibility = View.GONE
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            try {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val fileUri = data!!.data!!
                        Glide.with(this)
                            .load(fileUri)
                            .into(binding.ivEditProfile)
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
                        showToast(act, ImagePicker.getError(data))
                    }
                    else -> {
                        showToast(act, "Task Cancelled")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    override fun onClick(view: View?) {
        when (requireView().id) {
            binding.toolbar.ivBack.id -> {
            }
            R.id.iv_edit_profile -> {
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun update(observable: Observable?, data: Any?) {
        super.update(observable, data)
        if (app!!.observer.value == Constant.OBSERVER_EDIT_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE) {
            onBackPressed()
        }
    }

    var screenAttech: Boolean = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        screenAttech = true
    }

    override fun onDetach() {
        super.onDetach()
        screenAttech = false
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        screenAttech = true
        checkThatOtherFragmentVisible()
        refreshData()
    }

    private fun refreshData() {
        if (binding.profileFragmentContainer.visibility == View.GONE) {
            onBackPressed()
        }
        SWCApp.getInstance().observer.value = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
        initViews()
    }

    private fun checkThatOtherFragmentVisible() {
        if (screenAttech)
            onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        val fragmentList = childFragmentManager.fragments
        return if (fragmentList.size != 0) {
            val fragmentsInStack = childFragmentManager.backStackEntryCount
            if (fragmentsInStack > 1) {
                childFragmentManager.popBackStack()
                true
            } else if (fragmentsInStack == 1) {
                childFragmentManager.popBackStack()
                binding.profileFragmentContainer.visibility = View.GONE
                binding.nestedLayout.visibility = View.VISIBLE
                pref = Pref(act)
                initViews()
                SWCApp.getInstance().observer.value = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    private fun setPublic(fragment: Fragment, tag: String) {
        val currentFragment = childFragmentManager.findFragmentById(R.id.profile_fragment_container)
        if (currentFragment != null && currentFragment.javaClass == fragment.javaClass) {
            if (tag.equals("home", ignoreCase = true)) {
            }
            return
        }
        if (childFragmentManager.findFragmentByTag(tag) != null) {

            childFragmentManager.popBackStack(tag, 0)
        }
        childFragmentManager
            .beginTransaction()
            .addToBackStack(tag)
            .replace(R.id.profile_fragment_container, fragment, tag)
            .commit()
        binding.profileFragmentContainer.visibility = View.VISIBLE
        binding.nestedLayout.visibility = View.GONE
    }
}