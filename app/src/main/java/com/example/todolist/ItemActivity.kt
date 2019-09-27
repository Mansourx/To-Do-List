package com.example.todolist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.DTO.ToDo
import com.example.todolist.DTO.ToDoItem
import kotlinx.android.synthetic.main.activity_item.*

/**
 * Created by Ahmad Mansour on 27,September,2019
 */


class ItemActivity: AppCompatActivity() {

    lateinit var dbHandler: DBHandler
    var todoid: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(item_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent.getStringExtra(INTENT_TODO_NAME)

        dbHandler = DBHandler(this)
        todoid = intent.getLongExtra(INTENT_TODO_ID, -1)

        rv_item.layoutManager = LinearLayoutManager(this)


        fab_item.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)
            dialog.setView(view)
            dialog.setPositiveButton("Add") { _: DialogInterface, i: Int ->
                if (toDoName.text.isNotEmpty()) {
                    val item = ToDoItem()
                    item.itemName = toDoName.text.toString()
                    item.toDoId = todoid
                    item.isCompleted = false
                    dbHandler.addToDoItem(item)
                    refreshList()
                }
            }

            dialog.setNegativeButton("Cancel") { _: DialogInterface, i: Int ->
            }
            dialog.show()

        }
    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }
    private fun refreshList(){
        rv_item.adapter = ItemAdapter(this, dbHandler.getToDoItems(todoid))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }


    class ItemAdapter(val context : Context, val list: MutableList<ToDoItem>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_child_item,parent,false))
        }

        override fun getItemCount(): Int {
            return list.size

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemName.text = list[position].itemName
            holder.itemName.isChecked = list[position].isCompleted
            holder.itemName.setOnClickListener{
                val intent = Intent(context, ItemActivity::class.java)
                intent.putExtra(INTENT_TODO_ID, list[position].id)
                intent.putExtra(INTENT_TODO_NAME, list[position].itemName)
                context.startActivity(intent)
            }
        }

        class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
            val itemName: CheckBox = view.findViewById(R.id.cb_item)
        }
    }
}