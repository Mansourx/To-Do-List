package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.todolist.DTO.ToDo
import com.example.todolist.DTO.ToDoItem

/**
 * Created by Ahmad Mansour on 27,September,2019
 */

class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
       val createToDoTable: String = "CREATE TABLE $TABLE_TODO (" +
               "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
               "$COL_CREATED_AT createdAt datetime DEFAULT CURRENT_TIMESTAME," +
               "$COL_NAME varchar" +
               ");"


        val createToDoItemTable: String =  "CREATE TABLE $TABLE_TODO_ITEM (" +
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

    fun addToDoItem (item : ToDoItem) : Boolean{
        val db : SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TODO_ID, item.toDoId)
       // cv.put(COL_ITEM_NAME, item.itemName)

        if (item.isCompleted) {
            cv.put(COL_IS_COMPLETED, 1)
        } else {
            cv.put(COL_IS_COMPLETED, 0)
        }
        val result : Long = db.insert(TABLE_TODO_ITEM, null, cv)
        return result != (-1).toLong()
    }

    fun getToDoItems(todoid : Long) : MutableList<ToDoItem> {
        val result : MutableList<ToDoItem> = ArrayList()

        val db : SQLiteDatabase = readableDatabase
        val queryResult : Cursor = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEM WHERE $COL_TODO_ID=$todoid", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = ToDoItem()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.toDoId = queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID))
                //item.itemName = queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME))
                item.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                item.toDoId = todoid
                Log.i("Item Result >>>", item.itemName)
                result.add(item)
            } while (queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }
}