package com.example.todolist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.DTO.ToDo
import kotlinx.android.synthetic.main.activity_dash_board.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.rv_child_dashboard.view.*

/**
 * Created by Ahmad Mansour on 27,September,2019
 */


class DashBoardActivity : AppCompatActivity() {

    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(dashboard_toolbar)
        title = "ToDo App Home"
        rv_dashboard.layoutManager = LinearLayoutManager(this)

        dbHandler = DBHandler(this)
        fab.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Add Your Task")
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

    fun updateToDo (toDo: ToDo) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Update Your Task")
        val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
        val toDoName = view.findViewById<EditText>(R.id.ev_todo)
        toDoName.setText(toDo.name)
        dialog.setView(view)
        dialog.setPositiveButton("Update") { _: DialogInterface, i: Int ->
            if (toDoName.text.isNotEmpty()) {
                toDo.name = toDoName.text.toString()
                dbHandler.updateToDo(toDo)
                refreshList()
            }
        }

        dialog.setNegativeButton("Cancel") { _: DialogInterface, i: Int ->


        }
        dialog.show()
    }
    override fun onResume() {
        refreshList()
        super.onResume()
    }

    private fun refreshList() {
        rv_dashboard.adapter = DashBoardAdapter(this, dbHandler.getToDos())
    }

    class DashBoardAdapter(val activity: DashBoardActivity, val list: MutableList<ToDo>) : RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashboard,parent,false))
        }

        override fun getItemCount(): Int {
            return list.size

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.toDoName.text = list[position].name
            holder.toDoName.setOnClickListener{
                val intent = Intent(activity, ItemActivity::class.java)
                intent.putExtra(INTENT_TODO_ID, list[position].id)
                intent.putExtra(INTENT_TODO_NAME, list[position].name)
                activity.startActivity(intent)
            }
            holder.menu.setOnClickListener{
                val popup = PopupMenu(activity, holder.menu)
                popup.inflate(R.menu.dashboard_child)
                popup.setOnMenuItemClickListener {

                    when(it.itemId) {
                        R.id.menu_delete -> {
                            activity.dbHandler.deleteToDo(list[position].id)
                            activity.refreshList()
                        }
                        R.id.menu_edit -> {
                            activity.updateToDo(list[position])
                        }
                        R.id.menu_mark_as_completed -> {
                            activity.dbHandler.updateToDoItemComplete(list[position].id, true)
                        }
                        R.id.menu_reset -> {
                            activity.dbHandler.updateToDoItemComplete(list[position].id, false)
                        }
                    }

                    true
                }
                popup.show()
            }
        }

        class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
            val toDoName : TextView = view.tv_todo_name
            val menu : ImageView = view.iv_menu
        }
    }
}
