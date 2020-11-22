package com.razvantmz.onemove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.models.CompetitionModel
import com.razvantmz.onemove.models.Setter

class SetterSpinnerAdapter(var itemSource: List<Setter>) : BaseAdapter() {
    class SetterSpinnerViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup?, id:Int) : RecyclerView.ViewHolder(layoutInflater.inflate(
       id, parent, false)) {

        private var textView: TextView = itemView.findViewById(
            R.id.tv_spinner
        )

        fun bind(item: Setter)
        {
            textView.text = item.name
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = SetterSpinnerAdapter.SetterSpinnerViewHolder(
            LayoutInflater.from(parent?.context),
            parent!!,  R.layout.view_custom_spinner
        )
        viewHolder.bind(itemSource[position])

        return viewHolder.itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = SetterSpinnerAdapter.SetterSpinnerViewHolder(
            LayoutInflater.from(parent?.context), parent!!,
            R.layout.view_custom_spinner_dropdown
        )
        viewHolder.bind(itemSource[position])

        return viewHolder.itemView
    }

    override fun getItem(position: Int): Any {
        return itemSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemSource.size
    }
}