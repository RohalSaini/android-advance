package com.example.demo.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class TodoViewModal(application: Application):AndroidViewModel(application) {
     val repostory: TodoSchemaRepostory
     val readAllData: LiveData<List<TodoSchema>>
    //lateinit var userSchema: LiveData<TodoSchema>
    init {
        val userSchemaDAO = Db.getDataBase(application).todoSchemaDao()
        repostory = TodoSchemaRepostory(todoSchemaDAO = userSchemaDAO)
        readAllData = repostory.readAllData
      //  userSchema = MutableLiveData<TodoSchema>()
    }

    fun addTodo(user: TodoSchema) {
        viewModelScope.launch(Dispatchers.IO) {
            repostory.addTodo(todo = user)
        }
    }

//    fun getUserWithEmail(email: String){
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                repostory.findTodoByEmail(email=email).also { userSchema = it }
//            }
//
//        }
//    }
}
