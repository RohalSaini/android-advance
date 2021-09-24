package com.example.demo.room

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.demo.Session
import dagger.Binds
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TodoViewModal @Inject constructor(
    @ApplicationContext context: Context,
    @Named("session") val session: Session) :ViewModel() {
     val repository: TodoSchemaRepository
     val readAllData: LiveData<List<TodoSchema>>
     lateinit var userSchema: LiveData<TodoSchema>
    init {
        val userSchemaDAO = Db.getDataBase(context.applicationContext).todoSchemaDao()
        repository = TodoSchemaRepository(todoSchemaDAO = userSchemaDAO)
        readAllData = repository.readAllData
        userSchema = MutableLiveData<TodoSchema>()
    }

    private fun addTodoSchema(user: TodoSchema) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo = user)
        }
    }
    fun deleteTodoByID(todo:TodoSchema) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodoByID(todo =todo)
        }
    }
    fun updateTodoByID(todo:TodoSchema) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodoByID(todo = todo)
        }
    }
    suspend fun  getUserWithEmail(email: String,index:Int,limit:Int=4): LiveData<List<TodoSchema>> {
        return withContext(viewModelScope.coroutineContext) {
            repository.findTodoByEmail(email = email,index=index,limit=limit)
        }
    }

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
