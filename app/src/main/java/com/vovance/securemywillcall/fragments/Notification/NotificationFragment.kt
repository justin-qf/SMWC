package com.vovance.securemywillcall.fragments.Notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vovance.frimline.views.Utils
import com.vovance.omcsalesapp.Common.PubFun
import com.vovance.securemywillcall.R
import com.vovance.securemywillcall.common.Constant
import com.vovance.securemywillcall.common.HELPER
import com.vovance.securemywillcall.databinding.FragmentNotificationBinding
import com.vovance.securemywillcall.fragments.BaseFragment
import com.vovance.securemywillcall.interfaces.ListClickListener
import com.vovance.ssn.Utils.Loader
import com.vovance.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
        iniRefreshListener()
    }

    private fun iniRefreshListener() {
        binding.swipeContainer.setOnRefreshListener {
            setSwipeContainer()
        }
    }

    private fun setApiCall() {
        if (Utils.hasNetwork(act)) {
            //Loader.showProgress(act)
            notificationViewModel.notification(
                if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
            )
            setNotificationResponse()
        } else {
            //HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun setNotificationResponse() {
        try {
            notificationViewModel.notificationResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        setShimmerWithSwipeContainer()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            HELPER.print("NotificationResponse::", gson!!.toJson(it.data))
                            if (it.data.data != null && it.data.data.size != 0) {
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
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                    PubFun.openLoginScreen(act, pref)
                                })
                        } else {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.notification_title),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
                                false,
                                clickListener = {
                                })
                        }
                    }
                    is NetworkResult.Error -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
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
        binding.notificationMainLayout.visibility = View.GONE
        setApiCall()
    }

    private fun setShimmerWithSwipeContainer() {
        if (binding.swipeContainer.isRefreshing) {
            binding.swipeContainer.isRefreshing = false
        }
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
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
        binding.toolbar.ivBack.setOnClickListener(this)
    }

    @Deprecated("Deprecated in Java")
    override fun update(observable: Observable?, data: Any?) {
        super.update(observable, data)
        if (app!!.observer.value == Constant.OBSERVER_NO_INTERNET_CONNECTION) {
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
    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.toolbar.ivBack.id -> {
                app!!.observer.value = Constant.OBSERVER_NOTIFICATION_BACK_PRESS_FRAGMENT_VISIBLE
            }
        }
    }
}