package com.razvantmz.onemove.ui.selectItems

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.razvantmz.onemove.models.SelectItem
import com.razvantmz.onemove.ui.base.BaseViewModel

class SelectItemsViewModel : BaseViewModel() {

    var selectedItem: MutableLiveData<SelectItem> = MutableLiveData()

    private var _itemList: MutableLiveData<ArrayList<SelectItem>> = MutableLiveData()
    private var _toolbarTitle: MutableLiveData<String> = MutableLiveData()


    val itemList: LiveData<ArrayList<SelectItem>> = _itemList
    val toolbarTitle: LiveData<String> = _toolbarTitle


    fun setItemList(list: ArrayList<SelectItem>?)
    {
        list?:return
        _itemList.value = list
    }

    fun setToolbarTitle(title:String)
    {
        _toolbarTitle.value = title
    }
}