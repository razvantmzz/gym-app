package com.razvantmz.onemove.adapters.addSchedule

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.core.extensions.addDays
import com.razvantmz.onemove.core.extensions.showDateDialog
import com.razvantmz.onemove.core.utils.toScheduleFormat
import com.razvantmz.onemove.databinding.CellScheduleBinding
import com.razvantmz.onemove.extensions.setVisibilityAnimated
import com.razvantmz.onemove.models.event.Schedule
import com.razvantmz.onemove.models.event.ScheduleEntry
import java.util.*
import kotlin.collections.ArrayList

class ScheduleListAdapter(var entries:ArrayList<Schedule>, var isEditingEnabled:Boolean = true) : RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder>()
{
    init {
        if(entries.isEmpty())
        {
            entries.add(getEmptySchedule(Calendar.getInstance()))
        }
    }

    class ScheduleListViewHolder(val binding: CellScheduleBinding, var isEditingEnabled: Boolean) : RecyclerView.ViewHolder(binding.root),  ScheduleEntryListAdapter.ScheduleEntryInteraction
    {
        var adapter:ScheduleEntryListAdapter = ScheduleEntryListAdapter(this, isEditingEnabled)

        init {
            binding.scheduleEntryRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.scheduleEntryRecyclerView.adapter = adapter
        }

        fun bind(schedule: Schedule)
        {
            adapter.entries = schedule.entrys
            adapter.notifyDataSetChanged()

            binding.scheduleHeaderTV.text = schedule.date.time.toScheduleFormat()
            if(!isEditingEnabled)
            {
                binding.removeScheduleIv.visibility = View.GONE
                return
            }
            binding.scheduleHeaderTV.setOnClickListener {
                binding.root.context.showDateDialog {
                    onDateSetListener = object : DatePickerDialog.OnDateSetListener
                    {
                        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                            val actualMonth = month + 1
                            val date = Calendar.getInstance()
                            date.set(year, month, dayOfMonth)
                            schedule.date = date
                            binding.scheduleHeaderTV.text = schedule.date.time.toScheduleFormat()

                            for (entry in schedule.entrys)
                            {
                                entry.endDate.set(year, month, dayOfMonth)
                            }
                        }
                    }
                }
            }
        }

        override fun onScheduleEntryAdded(entry: ScheduleEntry?, position: Int) {
            var params = binding.scheduleEntryRecyclerView.layoutParams
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT
            binding.scheduleEntryRecyclerView.layoutParams = params
            binding.scheduleEntryRecyclerView.invalidateOutline()
        }

        override fun onScheduleEntryRemoved(position: Int) {
            val params = binding.scheduleEntryRecyclerView.layoutParams
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT
            binding.scheduleEntryRecyclerView.layoutParams = params
            binding.scheduleEntryRecyclerView.invalidateOutline()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListViewHolder {
        val viewBinding = CellScheduleBinding.inflate(LayoutInflater.from(parent.context))
        val viewHolder = ScheduleListViewHolder(viewBinding, isEditingEnabled)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: ScheduleListViewHolder, position: Int) {
        holder.bind(entries[position])
        holder.binding.removeScheduleIv.setVisibilityAnimated(position == entries.size - 1 && position != 0)

        holder.binding.removeScheduleIv.setOnClickListener {
            entries.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeRemoved(position - 1, entries.size)
        }
        if(!isEditingEnabled)
        {
            holder.binding.removeScheduleIv.visibility = View.GONE
            return
        }
    }

    fun addNextEntry()
    {
        val lastEntryDate = entries.last().date
        val schedule = getEmptySchedule(lastEntryDate.addDays(1))
        entries.add(schedule)
        notifyDataSetChanged()
    }
}