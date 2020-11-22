package com.razvantmz.onemove.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.razvantmz.onemove.R
import com.razvantmz.onemove.databinding.CellSelectItemBinding
import com.razvantmz.onemove.models.SelectItem
import kotlinx.android.synthetic.main.cell_select_item.view.*

class SelectableItemsAdapter(private val interaction: Interaction? = null, val selectedItem:LiveData<SelectItem>, val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    @Suppress("PrivatePropertyName")
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SelectItem>() {
        override fun areItemsTheSame(oldItem: SelectItem, newItem: SelectItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SelectItem, newItem: SelectItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectItemViewHolder(CellSelectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), interaction, lifecycleOwner, selectedItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelectItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<SelectItem>) {
        differ.submitList(list)
    }

    class SelectItemViewHolder (val binding: CellSelectItemBinding, private val interaction: Interaction?, private val lifecycleOwner: LifecycleOwner, private val selectedItem:LiveData<SelectItem>) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SelectItem) = with(itemView) {
            binding.text.text = item.name
            selectedItem.observe(lifecycleOwner, Observer {
                itemView.checkImage.imageTintList = ColorStateList.valueOf(
                    if(item.id == selectedItem.value?.id)
                        itemView.context.getColor(R.color.selected_item)
                    else itemView.context.getColor(R.color.om_sec_background)
                )
            })

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: SelectItem)
    }
}