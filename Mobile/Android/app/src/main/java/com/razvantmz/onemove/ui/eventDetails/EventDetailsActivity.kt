package com.razvantmz.onemove.ui.eventDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.github.irshulx.Editor
import com.razvantmz.onemove.R
import com.razvantmz.onemove.adapters.addPrice.PriceEntryListAdapter
import com.razvantmz.onemove.adapters.addSchedule.ScheduleListAdapter
import com.razvantmz.onemove.core.extensions.getDay
import com.razvantmz.onemove.core.extensions.getMonthName
import com.razvantmz.onemove.core.helpers.HtmlTagHandler
import com.razvantmz.onemove.core.utils.toDayMonthFormat
import com.razvantmz.onemove.core.utils.toHourMinFormat
import com.razvantmz.onemove.databinding.ActivityEventDetailsBinding
import com.razvantmz.onemove.models.event.Schedule
import com.razvantmz.onemove.models.price.PriceEntry
import com.razvantmz.onemove.ui.base.BaseActivity
import kotlinx.android.synthetic.main.cell_select_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class EventDetailsActivity : BaseActivity<ActivityEventDetailsBinding, EventDetailsViewModel>() {

    private val scheduleAdapter = ScheduleListAdapter(arrayListOf<Schedule>(), false)
    private lateinit var feesAdapter: PriceEntryListAdapter

    companion object {
        const val EVENT_ID = "eventId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        viewModel = ViewModelProviders.of(this).get(EventDetailsViewModel::class.java)
        super.onCreate(savedInstanceState)
        feesAdapter =  PriceEntryListAdapter(baseContext, arrayListOf<PriceEntry>(), false)
        setCoverPhoto()
        setHeader()
        setScheduleRecyclerView()
        setFeesRecyclerView()
        setDetails()
        setRules()
        setHeaderButtonsActions()
        setToolbar()
    }

    override fun onResume() {
        super.onResume()
        val eventId = intent.extras?.getString(EVENT_ID)
        viewModel.getEventByIdAsync(eventId)
    }

    private fun setCoverPhoto()
    {
        viewModel.coverImgUrl.observe(this, Observer {
                binding.coverImageIv.load(it)
        })
    }

    private fun setHeader()
    {
        viewModel.title.observe(this, Observer {
            binding.titleTv.text = it
        })

        viewModel.startDate.observe(this, Observer {
            binding.monthTv.text = it.getMonthName().toUpperCase()
            binding.dayTv.text = it.getDay().toString()
            binding.dateIntervalTv.text = "${it.toDayMonthFormat()} - ${viewModel.endDate.value?.toDayMonthFormat()}"
            binding.timeIntervalTv.text = if(viewModel.isAllDay.value!!)
                                            getString(R.string.allDay)
                                        else
                                            "${it.toHourMinFormat()} - ${viewModel.endDate.value?.toHourMinFormat()}"
        })

        viewModel.feesEntry.observe(this, Observer {
            if(it.isEmpty())
            {
                return@Observer
            }
            val standardPrice = it.first()
            binding.pricePreviewTv.text = "${standardPrice.price.toInt()} ${standardPrice.currency.symbol}"
        })
    }

    private fun setDetails()
    {
        viewModel.details.observe(this, Observer {
            val editor = Editor(this, null)
            val mSerializedHtml = editor.getContentAsHTML(it)
            binding.detailsTv.text = Html.fromHtml(mSerializedHtml, null, HtmlTagHandler())
        })
    }

    private fun setRules()
    {
        viewModel.rules.observe(this, Observer {
            val editor = Editor(this, null)
            val mSerializedHtml = editor.getContentAsHTML(it)
            binding.rulesTv.text = Html.fromHtml(mSerializedHtml, null, HtmlTagHandler())        })
    }

    private fun setScheduleRecyclerView()
    {
        binding.scheduleRv.layoutManager = LinearLayoutManager(baseContext)
        binding.scheduleRv.adapter = scheduleAdapter
        viewModel.scheduleEntry.observe(this, Observer {
            scheduleAdapter.entries = it
            scheduleAdapter.notifyDataSetChanged()
        })
    }

    private fun setFeesRecyclerView()
    {
        binding.priceRv.layoutManager = LinearLayoutManager(baseContext)
        binding.priceRv.adapter = feesAdapter
        viewModel.feesEntry.observe(this, Observer {
            feesAdapter.entries = it
            feesAdapter.notifyDataSetChanged()
        })
    }

    private fun setHeaderButtonsActions()
    {
        binding.scheduleBtn.setOnClickListener {
            binding.scrollView.smoothScrollTo(0, binding.scheduleContainer.top)
        }

        binding.priceBtn.setOnClickListener {
            binding.scrollView.smoothScrollTo(0, binding.priceContainer.top)
        }
    }

    fun setToolbar()
    {
        binding.backIv.setOnClickListener {
            finish()
        }
    }
}
