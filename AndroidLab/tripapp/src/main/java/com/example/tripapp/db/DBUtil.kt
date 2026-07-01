package com.example.tripapp.db

import android.content.Context
import android.database.Cursor
import com.example.tripapp.util.Constant

// DBMS 를 위해 앱 곳곳에서 호출할 함수. 일종의 DAO

fun insertInfo(context: Context, email:String, phone:String?, photo : String?) : Boolean {
    try {
        val db = DBHelper(context).writableDatabase
        db.execSQL("insert into TB_INFO" + "(email, phone, photo) values (?,?,?)",
            arrayOf(email, phone, photo))
        db.close()
        return true
    }catch (e : Exception){
        e.printStackTrace()
        return false
    }
}

fun selectInfo(context: Context) : Cursor?{
    try {
        val db = DBHelper(context).readableDatabase
        val result = db.rawQuery("select * from TB_INFO order by _id DESC limit 1", null)
        return result
    }catch (e: Exception){
        e.printStackTrace()
        return null
    }
}