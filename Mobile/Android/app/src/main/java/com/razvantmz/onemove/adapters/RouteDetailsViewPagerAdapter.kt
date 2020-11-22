package com.razvantmz.onemove.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import coil.api.load
import coil.transform.CircleCropTransformation
import com.razvantmz.onemove.R
import com.razvantmz.onemove.core.utils.toSimpleDisplayFormat
import com.razvantmz.onemove.databinding.CellRouteDetailsBinding
import com.razvantmz.onemove.models.RouteModel
import java.text.MessageFormat
import java.util.*

class RouteDetailsViewPagerAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RouteModel>() {

        override fun areItemsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ReouteDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cell_route_details,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReouteDetailsViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<RouteModel>) {
        differ.submitList(list)
    }

    fun getItemPosition(item:RouteModel) : Int
    {
        return differ.currentList.indexOf(item)
    }

    fun getItemByPosition(position: Int): RouteModel
    {
        return differ.currentList[position]
    }

    class ReouteDetailsViewHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        private val binding: CellRouteDetailsBinding = CellRouteDetailsBinding.bind(itemView)

        fun bind(route: RouteModel) = with(itemView) {

            binding.nameTextView.text = route.name
            binding.routeImageView.load(route.imageUrl)
            binding.locationImageView.load(route.locationUrl)

            binding.locationTextView.text = route.location.toString()
            binding.gradeTextView.text = route.grade
            val fmt: String = itemView.context.getText(R.string.numberOfTries).toString()
            binding.sendStatusTextView.text = MessageFormat.format(fmt, route.triesCount)
            binding.favoriteBtn.isChecked = route.isFavorite
            binding.favoriteBtn.isSelected = route.isFavorite
            binding.nrLikesTextView.text = itemView.context.getString(R.string.nr_likes, route.likesCount)
            binding.setterImageVew.load(route.setter.imageUrl)
            {
                transformations(CircleCropTransformation())
            }
            binding.setterTextView.text = itemView.context.getString(R.string.set_by_name, route.setter.name)
            binding.dateTextView.text = route.date.toSimpleDisplayFormat()

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, route)
            }
            binding.favoriteBtn.setOnCheckedChangeListener(null)
            binding.favoriteBtn.setOnClickListener {
                    interaction?.onFavoriteStatusChanged(route, (it as ToggleButton).isChecked)
            }
            binding.sentButton.setOnClickListener {
                interaction?.onMarkAsSentClicked(route)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RouteModel)
        fun onFavoriteStatusChanged(item: RouteModel, isFavorite:Boolean)
        fun onMarkAsSentClicked(item:RouteModel);
    }
}