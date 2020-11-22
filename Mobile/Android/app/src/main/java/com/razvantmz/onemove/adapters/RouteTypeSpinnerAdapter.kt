package com.razvantmz.onemove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.enums.RouteType
import com.razvantmz.onemove.models.CompetitionModel
import com.razvantmz.onemove.models.Setter

class RouteTypeSpinnerAdapter() : BaseAdapter() {
    class SetterSpinnerViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup?, id:Int) : RecyclerView.ViewHolder(layoutInflater.inflate(
       id, parent, false)) {

        private var textView: TextView = itemView.findViewById(
            R.id.tv_spinner
        )

        fun bind(item: RouteType)
        {
            when(item)
            {
                RouteType.BOULDER -> textView.text = itemView.context.getString(R.string.boulder)
                RouteType.LEAD -> textView.text = itemView.context.getString(R.string.lead)
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = RouteTypeSpinnerAdapter.SetterSpinnerViewHolder(
            LayoutInflater.from(parent?.context),
            parent!!,  R.layout.view_custom_spinner
        )
        viewHolder.bind(getItem(position))

        return viewHolder.itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = RouteTypeSpinnerAdapter.SetterSpinnerViewHolder(
            LayoutInflater.from(parent?.context), parent!!,
            R.layout.view_custom_spinner_dropdown
        )
        viewHolder.bind(getItem(position))

        return viewHolder.itemView
    }

    override fun getItem(position: Int): RouteType {
        when(position)
        {
            0 -> return RouteType.BOULDER
            1 -> return RouteType.LEAD
        }
      return RouteType.BOULDER
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return 2
    }
}