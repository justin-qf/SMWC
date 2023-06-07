package com.app.smwc.fragments.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.smwc.Activity.CompanyInfo.CompanyInfoActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Activity.OtpActivity.OtpActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.databinding.FragmentHomeBinding
import com.app.smwc.fragments.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var homeAdapter: HomeAdapter? = null
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
    }

    private val storeDetails: Unit
        get() {
            binding.rootRecyclerList.layoutManager = LinearLayoutManager(
                act,
                LinearLayoutManager.VERTICAL,
                false
            )
            val listClickListener =
                ListClickListener { view, pos, `object` ->
                    val intent = Intent(act, MainActivity::class.java)
                    act.startActivity(intent)
                    HELPER.slideEnter(act)
                }

            homeAdapter = HomeAdapter(
                act,
                listClickListener
            )
            binding.rootRecyclerList.adapter = homeAdapter
        }

    private fun initViews() {
        binding.toolbar.ivQrcode.setOnClickListener(this)
        binding.toolbar.ivQrcode.setOnClickListener {
            val i = Intent(act, CompanyInfoActivity::class.java)
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            HELPER.slideEnter(act)
        }
        binding.toolbar.ivProfile.setOnClickListener {
//            val i = Intent(act, OtpActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(i)
//            HELPER.slideEnter(act)
            app!!.observer.value = Constant.OBSERVER_PROFILE_VISIBLE_FROM_HOME
        }
        storeDetails
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