package com.razvantmz.onemove.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.razvantmz.onemove.R
import com.razvantmz.onemove.extensions.px
import com.razvantmz.onemove.models.RankModel
import java.text.MessageFormat

class RanksListAdapter(var itemSource: ArrayList<RankModel>) : RecyclerView.Adapter<RanksListAdapter.RankViewHolder>()
{
    class RankViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.cell_rank, parent,  false))
    {
        private var userImage: ImageView = itemView.findViewById(R.id.iv_cell_rank_user)
        private var userImageContainer: RelativeLayout = itemView.findViewById(R.id.v_cell_rank_image_container)
        private var name: TextView = itemView.findViewById(R.id.tv_cell_rank_name)
        private var category: TextView = itemView.findViewById(R.id.tv_cell_rank_category)
        private var position: TextView = itemView.findViewById(R.id.tv_cell_rank_position)
        private var points: TextView = itemView.findViewById(R.id.tv_cell_points)

        fun bind(item: RankModel)
        {
            userImage.load(item.userImageUrl)
            {
                transformations(CircleCropTransformation())
            }
            name.text = item.userName
//            val categoryFmt: String = itemView.context.resources.getText(R.string.category).toString()
//            category.text =
//                MessageFormat.format(categoryFmt, item.userCategory)
            val positionFmt: String = itemView.context.resources.getText(R.string.rank_position).toString()
            position.text =
                MessageFormat.format(positionFmt, item.rank)
            points.text = itemView.context.getString(R.string.rank_points, item.points)

            val shape = GradientDrawable()
            shape.cornerRadius = 25.px.toFloat()
            shape.setStroke(3.px, Color.RED)
            userImageContainer.background = shape
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        return RankViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return itemSource.count()
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(itemSource[position])
    }
}