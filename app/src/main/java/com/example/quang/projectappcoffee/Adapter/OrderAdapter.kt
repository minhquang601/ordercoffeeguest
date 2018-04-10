package com.example.quang.projectappcoffee.Adapter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.quang.projectappcoffee.Fragment.DialogOrder
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.Order
import com.example.quang.projectappcoffee.ui.OrderScreen


class OrderAdapter(val context: OrderScreen, val list: MutableList<Order>)
    : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val order:Order = list[position]

        holder?.id?.text = (position+1).toString()
        holder?.name?.text = order.name
        holder?.price?.text = "$ "+order.price
        holder?.amount?.text = " X "+ order.amount

        holder?.edit?.setOnClickListener {
            context.dialogEdit(order.amount,order.id,order.price)
        }
        holder?.remove?.setOnClickListener {
            context.deteleOrder(order.id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent?.context).inflate(R.layout.item_listview_order,parent,false)
        return ViewHolder(row)
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
            val id = itemView.findViewById<TextView>(R.id.list_order_id)
            val name = itemView.findViewById<TextView>(R.id.list_order_name)
            val amount = itemView.findViewById<TextView>(R.id.list_order_amount)
            val price = itemView.findViewById<TextView>(R.id.list_order_price)
            val edit = itemView.findViewById<ImageView>(R.id.list_order_edit)
            val remove = itemView.findViewById<ImageView>(R.id.list_order_delete)
    }
}