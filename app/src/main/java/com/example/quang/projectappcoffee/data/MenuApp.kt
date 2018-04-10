package com.example.quang.projectappcoffee.data

import java.io.Serializable

/**
 * Created by quang on 12/26/17.
 */
class MenuApp(var name:String, var price:String,
              var imgUrl:String, var description:String,
              var type:String, var status:String) :
            Serializable {
    constructor():this("","","","","","")
}