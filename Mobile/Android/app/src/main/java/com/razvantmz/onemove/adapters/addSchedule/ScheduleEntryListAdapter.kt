package com.razvantmz.onemove.adapters.addSchedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.extensions.*
import com.razvantmz.onemove.core.utils.toHourMinFormat
import com.razvantmz.onemove.databinding.CellScheduleEntryBinding
import com.razvantmz.onemove.extensions.setVisibilityAnimated
import com.razvantmz.onemove.models.event.ScheduleEntry
import java.util.*
import kotlin.collections.ArrayList

class ScheduleEntryListAdapter(var interaction:ScheduleEntryInteraction, var isEditingEnabled:Boolean) : RecyclerView.Adapter<ScheduleEntryListAdapter.ScheduleEntryListViewHolder>() {

    interface ScheduleEntryInteraction
    {
        fun onScheduleEntryAdded(entry:ScheduleEntry?, position:Int)
        fun onScheduleEntryRemoved( position:Int)

    }

    var entries: ArrayList<ScheduleEntry> = arrayListOf()

    class ScheduleEntryListViewHolder(val binding: CellScheduleEntryBinding) : RecyclerView.ViewHolder(binding.root)
    {
        var isDescriptionListenerAdded:Boolean = false

        fun bind(entry: ScheduleEntry)
        {
            binding.scheduleEntryHourIntervalTv.text = entry.startDate.toHourMinFormat() + " - " + entry.endDate.toHourMinFormat()
            binding.scheduleEntryDetailsTv.setText(entry.description)
            binding.deleteImageView.setVisibilityAnimated((entry.description.isNotEmpty() && adapterPosition != 0) || (adapterPosition == 0 && entry.description.isNotEmpty()))

            binding.scheduleEntryHourIntervalTv.setOnClickListener {
                binding.scheduleEntryDetailsTv.context.showTimeRangeDialog()
                {
                    startHour = entry.startDate.getHours()
                    startMinute = entry.startDate.getMinutes()
                    endHour = entry.endDate.getHours()
                    endMinute = entry.endDate.getMinutes()
                    startMessage = binding.root.context.getString(R.string.startTime)
                    onDateSetListener = object : TimeRangePickerBuilder.OnTimeRangeSetListener
                    {
                        override fun onTimeRangeSet(startHour: Int, startMinute: Int, endHour: Int, endMin: Int) {
                            binding.scheduleEntryHourIntervalTv.text = "${startHour.formatHourMin()}:${startMinute.formatHourMin()} - ${endHour.formatHourMin()}:${endMin.formatHourMin()}"

                            entry.startDate =entry.startDate.setHoursMin(startHour, startMinute)
                            entry.endDate = entry.endDate.setHoursMin(endHour, endMin)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleEntryListViewHolder {
        val viewHolderBinding = CellScheduleEntryBinding.inflate(LayoutInflater.from(parent.context))
        return ScheduleEntryListViewHolder(viewHolderBinding);
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: ScheduleEntryListViewHolder, position: Int) {
        holder.bind(entries[position])
        holder.binding.setDecoratorColor(holder.itemView.context, entries[holder.adapterPosition])
        holder.binding.scheduleEntryDetailsTv.doOnTextChanged { text, start, count, after ->
            entries[holder.adapterPosition].description = text.toString()
            addEntryIfNeeded(entries[holder.adapterPosition].description, holder.adapterPosition)
            holder.binding.deleteImageView.setVisibilityAnimated((holder.adapterPosition != 0 && holder.adapterPosition != entries.size - 1) || (holder.adapterPosition == 0 && entries[holder.adapterPosition].description.isNotEmpty()))
            holder.binding.setDecoratorColor(holder.itemView.context, entries[holder.adapterPosition])
        }

        holder.binding.deleteImageView.setOnClickListener {
            removeItem(holder.adapterPosition)
        }

        setDecorator(holder, position)

        if(!isEditingEnabled)
        {
            disableEditing(holder)
        }
    }

    private fun addEntryIfNeeded(text:String, position: Int)
    {
        if(text.isEmpty() && entries.filter { it -> it.description.isEmpty() }.size >= 2)
        {
//            if(position != entries.size && position != 0)
//            {
//                try {
//                    var lastEmptyView = entries.indexOfLast { it -> it.description.isEmpty() }
//                    removeItem(lastEmptyView)
//                }catch (ex:Exception)
//                {
//                }
//
////                entries.removeAt(entries.size - 1)
////                interaction.onScheduleEntryAdded(null, position)
//            }
        }
        else
        {
            if(entries.last().description.isNotEmpty() && entries[position].description.isNotEmpty())
            {
                val entry = getNextScheduleEntry(entries[position])
                entries.add(entry)
                notifyItemInserted(position + 1)
                notifyItemRangeInserted(position + 1, entries.size)
                interaction.onScheduleEntryAdded(entry, position)
            }
        }
    }

    private fun removeItem(position: Int)
    {
        entries.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position , entries.size)
        interaction.onScheduleEntryRemoved(position)
    }

    private fun disableEditing(holder:ScheduleEntryListViewHolder)
    {
        holder.binding.deleteImageView.visibility = View.INVISIBLE
        holder.binding.scheduleEntryDetailsTv.isEnabled = false
        holder.binding.scheduleEntryHourIntervalTv.isClickable = false
        holder.binding.scheduleEntryHourIntervalTv.isFocusable = false

    }

    private fun setDecorator(viewHolder: ScheduleEntryListViewHolder, position: Int)
    {
        val onDrawListener = ViewTreeObserver.OnDrawListener {
            viewHolder.itemView.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            val height: Int = viewHolder.itemView.measuredHeight
            val params = viewHolder.binding.decoratorVerticalBar.layoutParams
            params.height = if(position == 0)
                height/2
                            else height
            viewHolder.binding.decoratorVerticalBar.layoutParams = params
            viewHolder.isDescriptionListenerAdded = true;
        }
        viewHolder.itemView.viewTreeObserver.addOnDrawListener(onDrawListener)
        viewHolder.itemView.viewTreeObserver.addOnGlobalLayoutListener {
            if (viewHolder.isDescriptionListenerAdded) {
                viewHolder.isDescriptionListenerAdded = false
                viewHolder.itemView.viewTreeObserver.removeOnDrawListener(onDrawListener)
            }
        }
    }
}