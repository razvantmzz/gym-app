package com.razvantmz.onemove.ui.selectItems

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.SelectableItemsAdapter
import com.razvantmz.onemove.databinding.ActivitySelectItemsBinding
import com.razvantmz.onemove.models.SelectItem
import com.razvantmz.onemove.ui.base.BaseActivity

class SelectItemsActivity : BaseActivity<ActivitySelectItemsBinding, SelectItemsViewModel>(), SelectableItemsAdapter.Interaction {
    companion object
    {
        const val DATA = "selectableData"
        const val TITLE = "title"
    }

    private lateinit var listAdapter: SelectableItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySelectItemsBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(SelectItemsViewModel::class.java)
        super.onCreate(savedInstanceState)

        val bundle = intent.extras ?: return
        viewModel.setToolbarTitle(bundle.getString(TITLE, getString(R.string.title_select_item)))
        viewModel.setItemList(bundle.getParcelableArrayList<SelectItem>(DATA))

        listAdapter = SelectableItemsAdapter(this, viewModel.selectedItem, this)
        binding.dataRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.dataRecyclerView.adapter = listAdapter

        viewModel.itemList.observe(this, Observer {
            listAdapter.submitList(it)
        })

        viewModel.toolbarTitle.observe(this, Observer {
            binding.toolbar.toolbarTitle.text = it
        })

        binding.toolbar.cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.toolbar.saveBtn.setOnClickListener {
            intent.putExtra(DATA, viewModel.selectedItem.value)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


    }

    override fun onItemSelected(position: Int, item: SelectItem) {
        viewModel.selectedItem.value = item
    }
}