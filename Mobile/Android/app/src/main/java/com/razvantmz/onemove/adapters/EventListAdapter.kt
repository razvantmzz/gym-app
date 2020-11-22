package com.razvantmz.onemove.adapters

import android.R.string
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.extensions.*
import com.razvantmz.onemove.core.utils.fromISOString
import com.razvantmz.onemove.core.utils.toHourMinFormat
import com.razvantmz.onemove.databinding.CellEventBinding
import com.razvantmz.onemove.extensions.dp
import com.razvantmz.onemove.models.postModels.EventPost
import kotlinx.android.synthetic.main.cell_select_item.view.*
import java.time.Instant


class EventListAdapter(var itemSource: ArrayList<EventPost>, var interaction:Interaction) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>()
{
    interface Interaction
    {
        fun onItemClicked(item:EventPost)
    }
    class EventViewHolder(var binding: CellEventBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: EventPost)
        {

            binding.eventIv.load(item.coverImgUrl)
            {
                transformations(
                    RoundedCornersTransformation(15f, 15f, 15f, 15f)
                )
            }

            binding.titleTv.text = item.title
            val startDate = item.startDateIso.fromISOString()
            val endDate = item.endDateIso.fromISOString()
            binding.monthTv.text = startDate.getMonthName().toUpperCase()
            binding.dayTv.text = startDate.getDay().toString()
            binding.eventTimeTv.text = "${startDate.toHourMinFormat()} - ${endDate.toHourMinFormat()}"

            if(item.isAllDay)
            {
                binding.eventTimeTv.text = itemView.context.getString(R.string.allDay)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CellEventBinding.inflate(LayoutInflater.from(parent.context))
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemSource.count()
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(itemSource[position])
        holder.itemView.setOnClickListener {
                interaction.onItemClicked(itemSource[position])
        }
    }
}