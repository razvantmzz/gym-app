package com.razvantmz.onemove.adapters.addPrice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.razvantmz.onemove.core.extensions.*
import com.razvantmz.onemove.core.utils.toScheduleFormat
import com.razvantmz.onemove.databinding.CellPriceEntryBinding
import com.razvantmz.onemove.extensions.setVisibilityAnimated
import com.razvantmz.onemove.models.price.PriceEntry
import com.razvantmz.onemove.ui.base.CoreApplication
import kotlin.collections.ArrayList

class PriceEntryListAdapter(var context: Context, var entries: ArrayList<PriceEntry>, var isEditingEnabled:Boolean = true) : RecyclerView.Adapter<PriceEntryListAdapter.PriceEntryListViewHolder>() {

    init {
        if(entries.isEmpty())
        {
            entries.add(getStandardPriceEntry(context))
        }
    }

    class PriceEntryListViewHolder(val binding: CellPriceEntryBinding) : RecyclerView.ViewHolder(binding.root)
    {
        var isDescriptionListenerAdded:Boolean = false

        fun bind(entry: PriceEntry, position: Int)
        {

            binding.titleTv.setTitleOrHint(entry.title)
            binding.currencyTv.text = entry.currency.symbol
            binding.priceTv.setPriceOrHint(binding.root.context, entry.price)
            binding.dateRangeTv.text = "${entry.startDate.toScheduleFormat()} - ${entry.endDate.toScheduleFormat()}"

            if(position == 0)
            {
                binding.dateRangeTv.visibility = View.GONE
            }

            binding.deleteImageView.setDeleteVisibility(entry, adapterPosition)
            binding.setDecoratorColor(itemView.context, entry)

            binding.dateRangeTv.setOnClickListener {
               CoreApplication.currentActivity?.fragmentManager?.showDateRangePickerDialog(
                   MaterialPickerOnPositiveButtonClickListener {
                       val startDate = it.first?.datefromMilli()
                       val endDate = it.second?.datefromMilli()

                       if (startDate != null)
                       {
                           entry.startDate = startDate
                       }
                       if(endDate != null)
                       {
                           entry.endDate = endDate
                       }
                       binding.dateRangeTv.text = "${startDate?.toScheduleFormat()} - ${endDate?.toScheduleFormat()}"
                   })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceEntryListViewHolder {
        val viewHolderBinding = CellPriceEntryBinding.inflate(LayoutInflater.from(parent.context))
        return PriceEntryListViewHolder(viewHolderBinding);
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: PriceEntryListViewHolder, position: Int) {
        val entry = entries[holder.adapterPosition]
        holder.bind(entry, position)

        holder.binding.priceTv.doOnTextChanged { text, start, count, after ->
            if(!text.isNullOrEmpty() && !text.isNullOrBlank())
            {
                entry.price = text.toString().toFloat()
            }
            else
            {
                entry.price = -1f
            }
            addEntryIfNeeded(entry.price, holder.adapterPosition)
            holder.binding.setDecoratorColor(holder.itemView.context, entry)
            holder.binding.deleteImageView.setDeleteVisibility(entry, holder.adapterPosition)
        }

        holder.binding.titleTv.doAfterTextChanged {
            entry.title = it.toString()
        }

        holder.binding.deleteImageView.setOnClickListener {
            removeItem(holder.adapterPosition)
        }

        if(entries.size - 1 == position)
        {
            holder.binding.deleteImageView.visibility = View.INVISIBLE
        }

        setDecorator(holder, position)
        if(!isEditingEnabled)
        {
            disableEditing(holder)
        }
    }

    private fun disableEditing(holder:PriceEntryListViewHolder)
    {
        holder.binding.titleTv.isEnabled = false
        holder.binding.dateRangeTv.isClickable = false
        holder.binding.dateRangeTv.isFocusable = false
        holder.binding.priceTv.isEnabled = false
        holder.binding.deleteImageView.visibility = View.INVISIBLE
    }

    private fun addEntryIfNeeded(price:Float, position: Int)
    {
        if(price == -1f && entries.filter { it -> it.price == -1f }.size >= 2)
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
            if(entries.last().price != -1f && entries[position].price != -1f)
            {
                val entry = getNextScheduleEntry(entries[position])
                entries.add(entry)
                notifyItemInserted(position + 1)
                notifyItemRangeInserted(position + 1, entries.size)
            }
        }
    }

    private fun removeItem(position: Int)
    {
        entries.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position , entries.size)
    }

    private fun setDecorator(viewHolder: PriceEntryListViewHolder, position: Int)
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