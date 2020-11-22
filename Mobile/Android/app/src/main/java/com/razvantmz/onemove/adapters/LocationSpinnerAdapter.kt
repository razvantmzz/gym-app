package com.razvantmz.onemove.adapters

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.razvantmz.onemove.R
import com.razvantmz.onemove.models.CompetitionModel
import com.razvantmz.onemove.models.RouteLocation
import com.razvantmz.onemove.models.Setter

class LocationSpinnerAdapter() : BaseAdapter() {
    private var itemSource: List<RouteLocation> = emptyList()
    private var filteredItemSource: List<RouteLocation> = emptyList()
    private var routeType:Int = 0

    class LocationSpinnerViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup?, id:Int) : RecyclerView.ViewHolder(layoutInflater.inflate(
       id, parent, false)) {

        private val textView: TextView = itemView.findViewById(R.id.tv_cell_location_spn_name)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_cell_location_spn)

        fun bind(item: RouteLocation)
        {
            textView.text = item.name
            imageView.load(item.image)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = LocationSpinnerAdapter.LocationSpinnerViewHolder(
            LayoutInflater.from(parent?.context),
            parent!!,  R.layout.cell_location_spinner
        )
        viewHolder.bind(filteredItemSource[position])

        return viewHolder.itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder = LocationSpinnerAdapter.LocationSpinnerViewHolder(
            LayoutInflater.from(parent?.context), parent!!,
            R.layout.cell_location_spinner_dropdown
        )
        viewHolder.bind(filteredItemSource[position])

        return viewHolder.itemView
    }

    fun setItemSource(itemSource: List<RouteLocation>)
    {
        this.itemSource = itemSource
        filteredItemSource = filterList(routeType)
        this.notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): RouteLocation
    {
        return filteredItemSource[position]
    }

    fun setType(type:Int)
    {
        routeType = type
        filteredItemSource = filterList(type)
        this.notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any {
        return filteredItemSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredItemSource.size
    }

    private fun filterList(routeType:Int) : List<RouteLocation>
    {
        return itemSource.filter { routeLocation -> routeLocation.type == routeType }
    }
}