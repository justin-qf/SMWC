package com.app.smwc.fragments.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.smwc.Activity.CompanyInfo.CompanyInfoActivity
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.databinding.FragmentHomeBinding
import com.app.smwc.fragments.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), View.OnClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private var homeAdapter: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("param1")
            param2 = it.getString("param2")
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SWCApp.getInstance().observer.value = Constant.OBSERVER_HOME_FRAGMENT_VISIBLE
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
//            val i = Intent(act, CompanyInfoActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(i)
//            HELPER.slideEnter(act)
            //HELPER.print("IS_CLICK", "DONE")
        }
        binding.toolbar.ivProfile.setOnClickListener {
//            val i = Intent(act, CompanyInfoActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(i)
//            HELPER.slideEnter(act)
        }
        storeDetails
    }

    override fun onClick(view: View?) {

        HELPER.print("IS_CLICK", id.toString())
        HELPER.print("IS_CLICK", binding.toolbar.ivQrcode.id.toString())

        when (requireView().id) {
            binding.toolbar.ivQrcode.id -> {
                val intent = Intent(act, MainActivity::class.java)
                act.startActivity(intent)
                HELPER.slideEnter(act)
            }
        }
    }
}