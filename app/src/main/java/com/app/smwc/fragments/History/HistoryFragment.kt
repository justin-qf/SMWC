package com.app.smwc.fragments.Home

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
import com.app.smwc.Common.SWCApp
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.FragmentHistoryBinding
import com.app.smwc.fragments.BaseFragment
import com.app.smwc.fragments.History.HistoryAdapter
import com.app.smwc.fragments.History.HistoryViewModel
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), View.OnClickListener {

    private var historyAdapter: HistoryAdapter? = null
    private val historyViewModel: HistoryViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SWCApp.getInstance().observer.value = Constant.OBSERVER_HISTORY_FRAGMENT_VISIBLE
        initViews()
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
            setHistoryResponse()
            historyViewModel.history(
                if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
            )
        } else {
            HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
        }
    }

    private fun setHistoryResponse() {
        try {
            historyViewModel.historyResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        setShimmerWithSwipeContainer()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            HELPER.print("HistoryResponse::", gson!!.toJson(it.data))
                            if (it.data.data!!.orders != null && it.data.data!!.orders.size != 0) {
                                binding.historyMainLayout.visibility = View.VISIBLE
                                binding.emptyText.visibility = View.GONE
                                setHeaderWithItemLayout(it.data.data!!.orders)
                            } else {
                                binding.historyMainLayout.visibility = View.GONE
                                binding.emptyText.visibility = View.VISIBLE
                            }
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.title_history),
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
                                getString(R.string.title_history),
                                it.data.message!!.ifEmpty { "Server Error" },
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
                            getString(R.string.title_history),
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
        binding.historyMainLayout.visibility = View.GONE
        setApiCall()
    }

    private fun setShimmerWithSwipeContainer() {
        if (binding.swipeContainer.isRefreshing) {
            binding.swipeContainer.isRefreshing = false
        }
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun setHeaderWithItemLayout(orderList: ArrayList<Orders>) {
        val taskByDate = orderList.groupBy { it.date }
        val orderArrayList: ArrayList<OrdersType> = ArrayList()
        for (list in taskByDate) {
            orderArrayList.add(
                OrdersType(
                    date = list.key.toString(),
                    type = Constant.IS_DATE
                )
            )
            for (model in list.value) {
                orderArrayList.add(
                    OrdersType(
                        order = model,
                        type = Constant.IS_ITEM
                    )
                )
            }
        }
        setAdapter(orderArrayList)
    }

    private fun setAdapter(orderList: ArrayList<OrdersType>) {
        binding.rootRecyclerList.layoutManager = LinearLayoutManager(
            act,
            LinearLayoutManager.VERTICAL,
            false
        )
        val listClickListener =
            ListClickListener { _, _, _ ->
            }

        historyAdapter = HistoryAdapter(
            act,
            orderList,
            listClickListener
        )
        binding.rootRecyclerList.adapter = historyAdapter
        binding.rootRecyclerList.setHasFixedSize(true)
    }

    private fun initViews() {
        binding.toolbar.title.text = getString(R.string.title_history)
        binding.toolbar.ivBack.setOnClickListener(this)
        setApiCall()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.toolbar.ivBack.id -> {
                app!!.observer.value = Constant.OBSERVER_HISTORY_BACK_PRESS_FRAGMENT_VISIBLE
            }
        }
    }
}