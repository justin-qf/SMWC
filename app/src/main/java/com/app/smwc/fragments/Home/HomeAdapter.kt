package com.app.smwc.fragments.Home

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.StoreMatchesItemLayoutBinding

class HomeAdapter(
    private val act: Activity,
    private var listClickListener: ListClickListener
) :

    RecyclerView.Adapter<HomeAdapter.DataHolder>() {
    private var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val binding: StoreMatchesItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.store_matches_item_layout,
            parent,
            false
        )

        return DataHolder(binding)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        holder.binding.totalProduct.text = "10"
        holder.binding.productTitle.text = "Product"
        holder.binding.title.text = "Angelic Threads"
        holder.binding.location.text = "2414 Leo StreetNew Florence, PA 15944"
    }

    override fun getItemCount(): Int {
        return 10
    }

    class DataHolder(itemView: StoreMatchesItemLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: StoreMatchesItemLayoutBinding

        init {
            binding = itemView
        }
    }
}
