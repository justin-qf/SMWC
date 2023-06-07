package com.app.smwc.fragments.Home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.R
import com.app.smwc.databinding.FragmentProfileBinding
import com.app.smwc.fragments.BaseFragment
import com.app.ssn.Common.Pref
import java.util.*

class ProfileFragment : BaseFragment<FragmentProfileBinding>(), View.OnClickListener {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HELPER.print("IsCall:::::::", "ProfileFragment")
        SWCApp.getInstance().observer.value = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
        initViews()
    }


    private fun initViews() {
        binding.toolbar.ivBack.setOnClickListener(this)
        //binding.ivEditProfile.setOnClickListener(this)
        binding.toolbar.editLayout.setOnClickListener {
            HELPER.print("----------IS_CLICK--------", "DONE")
            setPublic(EditFragment(), "Edit")
        }
        binding.toolbar.ivBack.setOnClickListener {
            app!!.observer.value = Constant.OBSERVER_PROFILE_BACK_PRESS_FRAGMENT_VISIBLE
        }
        binding.toolbar.title.text = getString(R.string.title_profile)
        binding.toolbar.editLayout.visibility = View.VISIBLE
        binding.toolbar.submitLayout.visibility = View.GONE
    }

    override fun onClick(view: View?) {

        when (requireView().id) {
            binding.toolbar.ivBack.id -> {

            }
            R.id.iv_edit_profile -> {
                //HELPER.print("ISCLICK", "DONE")
                //setPublic(EditFragment(), "history")
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
        HELPER.print("IS_STACK", "PARENT")
        val fragmentList = childFragmentManager.fragments
        return if (fragmentList.size != 0) {
            HELPER.print("IS_STACK", "MAIN")
            val fragmentsInStack = childFragmentManager.backStackEntryCount
            if (fragmentsInStack > 1) {
                HELPER.print("IS_STACK", "1")
                childFragmentManager.popBackStack()
                true
            } else if (fragmentsInStack == 1) {
                HELPER.print("IS_STACK", "2")
                childFragmentManager.popBackStack()
                binding.profileFragmentContainer.visibility = View.GONE
                binding.nestedLayout.visibility = View.VISIBLE
                pref = Pref(act)
                initViews()
                SWCApp.getInstance().observer.value = Constant.OBSERVER_PROFILE_FRAGMENT_VISIBLE
                true
            } else {
                HELPER.print("IS_STACK", "0")
                false
            }
        } else {
            HELPER.print("IS_STACK", "ELSE")
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