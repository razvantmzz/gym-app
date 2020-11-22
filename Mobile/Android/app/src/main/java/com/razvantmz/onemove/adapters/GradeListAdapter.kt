package com.razvantmz.onemove.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.managers.SliderLayoutManager
import com.razvantmz.onemove.extensions.px
import com.razvantmz.onemove.models.Grade

class GradeListAdapter(var itemSource: List<Grade>) : RecyclerView.Adapter<GradeListAdapter.GradeViewHolder>() {

    var callback: SliderLayoutManager.Callback? = null
    val clickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            v?.let { callback?.onItemClicked(it) }
        }
    }

    class GradeViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    {
        val gradeTv: TextView = itemView.findViewById(R.id.tv_cell_grade_text)
        val colorView: View = itemView.findViewById(R.id.v_cell_grade_color)

        fun bind(grade: Grade) {
            gradeTv.text = grade.grade

            val colorShape = GradientDrawable()
            colorShape.cornerRadius = 25.px.toFloat()
            colorShape.color = ColorStateList.valueOf(grade.color)
            colorShape.setStroke(1.px, Color.BLACK)
            colorView.background = colorShape
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cell_grade, parent, false)
        val viewHolder =  GradeViewHolder(view)
        viewHolder.itemView.setOnClickListener(clickListener)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return itemSource.count()
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        holder.bind(itemSource[position])
    }
}