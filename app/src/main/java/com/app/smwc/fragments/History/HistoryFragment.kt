package com.app.smwc.fragments.Home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.FragmentHistoryBinding
import com.app.smwc.fragments.BaseFragment
import com.app.smwc.fragments.History.HistoryViewModel
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), View.OnClickListener {

    private var homeAdapter: HomeAdapter? = null
    private val historyViewModel: HistoryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SWCApp.getInstance().observer.value = Constant.OBSERVER_HISTORY_FRAGMENT_VISIBLE
        initViews()
        setApiCall()
        iniRefreshListener()
    }

    fun iniRefreshListener() {
        binding.swipeContainer.setOnRefreshListener {
            setApiCall()
            val handler = Handler()
            handler.postDelayed({
                if (binding.swipeContainer.isRefreshing) {
                    binding.swipeContainer.isRefreshing = false
                }
            }, 1500)
        }
    }
    private fun setApiCall() {
        if (Utils.hasNetwork(act)) {
            Loader.showProgress(act)
            historyViewModel.history(
                if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
            )
            setHistoryResponse()
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
                        if (it.data!!.status == 1 && it.data.data != null) {
                            if (it.data.data!!.orders != null && it.data.data!!.orders.size != 0) {
                                HELPER.print("HistoryResponse::", gson!!.toJson(it.data))
                                binding.historyMainLayout.visibility = View.VISIBLE
                                binding.emptyText.visibility = View.GONE
                                setAdapter(it.data.data!!.orders)
                            } else {
                                binding.historyMainLayout.visibility = View.GONE
                                binding.emptyText.visibility = View.VISIBLE
                            }
//                            PubFun.commonDialog(
//                                act,
//                                getString(R.string.title_history),
//                                it.data.message!!.ifEmpty { "Server Error" },
//                                false,
//                                clickListener = {
//                                })
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
                        Loader.showProgress(act)
                        HELPER.print("Network", "loading")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //    {
//        "status": 1,
//        "message": "data get successfully",
//        "data": {
//        "orders": [
//        {
//            "total_amount": 60000,
//            "order_number": "DST00023",
//            "company_title": "Expanded Tertiary Migration",
//            "address": "1956 McClure Street suite 350, Homestead, PA, USA",
//            "status": 0,
//            "status_name": "Pending",
//            "currency": "$",
//            "date": "16/06/2023",
//            "id": 9,
//            "item_pickup_up": 5000,
//            "token_no": "Lu0004"
//        }
//        ]
//    }
//    }
    private fun setAdapter(orderList: ArrayList<Orders>) {
        binding.rootRecyclerList.layoutManager = LinearLayoutManager(
            act,
            LinearLayoutManager.VERTICAL,
            false
        )
        val listClickListener =
            ListClickListener { _, _, _ ->
            }

        homeAdapter = HomeAdapter(
            act,
            orderList,
            listClickListener
        )
        binding.rootRecyclerList.adapter = homeAdapter
    }

    private fun initViews() {
        binding.toolbar.title.text = getString(R.string.title_history)
        binding.toolbar.ivBack.setOnClickListener {
            app!!.observer.value = Constant.OBSERVER_HISTORY_BACK_PRESS_FRAGMENT_VISIBLE
        }
    }

    override fun onClick(view: View?) {
        when (requireView().id) {
            binding.toolbar.ivBack.id -> {
                val intent = Intent(act, MainActivity::class.java)
                act.startActivity(intent)
                HELPER.slideEnter(act)
            }
        }
    }
}