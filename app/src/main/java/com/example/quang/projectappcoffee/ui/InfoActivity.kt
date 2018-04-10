package com.example.quang.projectappcoffee.ui

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.quang.projectappcoffee.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_order_screen.*
import android.content.Intent
import android.net.Uri


class InfoActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        if (mLocationPermissionGranted) {

            getDeviceLocation()

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    private val TAG = "MapActivity"

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    private val LOCATION_PERMISSION_REQUEST_CODE = 1234



    private var mLocationPermissionGranted = false
    private var mMap: GoogleMap? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(toolbar_info)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorStatusBarInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        getLocationPermission()

        imgPhoneCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + txtPhoneInfo.text.toString()))
            startActivity(intent)
        }

    }


    private fun initMap() {
        Log.d(TAG, "initMap: initializing map")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the devices current location")

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            if (mLocationPermissionGranted) {

                val location = mFusedLocationProviderClient!!.lastLocation
                location.addOnCompleteListener { taskId ->
                    if (taskId.isSuccessful) {
                        Log.d(TAG, "onComplete: Found location!!")

                        moveCamera(
                                LatLng(10.7928337, 106.6960648), 18f, "62 Nguyễn Huy Tự, Đa Kao, Quận 1, Hồ Chí Minh, Việt Nam")

                    } else {
                        Log.d(TAG, "onComplete: Current location is null")
                        Toast.makeText(this@InfoActivity, "Unable to get current location", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        } catch (e: SecurityException) {
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.message)

        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {

        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        val options = MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))


        val marker = mMap!!.addMarker(options)
        marker.showInfoWindow()
    }

    private fun getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permission")
        val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                            COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true

                initMap()

            } else {
                ActivityCompat.requestPermissions(this,
                        permission, LOCATION_PERMISSION_REQUEST_CODE)
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permission, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult: called..")
        mLocationPermissionGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    for (grantResult in grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false
                            return
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted ")
                    mLocationPermissionGranted = true

                    initMap()
                }
            }
        }
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
