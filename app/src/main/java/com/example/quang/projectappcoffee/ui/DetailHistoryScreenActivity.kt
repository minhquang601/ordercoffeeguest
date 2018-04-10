package com.example.quang.projectappcoffee.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.example.quang.projectappcoffee.Adapter.DetailHistoryAdapter
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.Order
import com.example.quang.projectappcoffee.data.OrderSubmit
import kotlinx.android.synthetic.main.activity_detail_history_screen.*
import kotlinx.android.synthetic.main.activity_order_screen.*


class DetailHistoryScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history_screen)
        val his = intent.getSerializableExtra("History") as OrderSubmit
        txtDateDetail.text = his.date
        txtTableDetail.text = his.table
        txtTotalDetail.text = his.total
        val list:List<Order> = his.order!!
        val adapter = DetailHistoryAdapter(this,list)
        listDetailHis.adapter = adapter

        setSupportActionBar(toolbar_detailHis)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorStatusBarHistory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
