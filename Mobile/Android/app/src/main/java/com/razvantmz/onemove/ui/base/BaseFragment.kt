package com.razvantmz.onemove.ui.base

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.razvantmz.onemove.core.responseHandlers.ResponseHandler

open class BaseFragment<B : ViewBinding, T : BaseViewModel> : Fragment() {

    lateinit var binding:B
    lateinit var viewModel:T
    lateinit var responseHandler: ResponseHandler
    lateinit var coordinatorLayout: CoordinatorLayout

    fun isViewModelInitialised():Boolean
    {
        return ::viewModel.isInitialized
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = binding.root
        if(isViewModelInitialised())
        {
            responseHandler = ResponseHandler(root, viewModel, viewLifecycleOwner)
            bindDialog(viewModel.isUpdating)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    fun getFragmentContext(): Context
    {
        return this.context!!
    }

    var dialogView: Dialog? = null

    fun showProgressDialog(context: Context)
    {
        dialogView = Dialog(context, R.style.Theme_Material_Light_Dialog)
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
        isUpdating.observe(viewLifecycleOwner, Observer { update->
            if(update)
            {
                context?: return@Observer
                showProgressDialog(context!!)
            }
            else
            {
                dismissProgressDialog()
            }
        })
    }

    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }

}