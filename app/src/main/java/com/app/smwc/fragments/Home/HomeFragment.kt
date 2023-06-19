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
import com.app.smwc.Activity.LoginActivity.LoginActivity
import com.app.smwc.Activity.MainActivity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var homeAdapter: HomeAdapter? = null
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var isReload: Boolean = false

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
        // Make the API call
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (Utils.hasNetwork(act)) {
                    //Loader.showProgress(act)
                    // setHomeResponse()
                    homeViewModel.home(
                        if (pref!!.getUser()!!.token!!.isNotEmpty()) "Bearer " + pref!!.getUser()!!.token!! else ""
                    )
                } else {
                    HELPER.commonDialog(act, Constant.NETWORK_ERROR_MESSAGE)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exception
            }
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
                            HELPER.print("GetHOMEResponse::", gson!!.toJson(it.data))
                            setStoreTitle(it.data.data!!.orders)
                            setTokenWithCountLayout(it.data.data!!)
                            setAdapter(it.data.data!!.orders)
//                            PubFun.commonDialog(
//                                act,
//                                getString(R.string.home),
//                                it.data.message!!.ifEmpty { "Server Error" },
//                                false,
//                                clickListener = {
//                                })
                        } else if (it.data.status == 2) {
                            PubFun.commonDialog(
                                act,
                                getString(R.string.home),
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
                                getString(R.string.home),
                                it.data.message!!.ifEmpty { "Server Error" },
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
        binding.toolbar.ivQrcode.setOnClickListener(this)
        binding.toolbar.ivQrcode.setOnClickListener {
            val i = Intent(act, ScannerActivity::class.java)
            startActivity(i)
            HELPER.slideEnter(act)
        }
        binding.toolbar.ivProfile.setOnClickListener {
            try {
                app!!.observer.value = Constant.OBSERVER_PROFILE_VISIBLE_FROM_HOME
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (pref!!.getUser() != null && pref!!.getUser()!!.firstName != null && pref!!.getUser()!!.firstName!!.isNotEmpty() && pref!!.getUser()!!.lastName != null && pref!!.getUser()!!.lastName!!.isNotEmpty()) {
            binding.userNameTxt.text =
                "Hello, " + pref!!.getUser()!!.firstName + " " + pref!!.getUser()!!.lastName
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            setHomeResponse()
            HELPER.print("onAttach", "DONE")
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener.  */
        }
    }


    @Deprecated("Deprecated in Java")
    override fun update(observable: Observable?, data: Any?) {
        super.update(observable, data)
        if (app!!.observer.value == Constant.OBSERVER_REFRESH_DASHBOARD_DATA) {
            act.runOnUiThread {
                setApiCall()
            }
        }
    }

    override fun onClick(view: View?) {
        when (requireView().id) {
            binding.toolbar.ivQrcode.id -> {
                val intent = Intent(act, MainActivity::class.java)
                act.startActivity(intent)
                HELPER.slideEnter(act)
            }
        }
    }
}