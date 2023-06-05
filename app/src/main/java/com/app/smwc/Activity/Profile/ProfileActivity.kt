package com.app.smwc.Activity.Profile

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.frimline.views.Utils
import com.app.smwc.Activity.BaseActivity
import com.app.smwc.R
import com.app.smwc.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private var binding: ActivityProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(act, R.layout.activity_profile)
        Utils.makeStatusBarTransparent(this)
        initView()
    }

    private fun initView() {
        binding!!.toolbar.ivBack.setOnClickListener(this)
        binding!!.toolbar.title.text = getString(R.string.title_profile)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding!!.toolbar.ivBack.id -> {

            }

        }
    }
}