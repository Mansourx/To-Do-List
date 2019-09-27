package com.example.todolist

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.DTO.ToDo
import kotlinx.android.synthetic.main.activity_dash_board.*
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by Ahmad Mansour on 27,September,2019
 */


class DashBoardActivity : AppCompatActivity() {

    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(dashboard_toolbar)
        setTitle("DashBoard")
        rv_dashboard.layoutManager = LinearLayoutManager(this)

        dbHandler = DBHandler(this)
        fab.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)
            dialog.setView(view)
            dialog.setPositiveButton("Add") { _: DialogInterface, i: Int ->
                if (toDoName.text.isNotEmpty()) {
                    val toDo = ToDo()
                    toDo.name = toDoName.text.toString()
                    dbHandler.addToDo(toDo)
                    refreshList()
                }
            }

            dialog.setNegativeButton("Cancel") { _: DialogInterface, i: Int ->


            }
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        rv_dashboard.adapter = DashBoardAdapter(this, dbHandler.getToDos())
    }

    class DashBoardAdapter(val context : Context, val list: MutableList<ToDo>) : RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_child_dashboard,parent,false))
        }

        override fun getItemCount(): Int {
            return list.size

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.toDoName.text = list[position].name
        }

        class ViewHolder(v : View): RecyclerView.ViewHolder(v) {
            val toDoName : TextView = v.findViewById(R.id.tv_todo_name)
        }
    }
}
