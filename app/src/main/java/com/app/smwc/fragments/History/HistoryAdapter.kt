package com.app.smwc.fragments.History

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.smwc.Common.HELPER
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.ItemStickyDateBinding
import com.app.smwc.databinding.StoreMatchesItemLayoutBinding
import com.app.smwc.fragments.Home.OrdersType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val act: Activity,
    private var orderArrayList: List<OrdersType>,
    private var listClickListener: ListClickListener
) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {


                val binding2: ItemStickyDateBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
                    R.layout.item_sticky_date,
                    viewGroup,
                    false
                )
                return StickyHeader(binding2)

            }
            1 -> {
                val binding: StoreMatchesItemLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
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

    fun convertDateToString(date: String?): String? {
        val df: DateFormat = SimpleDateFormat(date)
        val today: Date = Calendar.getInstance()
            .time
        return df.format(today)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> {
                val model = orderArrayList[position]
                (holder as StickyHeader).binding.dateTxt.visibility = View.VISIBLE
                HELPER.print("ORDER_LIST", model.date)
                holder.binding.dateTxt.text = "sachin dfnsdkfnsdjfnsdjklf"
            }
            1 -> {
                val model = orderArrayList[position]
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
        return orderArrayList[position].type;
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
