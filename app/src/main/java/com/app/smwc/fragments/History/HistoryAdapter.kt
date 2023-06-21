package com.app.smwc.fragments.History

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.smwc.Common.Constant
import com.app.smwc.Common.HELPER
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.ItemStickyDateBinding
import com.app.smwc.databinding.StoreMatchesItemLayoutBinding
import com.app.smwc.fragments.Home.OrdersType

class HistoryAdapter(
    private val act: Activity,
    private var orderArrayList: List<OrdersType>,
    private var listClickListener: ListClickListener
) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            Constant.IS_DATE -> {
                val binding: ItemStickyDateBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(act),
                    R.layout.item_sticky_date,
                    viewGroup,
                    false
                )
                return StickyHeader(binding)
            }

            Constant.IS_ITEM -> {
                val binding: StoreMatchesItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(act),
                    R.layout.store_matches_item_layout,
                    viewGroup,
                    false
                )
                return TopViewHolder(binding)

            }

            else -> {
                val binding: StoreMatchesItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.store_matches_item_layout,
                    viewGroup,
                    false
                )
                return TopViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = orderArrayList[position]
        when (holder.itemViewType) {
            Constant.IS_DATE -> {
                (holder as StickyHeader).binding.dateTxt.text =
                    if (model.date.isNotEmpty()) HELPER.formatDate(model.date) else ""
            }

            Constant.IS_ITEM -> {
                (holder as TopViewHolder).binding.symbol.text =
                    model.order!!.currency.toString().trim()
                holder.binding.totalProduct.text = model.order!!.totalAmount.toString().trim()
                holder.binding.totalProduct.text = model.order!!.itemPickupUp.toString().trim()
                holder.binding.title.text = model.order!!.companyTitle.toString().trim()
                holder.binding.location.text = model.order!!.address.toString().trim()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return orderArrayList[position].type
    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }

    inner class TopViewHolder(itemView: StoreMatchesItemLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: StoreMatchesItemLayoutBinding

        init {
            binding = itemView
        }
    }

    inner class StickyHeader(itemView: ItemStickyDateBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemStickyDateBinding

        init {
            binding = itemView
        }
    }
}