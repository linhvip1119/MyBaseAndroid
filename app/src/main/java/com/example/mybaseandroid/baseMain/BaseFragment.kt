package com.example.mybaseandroid.baseMain

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.mybaseandroid.MainActivity
import timber.log.Timber

abstract class BaseFragment : Fragment, View.OnClickListener {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var mIsReloadData = true
    protected var mainActivity: MainActivity? = null
    protected lateinit var mSafeContext: Context
    private var dialog: Dialog? = null
    override fun onClick(v: View?) = Unit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mSafeContext = context
    }

    fun getIsReloadData() = mIsReloadData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is MainActivity) {
            mainActivity = activity as MainActivity
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    fun isReloadData(): Boolean {
        return mIsReloadData
    }

    override fun startActivity(intent: Intent?) {
        mainActivity?.startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
//    hideKeyBoard()
    }

//open fun hideKeyBoard() = activity.hideKeyboard()

    override fun onResume() {
        super.onResume()
        Timber.d("nameScreen = ${this.javaClass.simpleName}")
        if (!mIsReloadData) return
        mIsReloadData = false
    }

    private fun initObserver() {
        // todo
    }

    open fun setReloadData(isReloadData: Boolean) {
        mIsReloadData = isReloadData
    }

    open fun disableBackPressed(): Boolean = false

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    open fun dismissDialogFromAppsFlyers() {
        dialog?.dismiss()
    }
}