package com.vovance.securemywillcall.fragments.Profile.ProfileFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.activity.EditProfile.EditActivity
import com.vovance.securemywillcall.activity.LoginActivity.LoginActivity
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.common.SWCApp
import com.vovance.securemywillcall.databinding.FragmentProfileBinding
import com.vovance.securemywillcall.fragments.BaseFragment
import com.vovance.ssn.Common.Pref
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(), View.OnClickListener {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var profileData: ProfileData? = null

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SWCApp.getInstance().observer.value = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
        initViews()
        setApiCall()
        iniRefreshListener()
    }

    private fun iniRefreshListener() {
        binding.swipeContainer.setOnRefreshListener {
            setSwipeContainer()
        }
    }

    private fun setApiCall() {
        setProfileResponse()
        if (Utils.hasNetwork(act)) {
            //Loader.showProgress(act)
            profileViewModel.profile(
                if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
            )
        } else {
            //HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun setProfileResponse() {
        try {
            profileViewModel.profileResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        setShimmerWithSwipeContainer()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            HELPER.print("ProfileResponse::", gson!!.toJson(it.data))
                            setData(it.data.data)
                            profileData = it.data.data
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.title_profile),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    PubFun.openLoginScreen(act, pref)
                                })
                        } else {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.title_profile),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                })
                        }
                    }
                    is NetworkResult.Error -> {
                        Loader.hideProgress()
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        binding.nestedLayout.visibility = View.VISIBLE
                        PubFun.commonDialog(
                            act,
                            getString(R.string.title_profile),
                            getString(
                                R.string.errorMessage
                            ),
                            false,
                            clickListener = {
                            })
                        HELPER.print("Network", "Error")
                    }
                    is NetworkResult.Loading -> {
                        //Loader.showProgress(act)
                        HELPER.print("Network", "loading")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSwipeContainer() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.nestedLayout.visibility = View.GONE
        setApiCall()
    }

    private fun setShimmerWithSwipeContainer() {
        if (binding.swipeContainer.isRefreshing) {
            binding.swipeContainer.isRefreshing = false
        }
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.nestedLayout.visibility = View.VISIBLE
    }

    private fun setData(data: ProfileData?) {
        binding.emailMobileTxt.text = data!!.emailMobile
        binding.companyNametxt.text = data.companyName
        binding.companyEmailtxt.text = data.companyEmail
        binding.companyMobiletxt.text = data.companyMobile
        binding.companyCitytxt.text = data.companyCity
        binding.zipCodeTxt.text = data.companyZipcode
        binding.companyAddressTxt.text = data.companyAddress
        if (data.firstName!!.isNotEmpty()) {
            binding.name.text = data.firstName + " " + data.lastName
        }
        if (data.image != null && data.image.toString().isNotEmpty()) {
            Glide.with(this).load(data.image)
                .placeholder(R.drawable.user_icon).error(R.drawable.user_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivEditProfile)
        } else {
            Glide.with(this).load(R.drawable.user_icon)
                .placeholder(R.drawable.user_icon).error(R.drawable.user_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivEditProfile)
        }
    }

    private fun initViews() {
        binding.toolbar.ivLogout.setOnClickListener(this)
        binding.toolbar.editLayout.setOnClickListener(this)
        binding.ivEditProfile.setOnClickListener(this)
        binding.toolbar.title.text = getString(R.string.title_profile)
        binding.toolbar.editLayout.visibility = View.VISIBLE
        binding.toolbar.submitLayout.visibility = View.GONE
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.toolbar.ivLogout.id -> {
                PubFun.logoutDialog(act, clickListener = {
                    PubFun.openLoginScreen(act, pref)
                })
                //app!!.observer.value = Constant.OBSERVER_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE
            }
            binding.toolbar.editLayout.id -> {
                if (profileData != null) {
                    val stringList = ArrayList<String>()
                    stringList.add(profileData!!.firstName.toString().trim())
                    stringList.add(profileData!!.lastName.toString().trim())
                    stringList.add(profileData!!.emailMobile.toString().trim())
                    stringList.add(profileData!!.companyName.toString().trim())
                    stringList.add(profileData!!.companyEmail.toString().trim())
                    stringList.add(profileData!!.companyMobile.toString().trim())
                    stringList.add(profileData!!.companyAddress.toString().trim())
                    stringList.add(profileData!!.companyCity.toString().trim())
                    stringList.add(profileData!!.companyZipcode.toString().trim())
                    stringList.add(profileData!!.image.toString().trim())
                    val i = Intent(act, EditActivity::class.java)
                    i.putStringArrayListExtra(Constant.PROFILE_DATA, stringList)
                    act.startActivity(i)
                    HELPER.slideEnter(act)
                }
                //setPublic(EditFragment(), "Edit")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun update(observable: Observable?, data: Any?) {
        super.update(observable, data)
        if (app!!.observer.value == Constant.OBSERVER_EDIT_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE) {
            onBackPressed()
        } else if (app!!.observer.value == Constant.OBSERVER_REFRESH_PROFILE_DATA) {
            act.runOnUiThread {
                setApiCall()
            }
        } else if (app!!.observer.value == Constant.OBSERVER_NO_INTERNET_CONNECTION) {
            if (!act.isDestroyed && !act.isFinishing) {
                act.runOnUiThread {
                    try {
                        setApiCall()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private var screenAttach: Boolean = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        screenAttach = true
    }

    override fun onDetach() {
        super.onDetach()
        screenAttach = false
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        screenAttach = true
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
        if (screenAttach)
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