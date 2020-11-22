package com.razvantmz.onemove.adapters

import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import coil.api.load
import coil.transform.CircleCropTransformation
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.utils.toSimpleDisplayFormat
import com.razvantmz.onemove.databinding.CellRoutesBinding
import com.razvantmz.onemove.extensions.px
import com.razvantmz.onemove.models.RouteModel
import kotlinx.android.synthetic.main.activity_add_problem.view.*
import kotlinx.android.synthetic.main.cell_route_details.view.*
import java.text.MessageFormat
import java.text.SimpleDateFormat

class RouteAsyncAdapter(var mock:Boolean = true, private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    var mock:Boolean = false

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RouteModel>() {

        override fun areItemsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            if (mock)
            {
                return false
            }
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            if (mock)
            {
                return false
            }
            return oldItem.triesCount == newItem.triesCount || oldItem.isFavorite == newItem.isFavorite
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RouteModelViewHolder(
            CellRoutesBinding.inflate(LayoutInflater.from(parent.context)),interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RouteModelViewHolder -> {
                if (mock)
                {
                    holder.bind(null)
                    return
                }
                holder.binding.shimmerEffect.visibility = View.GONE
                holder.binding.cellContainer.visibility = View.VISIBLE
                holder.bind(differ.currentList.get(position))
            }
        }
    }


    override fun getItemCount(): Int {
        if(mock)
        {
            return 10
        }
        return differ.currentList.size
    }

    fun submitList(list: List<RouteModel>) {
        mock = false
        differ.submitList(list)
    }

    class RouteModelViewHolder constructor(var binding: CellRoutesBinding, private val interaction: Interaction? ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(route: RouteModel?) = with(itemView) {
            route?:return@with
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, route)
            }

            itemView.setOnLongClickListener {
                interaction?:return@setOnLongClickListener false
                interaction.onLongItemClick(adapterPosition, route)
            }

            binding.btnCellRoutesFav.setOnCheckedChangeListener(null)
            binding.btnCellRoutesFav.setOnClickListener {
                interaction?.onFavoriteStatusChanged(route, (it as ToggleButton).isChecked)
            }

            binding.tvCellRouteIndex.setText(route.index.toString())
            binding.tvCellRoutesName.setText(route.name)
            binding.tvCellRoutesSetter.text = itemView.context.getString(R.string.set_by_name, route.setter.name)
            binding.tvCellRoutesDate.text = route.date.toSimpleDisplayFormat()
            binding.tvCellRoutesLikes.text = itemView.context.getString(R.string.nr_likes, route.likesCount)
            binding.tvCellRoutesGrade.text = route.grade

            val fmt: String = itemView.context.resources.getText(R.string.numberOfTries).toString()
            binding.tvCellRoutesNumberTries.text = MessageFormat.format(fmt, route.triesCount)
            binding.ivCellRouteHolds.load(route.previewImageUrl)
            {
                transformations(CircleCropTransformation())
            }
            binding.ivCellRoutesLocation.load(route.locationUrl)
            binding.btnCellRoutesFav.isSelected = route.isFavorite
            binding.btnCellRoutesFav.isChecked = route.isFavorite

            val shape = GradientDrawable()
            shape.cornerRadius = 25.px.toFloat()
            shape.setStroke(3.px, route.holdsColor)
            binding.vCellRouteImageContainer.background = shape
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RouteModel)
        fun onLongItemClick(position: Int, item:RouteModel): Boolean
        fun onFavoriteStatusChanged(item: RouteModel, isFavorite:Boolean)
    }
}