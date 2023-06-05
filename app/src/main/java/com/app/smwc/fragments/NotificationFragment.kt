package com.app.smwc.fragments.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.smwc.Common.Constant
import com.app.smwc.Common.SWCApp
import com.app.smwc.databinding.FragmentNotificationBinding
import com.app.smwc.fragments.BaseFragment

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(), View.OnClickListener {


    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SWCApp.getInstance().observer.value = Constant.OBSERVER_NOTIFICATION_FRAGMENT_VISIBLE
        initViews()
    }


    private fun initViews() {

    }

    override fun onClick(view: View?) {

    }
}