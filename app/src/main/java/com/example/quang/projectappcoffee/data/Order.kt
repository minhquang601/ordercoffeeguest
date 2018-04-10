package com.example.quang.projectappcoffee.data

import java.io.Serializable

/**
 * Created by quang on 1/6/18.
 */
class Order(var id:String, var name:String, var price:String, var amount:String ):Serializable {
    constructor():this("","","","")
}