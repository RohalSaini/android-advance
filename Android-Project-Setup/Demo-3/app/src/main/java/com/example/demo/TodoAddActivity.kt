package com.example.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.demo.databinding.ActivityAddTodoBinding
import com.example.demo.modal.Todo
import com.example.demo.modal.User

class TodoAddActivity : AppCompatActivity() {
    lateinit var user: User
    lateinit var binding:ActivityAddTodoBinding
    private val TAG = "TodoAddActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // user get from back activity
        user = intent.extras?.getParcelable<User>("obj")!!

        binding.editextJobName.setOnClickListener {
            binding.editextDescription.requestFocus()
        }
        binding.editextDescription.setOnClickListener {
            hideKeyboard(it)
            binding.btnAdd.requestFocus()
        }
        binding.btnAdd.setOnClickListener {
            if(ValidationCheck(des= binding.editextDescription, editextJobName = binding.editextJobName)) {
                var des = binding.editextDescription.text.trim().toString()
                var jobName = binding.editextJobName.text.trim().toString()
                InsertTodo(des =des,jobName =jobName)
            }
        }
    }

    private fun InsertTodo(des: String, jobName: String) {
        //var db =DbHelper(this)
      //  var todo_obj = Todo(user_email = user.email,description = des,id = "0",job = jobName)
        //db.addTodo(todo_obj)
        Toast.makeText(this,"Successfully todo added!!",Toast.LENGTH_LONG).show()
        this.finish()
    }

    fun ValidationCheck(des: EditText, editextJobName: EditText):Boolean {
        if(editextJobName.text.isNullOrEmpty()) {
            Toast.makeText(this,"Please enter job name",Toast.LENGTH_LONG).show()
            return false
        }
        if(des.text.isNullOrBlank()) {
            Toast.makeText(this,"Please description of job",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    private fun hideKeyboard(v: View) {
        val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}