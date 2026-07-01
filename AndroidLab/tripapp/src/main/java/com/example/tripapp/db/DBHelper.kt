package com.example.tripapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tripapp.util.Constant

class DBHelper(context: Context)
    : SQLiteOpenHelper(context, Constant.dbName, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table TB_INFO(" +
                "_id integer primary key autoincrement," +
                "email not null," +
                "phone," +
                "photo)")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }

}