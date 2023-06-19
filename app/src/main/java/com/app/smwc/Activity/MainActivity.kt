package com.app.smwc.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityMainBinding
import com.app.smwc.fragments.Home.*
import com.app.smwc.fragments.Notification.NotificationFragment
import com.app.smwc.fragments.Profile.ProfileFragment.ProfileFragment
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityMainBinding? = null
    private var homeFragment: HomeFragment? = null
    private var historyFragment: HistoryFragment? = null
    private var notificationFragment: NotificationFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var imageViewArray: ArrayList<ImageView>? = null
    private var localFragmentCalled: Fragment? = null
    private var currentFragmentVisible = 0
    private val homeViewModel: HomeViewModel by viewModels()
    private var isHomeCLick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_main)
        Utils.makeStatusBarTransparent(this)
        initView()
        //setHomeResponse()
    }

    private fun setApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            homeViewModel.home(
                if (prefManager!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + prefManager!!.getUser()!!.token!! else ""
            )
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun setHomeResponse() {
        try {
            homeViewModel.homeResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            HELPER.print("GetOtpResponse::", gson!!.toJson(it.data))

                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.home),
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
                                getString(R.string.home),
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
                            getString(R.string.home),
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

    private fun initView() {
        loadFragment(HomeFragment())
        binding!!.Home.setOnClickListener(this)
        binding!!.History.setOnClickListener(this)
        binding!!.Notification.setOnClickListener(this)
        binding!!.Profile.setOnClickListener(this)

        imageViewArray = ArrayList()
        imageViewArray!!.add(binding!!.homeSelected)
        imageViewArray!!.add(binding!!.historySelected)
        imageViewArray!!.add(binding!!.notificationSelected)
        imageViewArray!!.add(binding!!.profileSelected)

        homeFragment = HomeFragment()
        historyFragment = HistoryFragment()
        profileFragment = ProfileFragment()
        notificationFragment = NotificationFragment()
        localFragmentCalled = homeFragment
        resetBottomNav()
        binding!!.imgHome.setImageResource(R.drawable.home_fill)
        binding!!.homeSelected.visibility = View.VISIBLE
        //setApiCall()
        //setPublic(homeFragment!!, "Home")
    }

    private fun setPublic(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null && currentFragment.javaClass == fragment.javaClass) {
            return
        }
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(tag)
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    private fun resetBottomNav() {
        if (imageViewArray != null) {
            for (image in imageViewArray!!) {
                image.visibility = View.GONE
            }
        }
        binding!!.imgHome.setImageResource(R.drawable.home_fill)
        binding!!.igHistory.setImageResource(R.drawable.history)
        binding!!.igNotification.setImageResource(R.drawable.notification)
        binding!!.igProfile.setImageResource(R.drawable.profile)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.Home -> {
                localFragmentCalled = homeFragment
                homeTabEnable(true)
                setPublic(homeFragment!!, "Home")
            }
            R.id.History -> {
                localFragmentCalled = historyFragment
                historyTabEnable(true)
                setPublic(historyFragment!!, "History")
            }
            R.id.Notification -> {
                localFragmentCalled = notificationFragment
                notificationTabEnable(true)
                setPublic(notificationFragment!!, "Notification")
            }
            R.id.Profile -> {
                localFragmentCalled = profileFragment
                profileTabEnable(true)
                setPublic(profileFragment!!, "Profile")
            }
        }
    }

    private fun homeTabEnable(isActive: Boolean) {
        if (isActive) {
            binding!!.imgHome.setImageResource(R.drawable.home_fill)
            binding!!.homeSelected.visibility = View.VISIBLE
            historyTabEnable(false)
            notificationTabEnable(false)
            profileTabEnable(false)
        } else {
            binding!!.imgHome.setImageResource(R.drawable.home)
            binding!!.homeSelected.visibility = View.GONE
        }
    }

    private fun historyTabEnable(isActive: Boolean) {
        if (isActive) {
            binding!!.igHistory.setImageResource(R.drawable.history_fill)
            binding!!.historySelected.visibility = View.VISIBLE
            homeTabEnable(false)
            notificationTabEnable(false)
            profileTabEnable(false)
        } else {
            binding!!.igHistory.setImageResource(R.drawable.history)
            binding!!.historySelected.visibility = View.GONE
        }
    }

    private fun notificationTabEnable(isActive: Boolean) {
        if (isActive) {
            binding!!.igNotification.setImageResource(R.drawable.fill_notification)
            binding!!.notificationSelected.visibility = View.VISIBLE
            historyTabEnable(false)
            homeTabEnable(false)
            profileTabEnable(false)
        } else {
            binding!!.igNotification.setImageResource(R.drawable.notification_icon)
            binding!!.notificationSelected.visibility = View.GONE
        }
    }

    private fun profileTabEnable(isActive: Boolean) {
        if (isActive) {
            binding!!.igProfile.setImageResource(R.drawable.profile_fill)
            binding!!.profileSelected.visibility = View.VISIBLE
            historyTabEnable(false)
            notificationTabEnable(false)
            homeTabEnable(false)
        } else {
            binding!!.igProfile.setImageResource(R.drawable.profile)
            binding!!.profileSelected.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        var handled = false
        if (currentFragmentVisible != Constant.OBSERVER_HOME_FRAGMENT_VISIBLE && currentFragmentVisible != Constant.OBSERVER_HISTORY_FRAGMENT_VISIBLE && currentFragmentVisible != Constant.OBSERVER_NOTIFICATION_FRAGMENT_VISIBLE && currentFragmentVisible != Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE) {
            handled = true
            if (currentFragmentVisible == Constant.OBSERVER_EDIT_FRAGMENT_VISIBLE) {
                app!!.observer.value =
                    Constant.OBSERVER_EDIT_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE
            }
        }
        if (!handled) {
            //super.onBackPressed();
            val fragmentsInStack = supportFragmentManager.backStackEntryCount
            HELPER.print("fragmentsInStack", fragmentsInStack.toString())
            if (fragmentsInStack > 1) {
                supportFragmentManager.popBackStack()
            } else if (fragmentsInStack == 1) {
                finishAffinity()
            } else {
                finishAffinity()
                super.onBackPressed()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun update(observable: Observable?, data: Any?) {
        super.update(observable, data)
        if (app.observer.value === Constant.OBSERVER_PROFILE_VISIBLE_FROM_HOME) {
            binding!!.Profile.performClick()
        } else if (app.observer.value === Constant.OBSERVER_HOME_FRAGMENT_VISIBLE) {
            currentFragmentVisible = Constant.OBSERVER_HOME_FRAGMENT_VISIBLE
            homeTabEnable(true)
        } else if (app.observer.value === Constant.OBSERVER_HISTORY_FRAGMENT_VISIBLE) {
            currentFragmentVisible = Constant.OBSERVER_HISTORY_FRAGMENT_VISIBLE
            historyTabEnable(true)
        } else if (app.observer.value === Constant.OBSERVER_NOTIFICATION_FRAGMENT_VISIBLE) {
            currentFragmentVisible = Constant.OBSERVER_NOTIFICATION_FRAGMENT_VISIBLE
            notificationTabEnable(true)
        } else if (app.observer.value === Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE) {
            currentFragmentVisible = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
            profileTabEnable(true)
        } else if (app.observer.value === Constant.OBSERVER_HISTORY_BACK_PRESS_FRAGMENT_VISIBLE) {
            super.onBackPressed()
        } else if (app.observer.value === Constant.OBSERVER_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE) {
            super.onBackPressed()
        } else if (app.observer.value === Constant.OBSERVER_EDIT_FRAGMENT_VISIBLE) {
            currentFragmentVisible = Constant.OBSERVER_EDIT_FRAGMENT_VISIBLE
        }
    }
}