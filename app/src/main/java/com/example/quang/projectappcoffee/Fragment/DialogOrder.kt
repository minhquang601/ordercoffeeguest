package com.example.quang.projectappcoffee.Fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.DBHandler
import com.example.quang.projectappcoffee.data.MenuApp
import com.example.quang.projectappcoffee.data.Order
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.user_dialog.*
import java.util.regex.Pattern

/**
 * Created by quang on 1/7/18.
 */
class DialogOrder : DialogFragment() {
    private lateinit var db: DBHandler
    lateinit var order: MenuApp
    lateinit var id: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_order, container, false)
        isCancelable = false
        db = DBHandler(activity)
        order = arguments.getSerializable("menu") as MenuApp
        val edtAmount = view?.findViewById(R.id.edt_Amount_Order) as EditText
        val btnOrder = view.findViewById<Button>(R.id.btn_Order_Order)
        val btnCancel = view.findViewById<Button>(R.id.btn_Cancel_Order)


        btnCancel?.setOnClickListener {
            this.dismiss()
        }
        btnOrder?.setOnClickListener {
            if (TextUtils.isEmpty(edtAmount.text.toString())) {
                edtAmount.error = "Please enter amount !!"
                return@setOnClickListener
            } else {
                val check = edtAmount.text.toString().split(".")
                if (check.size == 1) {
                    if (edtAmount.text.toString().toInt() != 0) {
                        if (db.checkNameItem(order.name) == 1){
                            db.updateAmount(order.name,edtAmount.text.toString(),order.price)
                        }else{
                            db.insertMenu(Order("", order.name, order.price, edtAmount.text.toString()))
                            activity.fab.increase()
                        }
                        this.dismiss()
                    } else {
                        Toast.makeText(activity, "Please enter number > 0", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Please don't enter " + "\".\"", Toast.LENGTH_SHORT).show()
                }
            }

        }

        return view
    }


}