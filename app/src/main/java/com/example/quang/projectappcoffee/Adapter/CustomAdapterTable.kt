package com.example.quang.projectappcoffee.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.quang.projectappcoffee.data.Table
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.ui.MenuScreen
import com.nostra13.universalimageloader.core.ImageLoader


class CustomAdapterTable(val context:Context,val tableList:MutableList<Table>) : RecyclerView.Adapter<CustomAdapterTable.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_view_table,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tableList.size
    }


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val table : Table = tableList[position]
        if(table.Status.equals("false")){
            ImageLoader.getInstance().displayImage(table.No_People,holder?.img)
            holder?.cardview?.setBackgroundResource(R.color.color_Table_No_People)
        }else{
            ImageLoader.getInstance().displayImage(table.People,holder?.img)
            holder?.cardview?.setBackgroundResource(R.color.color_Table_People)
        }

        holder?.name?.text = context.getString(R.string.table_number) +" "+ table.Number

        holder?.layout?.setOnClickListener {
            Toast.makeText(context,holder.name.text,Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MenuScreen::class.java)
            intent.putExtra("table",table.Number)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
            val img = itemView.findViewById<ImageView>(R.id.img_Table)
            val name = itemView.findViewById<TextView>(R.id.txtTable)
            val layout = itemView.findViewById<RelativeLayout>(R.id.relative_Table)
            val cardview= itemView.findViewById<CardView>(R.id.cardview_Table)
    }



}