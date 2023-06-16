package com.app.smwc.fragments.Notification

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.smwc.Interfaces.ListClickListener
import com.app.smwc.R
import com.app.smwc.databinding.NotificationItemLayoutBinding

class NotificationAdapter(
    private val act: Activity,
    private var notificationList: ArrayList<NotificationData>,
    private var listClickListener: ListClickListener
) :

    RecyclerView.Adapter<NotificationAdapter.DataHolder>() {
    private var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val binding: NotificationItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notification_item_layout,
            parent,
            false
        )

        return DataHolder(binding)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        val model = notificationList[position]
        holder.binding.notificationTxt.text = model.notification.toString().trim()
        holder.binding.notificationTypeTxt.text = model.createdDate.toString().trim()
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    class DataHolder(itemView: NotificationItemLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: NotificationItemLayoutBinding

        init {
            binding = itemView
        }
    }
}
