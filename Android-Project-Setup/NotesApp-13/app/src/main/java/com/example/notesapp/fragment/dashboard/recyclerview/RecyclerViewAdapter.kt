package com.example.notesapp.fragment.dashboard.recyclerview

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.modal.TodoData
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class RecylerViewAdapter(var context:Context,val listener: OnClickListener) : RecyclerView.Adapter<com.example.notesapp.fragment.dashboard.recyclerview.RecylerViewAdapter.ViewHolder>(){
    var oldList:ArrayList<TodoData> = ArrayList()
    interface OnClickListener {
        fun onDelete(todo: TodoData, position: Int)
        fun onUpdate(todo: TodoData, position: Int,job:String,description:String)
    }
    override fun getItemId(positon: Int): Long {
        return positon.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.text = oldList[position].description
        //holder.description.text = differ.currentList[position].description
        holder.job.text = oldList[position].job
        //holder.job.text = differ.currentList[position].job

        /*
        holder.view.setOnClickListener {
            updateItem(it,position,oldList[position])
        } */
        holder.view.setOnClickListener {
            //showDialogBox(todo =oldList[position],view =it ,position=position)
            updateItem(it,position,todo = oldList[position])
        }

        /*
        holder.view.setOnLongClickListener {
            removeItem(view = it, position =position, todo = oldList[position])
            return@setOnLongClickListener true
        } */
        holder.view.setOnLongClickListener {
            Log.d("TAG","ArrayList Index ${position}")
            removeItem(it,position,oldList[position])
            return@setOnLongClickListener true
        }
    }

    //private fun updateItem(it: View?, position: Int, todo: TodoData) {
        //showDialogBox(todo,it,position)
    //}


    private fun removeItem(view: View, position: Int, todo: TodoData) {
        listener.onDelete(todo = todo,position)

    }
    private fun updateItem(view: View, position: Int, todo: TodoData) {
        //showDialogBox(todo, view, position)
        listener.onUpdate(todo = todo,position,job = "",description = "")
    }

    override fun getItemCount(): Int {
        //return differ.currentList.count()
        return oldList.size
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var view: View = view
        var job: TextView = view.findViewById(R.id.adpater_username)
        var description: TextView = view.findViewById(R.id.adpater_from)
    }

    class MyDiffUtil(var oldList: MutableList<TodoData>,var newList: MutableList<TodoData>) :DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.count()
        }

        override fun getNewListSize(): Int {
            return newList.count()
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition]._id == newList[newItemPosition]._id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition:Int):Boolean {
            return when {
                oldList[oldItemPosition]._id != newList[newItemPosition]._id -> {
                    false
                }
                oldList[oldItemPosition].job != newList[newItemPosition].job -> {
                    false
                }
                oldList[oldItemPosition].description != newList[newItemPosition].description -> {
                    false
                }
                else -> true
            }
        }
    }

    fun setData(newPersonList:ArrayList<TodoData>,updateTodo:TodoData ?= null) {
        val diffUtil = MyDiffUtil(oldList,newPersonList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList.clear()
        oldList.addAll(newPersonList)
        diffResults.dispatchUpdatesTo(this)
    }
    private fun showDialogBox(todo: TodoData, view: View?, position: Int) {
        var dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_update_todo)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = 1200
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp

        //setting description value
        var description = dialog.findViewById<EditText>(R.id.editext_description)
        description.text = Editable.Factory.getInstance().newEditable(todo.description)

        // setting name for todo
        var name = dialog.findViewById<EditText>(R.id.editext_job_name)
        name.text = Editable.Factory.getInstance().newEditable(todo.job)

        var update = dialog.findViewById<Button>(R.id.btn_update)

        update.setOnClickListener {
            if(checkValidate(description,name,it)) {
                if(name.text.trim()!=todo.job && description.text.trim()!= todo.description) {
                    listener.onUpdate(todo = todo,position,job = name.text.trim().toString(),description = description.text.toString())
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
    private fun checkValidate(description: EditText, name: EditText, view: View):Boolean {
        if(name.text.trim().isEmpty()) {
            Snackbar
                .make(view,"Please fill job , it should not be blank or empty", Snackbar.LENGTH_LONG).show()
            return false
        }
        if(description.text.trim().isEmpty()) {
            Snackbar
                .make(view,"Please fill description, it should not be blank or empty", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

}

