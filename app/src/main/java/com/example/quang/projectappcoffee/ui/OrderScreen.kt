package com.example.quang.projectappcoffee.ui

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.quang.projectappcoffee.Adapter.OrderAdapter
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.DBHandler
import com.example.quang.projectappcoffee.data.Order
import com.example.quang.projectappcoffee.data.OrderSubmit
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_order_screen.*
import java.util.*

class OrderScreen : AppCompatActivity() {
    lateinit var valueIntent: Intent
    lateinit var firebaseDB: DatabaseReference
    lateinit var db: DBHandler
    lateinit var adapter: OrderAdapter
    lateinit var list: MutableList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_screen)
        setSupportActionBar(toolbar_order)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorStatusBarOrder)
        toolbar_order.setTitleTextColor(ContextCompat.getColor(this, R.color.colorTitle))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        valueIntent = intent
        db = DBHandler(this)
        var table = intent.getStringExtra("table1")
        val phone = intent.getStringExtra("phone")
        list = db.getListOrder()
        firebaseDB = FirebaseDatabase.getInstance().getReference("Request")
        adapter = OrderAdapter(this, list)
        val layout = LinearLayoutManager(this)
        list_order.layoutManager = layout
        list_order.hasFixedSize()
        list_order.adapter = adapter

        firebaseDB.orderByKey().equalTo(table).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    btnSubmit_Order.text = getString(R.string.order_Order)
                } else {
                    btnSubmit_Order.text = getString(R.string.pay_order)
                }
            }
        })


        total_order.text = db.totalPrice().toString()
        btnSubmit_Order.setOnClickListener {
            if (btnSubmit_Order.text.toString() == getString(R.string.order_Order)) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Order")
                alertDialog.setMessage("Would you like to order these items?")
                alertDialog.setPositiveButton("Ok") { dialog, which ->
                    if (list.size > 0) {
                        firebaseDB.child(table).setValue(OrderSubmit(list, getCurrentDate(), phone, "false", table, total_order.text.toString()))
                        btnSubmit_Order.text = getString(R.string.pay_order)
                    } else {
                        Toast.makeText(this, "You have not selected drinks or food!!", Toast.LENGTH_SHORT).show()
                    }
                }
                alertDialog.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()
            } else {
                val refPay = FirebaseDatabase.getInstance().getReference("Pay")
                val dialog = AlertDialog.Builder(this)
                dialog.setMessage("Do you want to pay?")
                dialog.setPositiveButton("Ok",{ dialog,which->

                    refPay.child(table).setValue(OrderSubmit(list, getCurrentDate(), phone, "true", table, total_order.text.toString()))
                    db.deletePay("12")
                    finish()
                })
                dialog.setNegativeButton("Cancel",{ dialog,which->
                    dialog.dismiss()
                })
                dialog.create().show()
            }

        }

    }


    fun dialogEdit(amount: String, id: String, price: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_order)
        val edtAmount = dialog.findViewById<EditText>(R.id.edt_Amount_Order)
        val btnOrder = dialog.findViewById<Button>(R.id.btn_Order_Order)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_Cancel_Order)

        edtAmount.setText(amount)
        btnOrder.setOnClickListener {
            if (TextUtils.isEmpty(edtAmount.text.toString())) {
                edtAmount.error = "Please enter amount!!"
                return@setOnClickListener
            } else {
                val check = edtAmount.text.toString().split(".")
                if (check.size == 1) {
                    if (edtAmount.text.toString().toInt() != 0) {
                        db.editAmount(edtAmount.text.toString(), id, price)
                        list.clear()
                        list.addAll(db.getListOrder())
                        adapter.notifyDataSetChanged()
                        total_order.text = db.totalPrice().toString()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter number > 0", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please don't enter " + "\".\"", Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    fun deteleOrder(id: String) {
        db.deleteOrder(id)
        list.clear()
        list.addAll(db.getListOrder())
        adapter.notifyDataSetChanged()
        total_order.text = db.totalPrice().toString()
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

    private fun getCurrentDate(): String {
        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR)
        val mounth = date.get(Calendar.MONTH) + 1
        val day = date.get(Calendar.DAY_OF_MONTH)

        return "$mounth/$day/$year"

    }

}
