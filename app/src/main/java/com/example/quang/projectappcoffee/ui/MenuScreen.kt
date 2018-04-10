package com.example.quang.projectappcoffee.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quang.projectappcoffee.Fragment.DrinksFragment
import com.example.quang.projectappcoffee.Fragment.FoodFragment
import com.example.quang.projectappcoffee.Fragment.HistoryFragment
import com.example.quang.projectappcoffee.R.id.*
import kotlinx.android.synthetic.main.activity_menu.*
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.DBHandler
import com.google.firebase.auth.FirebaseAuth


class MenuScreen : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DBHandler
    private lateinit var numTable: String
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        db = DBHandler(this)
        mAuth = FirebaseAuth.getInstance()

        val sharedPre = getSharedPreferences("Table",Context.MODE_PRIVATE)

        numTable = if (intent.getStringExtra("table") == "") {
            sharedPre.getString("number","")
        } else {
            val editor = sharedPre.edit()
            editor.putString("number",intent.getStringExtra("table"))
            editor.apply()

            intent.getStringExtra("table")
        }

        Toast.makeText(this@MenuScreen, numTable, Toast.LENGTH_SHORT).show()
        bottomBar.setOnTabSelectListener { tabId ->
            when (tabId) {
                tab_drinks -> {
                    val drinks = DrinksFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.contentContainer, drinks).commit()

                }
                tab_food -> {
                    val food = FoodFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.contentContainer, food).commit()
                }
                tab_history -> {
                    val his = HistoryFragment()
                    val bundle = Bundle()
                    val phoneNumber = mAuth.currentUser?.phoneNumber.toString()
                    bundle.putString("phone", phoneNumber)
                    his.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.contentContainer, his).commit()
                }
            }
        }
        fab.setOnClickListener {
            val intent = Intent(this, OrderScreen::class.java)
            intent.putExtra("table1", numTable)
            intent.putExtra("phone", mAuth.currentUser?.phoneNumber.toString())
            startActivity(intent)
        }



    }

    override fun onResume() {
        super.onResume()
        fab.count = db.countList()
    }


}
