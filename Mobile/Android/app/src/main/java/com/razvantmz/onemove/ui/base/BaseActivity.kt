package com.razvantmz.onemove.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.interfaces.IViewContainer
import com.razvantmz.onemove.core.responseHandlers.ResponseHandler
import kotlinx.android.synthetic.main.fragment_routes.view.*

abstract class BaseActivity<B : ViewBinding, T : BaseViewModel> : AppCompatActivity(), IViewContainer {

    lateinit var binding:B
    lateinit var viewModel:T
    lateinit var responseHandler: ResponseHandler
    override var coordinatorLayout: CoordinatorLayout?= null
    override var fragmentManager: FragmentManager?
        get() = this.supportFragmentManager
        set(value) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentContainer()
        setContentView(binding.root)
        coordinatorLayout = binding.root.findViewById(R.id.coordinatorLayout)
        responseHandler = ResponseHandler(binding.root, viewModel, this)
        bindDialog(viewModel.isUpdating)
    }

    override fun onResume() {
        super.onResume()
        setCurrentContainer()
    }

    override fun onDestroy() {
        super.onDestroy()
        CoreApplication.currentActivity = null

    }
    var dialogView: Dialog? = null

    fun showProgressDialog(context: Context)
    {
        dialogView = Dialog(context, android.R.style.Theme_Material_Light_Dialog)
        dialogView!!.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView!!.window?.setBackgroundDrawableResource(com.razvantmz.onemove.R.color.transparent)
        dialogView!!.setContentView(com.razvantmz.onemove.R.layout.progress_dialog)
        dialogView!!.setCancelable(false)
        dialogView!!.show()
    }

    fun dismissProgressDialog()
    {
        dialogView?:return
        dialogView!!.dismiss()
    }

    fun bindDialog(isUpdating: LiveData<Boolean>)
    {
        isUpdating.observe(this, Observer { update->
            if(update)
            {
                showProgressDialog(this)
            }
            else
            {
                dismissProgressDialog()
            }
        })
    }

    private fun setCurrentContainer()
    {
        if(CoreApplication.currentActivity != this)
        {
            CoreApplication.currentActivity = this
        }
    }

    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }
}