package com.razvantmz.onemove.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.models.CompetitionModel


class RanksSpinnerAdapter(val context: Context, val itemSource: List<CompetitionModel>) : BaseAdapter()
{
    class SpinerViewHolder(inflater: LayoutInflater, parent: ViewGroup, layoutId:Int) : RecyclerView.ViewHolder(inflater.inflate(layoutId, parent, false))
    {
        var textView: TextView = itemView.findViewById(
            R.id.tv_spinner
        )

        fun bind(item: CompetitionModel)
        {
            textView.text = item.name
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = SpinerViewHolder(
            LayoutInflater.from(parent?.context), parent!!,
            R.layout.view_custom_spinner
        )
        viewHolder.bind(itemSource[position])

        return viewHolder.itemView
    }

    override fun getItem(position: Int): Any {
        return itemSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = SpinerViewHolder(
            LayoutInflater.from(parent?.context), parent!!,
            R.layout.view_custom_spinner_dropdown
        )
        viewHolder.bind(itemSource[position])

        return viewHolder.itemView
    }

    override fun getCount(): Int {
        return itemSource.count()
    }
}