package com.example.demo.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoViewModal(application: Application):AndroidViewModel(application) {
     val repository: TodoSchemaRepository
     val readAllData: LiveData<List<TodoSchema>>
    //lateinit var userSchema: LiveData<TodoSchema>
    init {
        val userSchemaDAO = Db.getDataBase(application).todoSchemaDao()
        repository = TodoSchemaRepository(todoSchemaDAO = userSchemaDAO)
        readAllData = repository.readAllData
      //  userSchema = MutableLiveData<TodoSchema>()
    }

    private fun addTodoSchema(user: TodoSchema) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo = user)
        }
    }

    suspend fun  getUserWithEmail(email: String): LiveData<List<TodoSchema>> {
        return withContext(viewModelScope.coroutineContext) {
            repository.findTodoByEmail(email = email)
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

    sealed class TodoAddUiState{
        data class Success(val todo:TodoSchema): TodoAddUiState()
        data class Error(val message:String):TodoAddUiState()
        object Loading :TodoAddUiState()
        object Empty: TodoAddUiState()
    }
    private val _todoUiState = MutableStateFlow<TodoAddUiState>(TodoAddUiState.Empty)
    val todoUiState: StateFlow<TodoAddUiState> = _todoUiState
    fun addTodo(todo:TodoSchema) {
        var TAG = "TODO ADD ACTIVITY"
        _todoUiState.value = TodoAddUiState.Loading
        if(todo.job.isEmpty()) {
            _todoUiState.value = TodoAddUiState.Error("Please enter job name !")
            return
        }
        if(todo.description.isEmpty()) {
            _todoUiState.value = TodoAddUiState.Error("Please enter description of job")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addTodoSchema(user = todo)
                _todoUiState.value = TodoAddUiState.Success(todo)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                _todoUiState.value = TodoAddUiState.Error(e.message.toString())
            }
        }
    }
}
