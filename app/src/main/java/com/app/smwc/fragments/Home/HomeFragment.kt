package com.app.smwc.fragments.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.frimline.views.Utils
import com.app.omcsalesapp.Common.PubFun
import com.app.smwc.APIHandle.NoConnectivityException
import com.app.smwc.APIHandle.NoInternetException
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Activity.ScannerActivity.ScannerActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.FragmentHomeBinding
import com.app.smwc.fragments.BaseFragment
import com.app.ssn.Utils.Loader
import com.app.ssn.Utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var homeAdapter: HomeAdapter? = null
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app!!.observer.value = Constant.OBSERVER_HOME_FRAGMENT_VISIBLE
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
        try {
            if (Utils.hasNetwork(act)) {
                //Loader.showProgress(act)
                homeViewModel.home(
                    if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
                )
            } else {
                //HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
            }
        } catch (e: IOException) {
            HELPER.print("IO","EXCEPTION")
            e.printStackTrace()
        } catch (e: Exception) {
            HELPER.print("E","EXCEPTION")
            e.printStackTrace()
        }
    }

    private fun setHomeResponse() {
        try {
            homeViewModel.homeResponseLiveData.observe(this) {
                when (it) {
                    is NetworkResult.Success -> {
                        Loader.hideProgress()
                        setShimmerWithSwipeContainer()
                        if (it.data!!.status == 1 && it.data.data != null) {
                            HELPER.print("HomeResponse::", gson!!.toJson(it.data))
                            setStoreTitle(it.data.data!!.orders)
                            setTokenWithCountLayout(it.data.data!!)
                            setAdapter(it.data.data!!.orders)
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.home),
                                it.data.message!!.ifEmpty { getString(R.string.serverErrorMessage) },
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
                                getString(R.string.home),
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
                        binding.homeMainLayout.visibility = View.VISIBLE
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
                        //Loader.showProgress(act)
                        HELPER.print("Network", "loading")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: NoInternetException) {
            HELPER.print("NoInternetException", "Done")
            HELPER.print("NoInternetException", e.message)

        } catch (e: NoConnectivityException) {
            HELPER.print("NoConnectivityException", "Done")
            HELPER.print("NoConnectivityException", e.message)
        }
    }

    private fun setSwipeContainer() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.homeMainLayout.visibility = View.GONE
        setApiCall()
    }

    private fun setShimmerWithSwipeContainer() {
        if (binding.swipeContainer.isRefreshing) {
            binding.swipeContainer.isRefreshing = false
        }
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.homeMainLayout.visibility = View.VISIBLE
    }

    private fun setStoreTitle(orderList: ArrayList<Orders>?) {
        if (orderList != null && orderList.size != 0) {
            binding.storeTitle.visibility = View.VISIBLE
        } else {
            binding.storeTitle.visibility = View.GONE
        }
    }

    private fun setTokenWithCountLayout(homeData: HomeData?) {
        if (homeData!!.queueToken != null && homeData.queueToken.toString()
                .isNotEmpty()
        ) {
            binding.tokenLayout.visibility = View.VISIBLE
            binding.tokenNumberTxt.text = homeData.queueToken
        } else {
            binding.tokenLayout.visibility = View.GONE
        }

        if (homeData.completedOrder != null && homeData.completedOrder.toString()
                .isNotEmpty()
        ) {
            binding.completeCountTxt.text =
                homeData.completedOrder.toString().trim()
        } else {
            binding.completeCountTxt.text = ""
        }

        if (homeData.pendingOrder != null && homeData.pendingOrder.toString()
                .isNotEmpty()
        ) {
            binding.pendingCountTxt.text =
                homeData.pendingOrder.toString().trim()
        } else {
            binding.pendingCountTxt.text = ""
        }
    }

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
        binding.rootRecyclerList.setHasFixedSize(true)
    }

    private fun initViews() {
        binding.completeCardview.setOnClickListener(this)
        binding.toolbar.ivQrcode.setOnClickListener(this)
        binding.toolbar.ivProfile.setOnClickListener(this)
        if (pref!!.getUser() != null && pref!!.getUser()!!.firstName != null && pref!!.getUser()!!.firstName!!.isNotEmpty() && pref!!.getUser()!!.lastName != null && pref!!.getUser()!!.lastName!!.isNotEmpty()) {
            binding.userNameTxt.text =
                "Hello, " + pref!!.getUser()!!.firstName + " " + pref!!.getUser()!!.lastName
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            setHomeResponse()
        } catch (castException: ClassCastException) {
            castException.printStackTrace()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun update(observable: Observable?, data: Any?) {
        super.update(observable, data)
        if (app!!.observer.value == Constant.OBSERVER_REFRESH_DASHBOARD_DATA) {
            act.runOnUiThread {
                try {
                    setApiCall()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.toolbar.ivQrcode.id -> {
                val i = Intent(act, ScannerActivity::class.java)
                startActivity(i)
                HELPER.slideEnter(act)
            }
            binding.toolbar.ivProfile.id -> {
                try {
                    app!!.observer.value = Constant.OBSERVER_PROFILE_VISIBLE_FROM_HOME
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}