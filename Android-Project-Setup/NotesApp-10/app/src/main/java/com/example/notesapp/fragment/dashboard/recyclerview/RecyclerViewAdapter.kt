package com.example.notesapp.fragment.dashboard.recyclerview

import android.app.Dialog
import android.content.Context
import android.service.autofill.UserData
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.modal.TodoData
import com.google.android.material.snackbar.Snackbar
import org.chromium.base.Callback

class RecylerViewAdapter(val listener: OnClickListener) : RecyclerView.Adapter<com.example.notesapp.fragment.dashboard.recyclerview.RecylerViewAdapter.ViewHolder>(){
    var oldList:MutableList<TodoData> = mutableListOf()
    interface OnClickListener {
        fun onDelete(todo: TodoData)
        fun onUpdate(todo: TodoData): TodoData
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
            //updateItem(it,position,differ.currentList[position])
        }

        /*
        holder.view.setOnLongClickListener {
            removeItem(view = it, position =position, todo = oldList[position])
            return@setOnLongClickListener true
        } */
        holder.view.setOnLongClickListener {
           // removeItem(view = it, position =position, todo = differ.currentList[position])
            return@setOnLongClickListener true
        }
    }

    private fun updateItem(it: View?, position: Int, todo: TodoData) {
        showDialogBox(todo,it,position)
    }

    private fun showDialogBox(todo: TodoData, view: View?, position: Int) {
        /*
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
        //description.text = Editable.Factory.getInstance().newEditable(todo.description)

        // setting name for todo
        var name = dialog.findViewById<EditText>(R.id.editext_job_name)
        //name.text = Editable.Factory.getInstance().newEditable(todo.job)

        var update = dialog.findViewById<Button>(R.id.btn_update)

        update.setOnClickListener {
            if(checkValidate(description,name,it)) {
                /*
                if(name.text.trim()!=todo.job) {
                    todo.job = name.text.trim().toString()
                }
                if(description.text.trim()!= todo.description) {
                    todo.description = description.text.toString()
                } */
                var update =listener.onUpdate(todo)
                oldList[position]=update
                notifyItemChanged(position)
                dialog.dismiss()
            }
        }
        dialog.show() */
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

    private fun removeItem(view: View, position: Int, todo: TodoData) {
        listener.onDelete(todo = todo)
        //oldList.remove(todo)
       // differ.currentList.removeAt(position)
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

    fun setData(newPersonList:MutableList<TodoData>) {
        val diffUtil = MyDiffUtil(oldList,newPersonList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldList = newPersonList
        diffResults.dispatchUpdatesTo(this)
    }

}
