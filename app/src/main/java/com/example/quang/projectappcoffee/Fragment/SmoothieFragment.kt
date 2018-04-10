package com.example.quang.projectappcoffee.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quang.projectappcoffee.Adapter.MenuAdapter

import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.Ultility.UniversalImageLoader
import com.example.quang.projectappcoffee.data.MenuApp
import com.example.quang.projectappcoffee.data.RecyclerViewClickListener
import com.google.firebase.database.*
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.nested_scroll_view_fragment_drinks.*


class SmoothieFragment : Fragment() {
    lateinit var  referenceDrinks :DatabaseReference

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.nested_scroll_view_fragment_drinks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initImageLoader()
        referenceDrinks = FirebaseDatabase.
                getInstance().
                getReference(context.getString(R.string.db_drink))!!
        getMenuListDrinks(getString(R.string.field_smoothie))

    }

    fun initImageLoader() {
        val imageLoader = UniversalImageLoader(activity)
        ImageLoader.getInstance().init(imageLoader.config)
    }
    fun getMenuListDrinks(type: String){
        val listMenu = mutableListOf<MenuApp>()
        referenceDrinks.
                orderByChild(context.getString(R.string.field_type)).
                equalTo(type).
                addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                    }
                    override fun onDataChange(p0: DataSnapshot?) {
                        if (p0!!.exists()) {
                            for (item in p0.children) {
                                val url = item.getValue(MenuApp::class.java)
                                listMenu.add(url!!)
                            }
                           val adapter = MenuAdapter(activity,listMenu , object : RecyclerViewClickListener {
                                override fun onClick(menu: MenuApp) {
                                    val dialogFragment = DialogOrder()
                                    val bundle = Bundle()
                                    bundle.putSerializable("menu",menu)
                                    dialogFragment.arguments = bundle
                                    dialogFragment.show(activity.supportFragmentManager,"Order")
                                }

                               override fun clickDetail(detail: MenuApp) {
                                   val dialogFragment = DialogDetail()
                                   val bundle = Bundle()
                                   bundle.putSerializable("menu",detail)
                                   dialogFragment.arguments = bundle
                                   dialogFragment.show(activity.supportFragmentManager,"Detail")
                               }
                            })
                            val linearLayout = LinearLayoutManager(activity)
                            recyclerView_Drinks.layoutManager = linearLayout
                            recyclerView_Drinks.hasFixedSize()
                            recyclerView_Drinks.adapter = adapter
                        }
                    }
                })

    }


}
