package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,

) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY ,"
        +"$COL_PHONE TEXT)") //create table with column ID and Phone
        db?.execSQL(CREATE_TABLE) // run query to create table
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
    fun insertData(phone: String){
        val db = writableDatabase
        val value = ContentValues()
        value.put(COL_PHONE, phone)
        db.insert(TABLE_NAME,null,value)
    }
    fun viewPhone():List<String>{
        var list : ArrayList<String> = ArrayList() //create array list to store data from database
        var query = "SELECT * FROM $TABLE_NAME" // query to get data from database
        var db = readableDatabase //create readable database to read data from database
        var cursor : Cursor? = null //create cursor(index) to move through database
        try {
            cursor = db.rawQuery(query,null) // run query without condition
        }catch (e: SQLiteException){
         db.execSQL(query)
            return list
        }
       var phone : String
       if (cursor.moveToNext())
       {
           do {
               phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE))//get data from database and store in phone
               list.add(phone) //add phone to list

           }while (cursor.moveToNext()) //move to next row
       }
        return list
    }
    fun update(phone: String,old_phone:String){
        var db = writableDatabase //create writable database to write new phone to database
        var up = "UPDATE $TABLE_NAME SET $COL_PHONE = '$phone' WHERE $COL_PHONE = '$old_phone'"
        db.execSQL(up)
        db.close()
    }
    fun delete(phone: String){
        var db = writableDatabase
        var de ="DELETE FROM $TABLE_NAME WHERE $COL_PHONE = '$phone'"
        db.execSQL(de)
        db.close()
    }

    companion object//create companion object to access constant values
     {
        val DB_NAME = "Contact_info" //create database Contact_info
        val TABLE_NAME = "Phone_info" // create table Phone_info
        val COL_ID = "ID" // create column ID
        val COL_PHONE = "Phone" // create column Phone
    }
}