package com.example.notesapp.fragment.addtodo.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Repository.Todo.TodoRepository
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.modal.AddTodo
import com.example.notesapp.modal.Data
import com.example.notesapp.modal.Todo
import com.example.notesapp.modal.TodoData
import com.example.notesapp.webserver.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddTodoViewModal: ViewModel() {
    var todoApi = TodoRepository()
    private var _apiStateFlow: MutableStateFlow<ApiStatus<AddTodo>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<AddTodo>> = _apiStateFlow
    fun postAddJob(todo: TodoData) = viewModelScope.launch(Dispatchers.Main) {
        if(todo.job.trim().isEmpty() || todo.job == "null") {
            _apiStateFlow.value = ApiStatus.Error(message = " Job name is  Required!!")
            return@launch
        }
        if(todo.description.trim().isEmpty() || todo.description.trim() == "null") {
            _apiStateFlow.value = ApiStatus.Error(message = " Description  Required!!")
            return@launch
        }
        todoApi.postAddJob(todo = todo)
            .catch {
                    error ->
                _apiStateFlow.value = ApiStatus.NetworkError(message = error.message.toString())
                Log.e(TAG,"POST-USER-API -> has Error ${error}!!!")
            }.onCompletion {
                Log.d(TAG,"POST-USER-API -> has Completed!!!")
            }.collect {
                    response ->
                Log.d(TAG,"POST-USER-API -> is collecting Data from collector !!!")
                if(response.statusCode == 200 || response.statusCode == 201) {
                    _apiStateFlow.value = ApiStatus.Success(response)

                } else {
                    response.error?.let {
                        _apiStateFlow.value = ApiStatus.NetworkError(response.error.toString())
                    }
                }
            }
    }
    fun setStateFlowToDefault() {
        _apiStateFlow.value = ApiStatus.Empty
    }
}