package com.example.quang.projectappcoffee.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_NAME = "project"

class DBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    lateinit var createTable:String
    companion object {
        val TABLE_MENU = "menu"
        val COL_ID = "id"
        val COL_NAME_MENU = "name"
        val COL_PRICE = "price"
        val COL_AMOUNT = "amount"
        val COL_TOTAL = "total"
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTable = "CREATE TABLE " + TABLE_MENU + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME_MENU + " TEXT, " +
                COL_PRICE + " TEXT, " +
                COL_TOTAL + " TEXT, " +
                COL_AMOUNT + " INTEGER);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    fun insertMenu(menu: Order) {
        val db: SQLiteDatabase = this.writableDatabase
        val value = ContentValues()
        value.put(COL_NAME_MENU, menu.name)
        value.put(COL_PRICE, menu.price)
        value.put(COL_TOTAL, menu.price.toInt() * menu.amount.toInt())
        value.put(COL_AMOUNT, menu.amount)
        db.insert(TABLE_MENU, null, value)
    }

    fun getListOrder(): MutableList<Order> {
        val list = mutableListOf<Order>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_MENU, null, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            list.add(Order(cursor.getString(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_NAME_MENU)),
                    cursor.getString(cursor.getColumnIndex(COL_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COL_AMOUNT))
            ))
        }
        cursor.close()
        return list
    }

    fun totalPrice(): Float {
        var total = 0f
        val db = this.readableDatabase
        val cursor = db.query(TABLE_MENU, arrayOf(COL_TOTAL), null, null, null, null, null)
        while (cursor.moveToNext()) {
            total += cursor.getString(cursor.getColumnIndex(COL_TOTAL)).toFloat()
        }
        cursor.close()
        return total
    }

    fun countList(): Int {
        var count: Int = 0
        val db = this.readableDatabase
        val cursor = db.query(TABLE_MENU, null, null, null, null, null, null, null)
        count = cursor.count
        cursor.close()
        return count
    }

    fun editAmount(newAmount: String, id: String, price: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_AMOUNT, newAmount)
        values.put(COL_TOTAL, (newAmount.toInt() * price.toInt()).toString())
        return db.update(TABLE_MENU, values, "$COL_ID=?", arrayOf(id))
    }

    fun deleteOrder(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_MENU, "$COL_ID=?", arrayOf(id))
    }

    fun deletePay(price: String):Int{
        val db = this.writableDatabase
        return db.delete(TABLE_MENU, "$COL_PRICE=?", arrayOf(price))
    }
    fun checkNameItem(name:String):Int{
        val db = this.readableDatabase
        val cursor = db.query(TABLE_MENU, arrayOf(COL_NAME_MENU), "$COL_NAME_MENU=?", arrayOf(name),null,null,null)
        val count :Int
        count = cursor.count
        cursor.close()
        return count
    }
    fun updateAmount(name:String,newAmount:String,price:String){
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(COL_AMOUNT,newAmount)
        value.put(COL_TOTAL,(newAmount.toInt() * price.toInt()).toString())
        db.update(TABLE_MENU,value, "$COL_NAME_MENU=?", arrayOf(name))
    }

}