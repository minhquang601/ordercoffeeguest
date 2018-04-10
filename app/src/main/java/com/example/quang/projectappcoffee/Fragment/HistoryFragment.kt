package com.example.quang.projectappcoffee.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quang.projectappcoffee.Adapter.HistoryAdapter

import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.OrderSubmit
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*


class HistoryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val refHistory = FirebaseDatabase.getInstance().getReference("History")
        val listHistory = mutableListOf<OrderSubmit>()
        val adapter = HistoryAdapter(activity,listHistory)
        activity.fab.visibility = View.INVISIBLE
        val phoneNumber = arguments.getString("phone")
        if (phoneNumber == "null"){
            textHis.visibility = View.VISIBLE
            textHis.text = "No phone number"
        }else{
            textHis.visibility = View.INVISIBLE
           refHistory.orderByChild("phone").equalTo(phoneNumber).addChildEventListener(object :ChildEventListener{
               override fun onCancelled(p0: DatabaseError?) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

               override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

               override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                   listHistory.clear()
                   val objectHis = p0.getValue(OrderSubmit::class.java)!!
                   listHistory.add(objectHis)
                   adapter.notifyDataSetChanged()
               }

               override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val objectHis = p0.getValue(OrderSubmit::class.java)!!
                   listHistory.add(objectHis)
                   listHistory.reverse()
                   adapter.notifyDataSetChanged()
               }

               override fun onChildRemoved(p0: DataSnapshot?) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

           })
            val layout = LinearLayoutManager(activity)
            list_History.layoutManager = layout
            list_History.hasFixedSize()
            list_History.adapter = adapter


        }

    }

}
