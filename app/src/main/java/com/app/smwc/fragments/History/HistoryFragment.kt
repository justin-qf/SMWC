package com.app.smwc.fragments.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.smwc.Activity.MainActivity
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Common.SWCApp
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.FragmentHistoryBinding
import com.app.smwc.databinding.FragmentHomeBinding
import com.app.smwc.fragments.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), View.OnClickListener {

    private var homeAdapter: HomeAdapter? = null

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
        HELPER.print("IsCall:::::::", "HistoryFragment")
        SWCApp.getInstance().observer.value = Constant.OBSERVER_HISTORY_FRAGMENT_VISIBLE
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
        storeDetails
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