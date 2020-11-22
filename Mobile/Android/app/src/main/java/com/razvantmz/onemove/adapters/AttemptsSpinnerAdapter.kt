package com.razvantmz.onemove.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.enums.RouteType
import com.razvantmz.onemove.models.Attempt
import com.razvantmz.onemove.models.CompetitionModel

class AttemptsSpinnerAdapter(val context: Context, val itemSource: List<Attempt>) : BaseAdapter() {

    class AttemptSpinnerViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup?, id:Int) : RecyclerView.ViewHolder(layoutInflater.inflate(
       id, parent, false)) {

        private var textView: TextView = itemView.findViewById(
            R.id.tv_spinner
        )

        fun bind(item: Attempt)
        {
            textView.text = item.name
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = AttemptsSpinnerAdapter.AttemptSpinnerViewHolder(
            LayoutInflater.from(parent?.context),
            parent!!,  R.layout.view_custom_spinner
        )
        viewHolder.bind(getItem(position))

        return viewHolder.itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = AttemptsSpinnerAdapter.AttemptSpinnerViewHolder(
            LayoutInflater.from(parent?.context), parent!!,
            R.layout.view_custom_spinner_dropdown
        )

        viewHolder.bind(getItem(position))
        return viewHolder.itemView
    }

    override fun getItem(position: Int): Attempt {
        return itemSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemSource.size
    }
}