package com.example.quang.projectappcoffee.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION_CODES.KITKAT
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.quang.projectappcoffee.R
import com.example.quang.projectappcoffee.data.DBHandler
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    lateinit var db: DBHandler
     var check:Int =0
    lateinit var i :Intent
    lateinit var i2 :Intent
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = DBHandler(this)
        check =  db.countList()
        i = Intent(this, Home::class.java)
        i2 = Intent(this, MenuScreen::class.java)
        i2.putExtra("table", "")

        if (!isNetworkConnected()) {
            alertMessage()
            btn_reLoad.visibility = View.VISIBLE
        } else {
            btn_reLoad.visibility = View.GONE
           timerTheard()
        }

        btn_reLoad.setOnClickListener {
            if (!isNetworkConnected()) {
                alertMessage()
            }else{
                if (check > 0) {
                    startActivity(i2)
                    finish()
                } else {
                    startActivity(i)
                    finish()
                }

            }
        }
        if (Build.VERSION.SDK_INT >= KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    private fun alertMessage() {
        AlertDialog.Builder(this)
                .setMessage("Device not connected to internet")
                .setPositiveButton("Ok", null)
                .create()
                .show()
    }
    private fun timerTheard(){
        val timer = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    if (check > 0) {
                        startActivity(i2)
                        finish()
                    } else {
                        startActivity(i)
                        finish()
                    }
                }
            }
        }
        timer.start()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}
