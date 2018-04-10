package com.example.quang.projectappcoffee.Fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.Ultility.UniversalImageLoader
import com.example.quang.projectappcoffee.data.DBHandler
import com.example.quang.projectappcoffee.data.MenuApp
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.dialog_detail.*

/**
 * Created by quang on 1/8/18.
 */
class DialogDetail:DialogFragment() {
    lateinit var db:DBHandler
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.dialog_detail,container,false)
        isCancelable= false
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initImageLoader()
        val menu = arguments.getSerializable("menu") as MenuApp
        db = DBHandler(activity)
        ImageLoader.getInstance().displayImage(menu.imgUrl, img_Detail)
        name_Detail.text = menu.name
        des_Detail.text = menu.description
        price_Detail.text = menu.price
        btn_Cancel_Detail.setOnClickListener {
            this.dismiss()
        }
    }
    fun initImageLoader() {
        val imageLoader = UniversalImageLoader(activity)
        ImageLoader.getInstance().init(imageLoader.config)
    }
}