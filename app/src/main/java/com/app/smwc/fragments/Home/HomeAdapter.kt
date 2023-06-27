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
    private var orderArrayList: List<Orders>,
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
        val model = orderArrayList[position]
        holder.binding.symbol.text = model.currency.toString().trim()
        holder.binding.totalProduct.text = model.itemPickupUp.toString().trim()
        holder.binding.count.text = model.totalAmount.toString().trim()
        holder.binding.title.text = model.companyTitle.toString().trim()
        holder.binding.location.text = model.address.toString().trim()
    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }

    class DataHolder(itemView: StoreMatchesItemLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: StoreMatchesItemLayoutBinding

        init {
            binding = itemView
        }
    }
}
