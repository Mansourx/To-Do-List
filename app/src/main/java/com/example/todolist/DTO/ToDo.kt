package com.example.todolist.DTO

/**
 * Created by Ahmad Mansour on 27,September,2019
 */

class ToDo {
    var id: Long = -1
    var name = ""
    var itemName = ""
    var isCompleted = false
    var createdAt = ""
    var items : MutableList<ToDoItem> = ArrayList()

}