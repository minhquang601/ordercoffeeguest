package com.example.quang.projectappcoffee.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.io.SerializablePermission


class OrderSubmit(var order:List<Order>?,var date:String,var phone:String,var status:String,var table:String,var total:String) :Serializable{

    constructor():this(null,"","","","","")

}