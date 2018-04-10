package com.example.quang.projectappcoffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.Order


class DetailHistoryAdapter(var ctx:Context,var list: List<Order>):BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_detail_history,null)
        val name = view.findViewById<TextView>(R.id.txtNameDetailHistory)
        val price = view.findViewById<TextView>(R.id.txtPriceDetailHistory)
        val amount = view.findViewById<TextView>(R.id.txtAmountDetailHistory)

        name.text = list[position].name
        price.text = list[position].price
        amount.text = list[position].amount
        return view
    }

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }
}