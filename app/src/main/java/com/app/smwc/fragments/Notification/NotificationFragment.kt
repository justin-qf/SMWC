package com.app.smwc.fragments.Notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.FragmentNotificationBinding
import com.app.smwc.fragments.BaseFragment
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(), View.OnClickListener {

    private val notificationViewModel: NotificationViewModel by activityViewModels()
    private var notificationAdapter: NotificationAdapter? = null

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app!!.observer.value = Constant.OBSERVER_NOTIFICATION_FRAGMENT_VISIBLE
        initViews()
        setApiCall()
    }

    private fun setApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            notificationViewModel.notification(
                if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
            )
            setNotificationResponse()
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun setNotificationResponse() {
        try {
            notificationViewModel.notificationResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            if (it.data.data != null && it.data.data.size != 0) {
                                HELPER.print("HistoryResponse::", gson!!.toJson(it.data))
                                binding.notificationMainLayout.visibility = View.VISIBLE
                                binding.emptyText.visibility = View.GONE
                                setAdapter(it.data.data)
                            } else {
                                binding.notificationMainLayout.visibility = View.GONE
                                binding.emptyText.visibility = View.VISIBLE
                            }

                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.notification_title),
                                it.data.message!!.ifEmpty { "Server Error" },
                                false,
                                clickListener = {
                                    pref!!.Logout()
                                    val i = Intent(act, LoginActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    act.startActivity(i)
                                    act.finish()
                                    HELPER.slideEnter(act)
                                })
                        } else {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.notification_title),
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
                            getString(R.string.notification_title),
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

    private fun setAdapter(notificationList: ArrayList<NotificationData>) {
        binding.rootRecyclerList.layoutManager = LinearLayoutManager(
            act,
            LinearLayoutManager.VERTICAL,
            false
        )
        val listClickListener =
            ListClickListener { _, _, _ ->
            }

        notificationAdapter = NotificationAdapter(
            act,
            notificationList,
            listClickListener
        )
        binding.rootRecyclerList.adapter = notificationAdapter
    }

    private fun initViews() {
        binding.toolbar.title.text = getString(R.string.notification_title)
        binding.toolbar.ivBack.setOnClickListener {
            app!!.observer.value = Constant.OBSERVER_HISTORY_BACK_PRESS_FRAGMENT_VISIBLE
        }
    }

    override fun onClick(view: View?) {

    }
}