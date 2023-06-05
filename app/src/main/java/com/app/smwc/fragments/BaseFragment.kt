package com.app.smwc.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.app.frimline.views.Utils
import com.app.smwc.Common.SWCApp
import com.app.ssn.Common.Pref
import com.google.gson.Gson
import java.util.*

abstract class BaseFragment<V : ViewBinding> : Fragment(), Observer {
    lateinit var binding: V

    //var binding: V?=null
    var app: SWCApp? = null
    var pref: Pref? = null
    lateinit var act: Activity
    var gson: Gson? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = requireActivity()
        gson = Gson()
        app = act.application as SWCApp
        app!!.observer.addObserver(this)

        pref = Pref(act)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = onCreateBinding(inflater, container, savedInstanceState)
        this.binding = binding
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        //binding.
    }

    abstract fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V


    override fun update(observable: Observable?, data: Any?) {}

    open fun hideKeyboard(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                Utils.hideKeyboard(act)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideKeyboard(innerView)
            }
        }
    }

    open fun onBackPressed(): Boolean {
        return false
    }
}