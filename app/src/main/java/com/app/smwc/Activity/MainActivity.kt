package com.app.smwc.Activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.frimline.views.Utils
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.R
import com.app.smwc.databinding.ActivityMainBinding
import com.app.smwc.fragments.Home.HistoryFragment
import com.app.smwc.fragments.Home.HomeFragment
import com.app.smwc.fragments.Home.NotificationFragment
import com.app.smwc.fragments.Home.ProfileFragment
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityMainBinding? = null
    private var homeFragment: HomeFragment? = null
    private var historyFragment: HistoryFragment? = null
    private var notificationFragment: NotificationFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var imageViewArray: ArrayList<ImageView>? = null
    private var localFragmentCalled: Fragment? = null
    private var currentFragmentVisible = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_main)
        Utils.makeStatusBarTransparent(this)
        initView()
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
        setPublic(homeFragment!!, "Home")
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
                HELPER.print("IsHomeClick", "YES")
                localFragmentCalled = homeFragment
                homeTabEnable(true)
                setPublic(homeFragment!!, "Home")
            }
            R.id.History -> {
                HELPER.print("IsHistoryClick", "YES")
                localFragmentCalled = historyFragment
                historyTabEnable(true)
                setPublic(historyFragment!!, "History")
            }
            R.id.Notification -> {
                HELPER.print("IsNotificationClick", "YES")
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
            binding!!.igNotification.setImageResource(R.drawable.notification)
            binding!!.notificationSelected.visibility = View.VISIBLE
            historyTabEnable(false)
            homeTabEnable(false)
            profileTabEnable(false)
        } else {
            binding!!.igNotification.setImageResource(R.drawable.notification)
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