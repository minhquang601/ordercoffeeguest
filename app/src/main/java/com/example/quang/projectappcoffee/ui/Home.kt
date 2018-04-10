package com.example.quang.projectappcoffee.ui

import android.Manifest.permission.CAMERA
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.quang.projectappcoffee.data.DBHandler
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class Home : AppCompatActivity(),ZXingScannerView.ResultHandler {
    private val REQUEST_CAMERA = 1
    private var mZXingScannerView: ZXingScannerView? = null
    var db:DBHandler =DBHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mZXingScannerView = ZXingScannerView(this)
        setContentView(mZXingScannerView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(this, "Permission is granted!", Toast.LENGTH_SHORT).show()

            } else {
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA -> if (grantResults.isNotEmpty()) {
                val camaraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (camaraAccepted) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            displayAlertMessage("You need to allow access for both permissions", DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(arrayOf(CAMERA), REQUEST_CAMERA)
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (mZXingScannerView == null) {
                    mZXingScannerView = ZXingScannerView(this)
                    setContentView(mZXingScannerView)
                }
                mZXingScannerView!!.setResultHandler(this)
                mZXingScannerView!!.startCamera()
            } else {
                requestPermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mZXingScannerView!!.stopCamera()
    }

    private fun displayAlertMessage(message: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    override fun handleResult(result: Result) {
        val scanResult = result.text
        val intent = Intent(this@Home,MenuScreen::class.java)
        intent.putExtra("table",scanResult)
        startActivity(intent)
        finish()
    }
}
