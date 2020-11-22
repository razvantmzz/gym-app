package com.razvantmz.onemove.ui.ranks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.EventListAdapter
import com.razvantmz.onemove.databinding.FragmentRanksBinding
import com.razvantmz.onemove.models.postModels.EventPost
import com.razvantmz.onemove.ui.addEvent.AddEventActivity
import com.razvantmz.onemove.ui.base.BaseFragment
import com.razvantmz.onemove.ui.eventDetails.EventDetailsActivity

class RanksFragment : BaseFragment<FragmentRanksBinding, RanksViewModel>(), EventListAdapter.Interaction {

    private lateinit var listAdapter: EventListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRanksBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(RanksViewModel::class.java)
        setUpRecyclerView()
        setToolbar()

        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            listAdapter.itemSource = it
            listAdapter.notifyDataSetChanged()
        })
        return binding.root
    }

    private fun setUpRecyclerView() {
        val viewManager = LinearLayoutManager(binding.root.context)
        listAdapter = EventListAdapter(arrayListOf(), this)
        binding.rvFrgRanks.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = listAdapter
        }
    }

    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.title_events)
        binding.toolbar.btnToolbarRight.setImageDrawable(context?.getDrawable(R.drawable.ic_add))
        binding.toolbar.btnToolbarRight.setOnClickListener { onAddBtnClicked() }
    }

    private fun onAddBtnClicked()
    {
        val intent = Intent(context, AddEventActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClicked(item: EventPost) {
        val intent = Intent(context, EventDetailsActivity::class.java)
        intent.putExtra(EventDetailsActivity.EVENT_ID, item.id)
        startActivity(intent)
    }
}

