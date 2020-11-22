package com.razvantmz.onemove.adapters

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.managers.SliderLayoutManager
import com.razvantmz.onemove.extensions.px

class ColorListAdapter(var itemSource: List<Int>) : RecyclerView.Adapter<ColorListAdapter.ColorViewHolder>() {

    var callback: SliderLayoutManager.Callback? = null
    val clickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            v?.let { callback?.onItemClicked(it) }
        }
    }

    class ColorViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.cell_color, parent, false))
    {
        fun bind(color: Int)
        {
            val colorShape = GradientDrawable()
            colorShape.cornerRadius = 25.px.toFloat()
            colorShape.color = ColorStateList.valueOf(color)
            colorShape.setStroke(1.px, ContextCompat.getColor(itemView.context, R.color.om_black))
            itemView.background = colorShape
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val viewHolder = ColorViewHolder(LayoutInflater.from(parent.context), parent)
        viewHolder.itemView.setOnClickListener(clickListener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemSource.count()
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(itemSource[position])
    }


}