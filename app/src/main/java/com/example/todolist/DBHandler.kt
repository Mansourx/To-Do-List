package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolist.DTO.ToDo

/**
 * Created by Ahmad Mansour on 27,September,2019
 */

class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
       val createToDoTable: String = "CREATE TABLE ToDo (" +
               "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
               "$COL_CREATED_AT createdAt datetime DEFAULT CURRENT_TIMESTAME," +
               "$COL_NAME varchar" +
               ");"


        val createToDoItemTable: String =  "CREATE TABLE toDoListItem (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_CREATED_AT datetime," +
                "$COL_TODO_ID integer," +
                "$COL_IS_COMPLETED integer" +
                "$COL_ITEM_NAME varchar" +
                ");"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addToDo(toDo : ToDo): Boolean {
        val db : SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME,toDo.name)
        val result : Long = db.insert(TABLE_TODO, null, cv)
        return result != (-1).toLong()
    }

    fun getToDos() : MutableList<ToDo> {
        val result : MutableList<ToDo> = ArrayList()
        val db : SQLiteDatabase = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_TODO", null)
        if(queryResult.moveToFirst()) {
            do {
                val todo = ToDo()
                todo.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                result.add(todo)
            } while (queryResult.moveToNext())

        }

        queryResult.close()

        return result
    }

}