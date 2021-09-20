package com.example.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demo.databinding.ActivityAddTodoBinding
import com.example.demo.modal.Todo
import com.example.demo.modal.User
import com.example.demo.room.TodoSchema
import com.example.demo.room.TodoViewModal
import com.example.demo.room.UserSchema
import com.example.demo.room.UserViewModal
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TodoAddActivity : AppCompatActivity() {
    lateinit var user: UserSchema
    lateinit var binding:ActivityAddTodoBinding
    private val TAG = "TodoAddActivity"
    lateinit var viewModal:TodoViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModal = ViewModelProvider(this)[TodoViewModal::class.java]

        // user get from back activity
        user = intent.extras?.getParcelable<UserSchema>("obj")!!

        binding.editextJobName.setOnClickListener {
            binding.editextDescription.requestFocus()
        }
        binding.editextDescription.setOnClickListener {
            hideKeyboard(it)
            binding.btnAdd.requestFocus()
        }
        binding.btnAdd.setOnClickListener {
            view -> hideKeyboard(view)
            var todo = TodoSchema(
                description = binding.editextDescription.text.trim().toString(),
                job = binding.editextJobName.text.trim().toString(),
                email = user.email
            )
            viewModal.addTodo(todo)
            viewModal.viewModelScope.launch {
                viewModal.todoUiState.collect {
                        data ->
                    when(data) {
                        is TodoViewModal.TodoAddUiState.Loading -> {
                            Snackbar.make(view,"Loading", Snackbar.LENGTH_SHORT).show()
                        }
                        is TodoViewModal.TodoAddUiState.Error -> {
                            Snackbar.make(view,data.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is TodoViewModal.TodoAddUiState.Success -> {
                            var user = data.todo
                            Snackbar.make(view,"Successfully Todo is Created !!", Snackbar.LENGTH_SHORT).show()
                            this@TodoAddActivity.finish()
                        }
                        else ->  {
                            Snackbar.make(view,"Nothing to do!!", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun hideKeyboard(v: View) {
        val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}