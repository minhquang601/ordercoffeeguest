package com.example.quang.projectappcoffee.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.OrderSubmit
import com.example.quang.projectappcoffee.ui.DetailHistoryScreenActivity


class HistoryAdapter(private val ctx:Context, private val list:MutableList<OrderSubmit>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent?.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(row)
    }

    override fun getItemCount(): Int {
            return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textview.text = list[position].date
        holder.cardview.setOnClickListener {
                val intent = Intent(ctx,DetailHistoryScreenActivity::class.java)
            intent.putExtra("History",list[position])
            ctx.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textview = itemView.findViewById<TextView>(R.id.txt_Date)
        val cardview = itemView.findViewById<CardView>(R.id.cardview_history)
    }
}