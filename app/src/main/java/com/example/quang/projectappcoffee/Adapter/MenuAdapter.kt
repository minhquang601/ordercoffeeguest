package com.example.quang.projectappcoffee.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.quang.projectappcoffee.data.MenuApp
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.RecyclerViewClickListener
import com.nostra13.universalimageloader.core.ImageLoader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.view.*


class MenuAdapter(private var ctx: Context, private val menuList: MutableList<MenuApp>,private val listener:RecyclerViewClickListener) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    lateinit var mListener:RecyclerViewClickListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val row = LayoutInflater.from(parent?.context).inflate(R.layout.test, parent, false)
        this.mListener = listener
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val menu: MenuApp = menuList[position]
        ImageLoader.getInstance().displayImage(menu.imgUrl, holder?.img)
        holder?.name?.text = menu.name
        holder?.price?.text = ctx.getString(R.string.unit_cart) + menu.price

        holder?.btnOrder?.setOnClickListener{
            mListener.onClick(menu)
        }
        holder?.btnDetails?.setOnClickListener {
            mListener.clickDetail(menu)
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val img = itemView.findViewById<ImageView>(R.id.img_Menu)
        val name = itemView.findViewById<TextView>(R.id.name_Menu)
        val price = itemView.findViewById<TextView>(R.id.price_Menu)
        val btnDetails = itemView.findViewById<Button>(R.id.btn_Details_Menu)
        val btnOrder = itemView.findViewById<Button>(R.id.btn_Order_Menu)



    }


}

