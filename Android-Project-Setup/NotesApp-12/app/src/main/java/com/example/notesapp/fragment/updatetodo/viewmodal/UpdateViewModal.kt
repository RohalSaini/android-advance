package com.example.notesapp.fragment.updatetodo.viewmodal

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Repository.Todo.TodoRepository
import com.example.notesapp.Repository.User.UserRepository
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.modal.*
import com.example.notesapp.util.Session
import com.example.notesapp.webserver.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UpdateViewModal :ViewModel(){
    var todoApi= TodoRepository()
    private var _apiStateFlow: MutableStateFlow<ApiStatus<AddTodo>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<AddTodo>> = _apiStateFlow

    fun postUpdateTodo(obj: TodoData) = viewModelScope.launch(Dispatchers.IO) {
        if (obj.job.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Job Required!!")
            return@launch
        }
        if(obj.description.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Description Required!!")
            return@launch
        }
        todoApi.postUpdateJob(todo = obj).flowOn(Dispatchers.Main).onStart {
            _apiStateFlow.value = ApiStatus.Loading
            Log.d(TAG,"POST-USER-API -> is loading !!!")
        }.catch {
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

    fun setValueToDefault() {
        _apiStateFlow.value = ApiStatus.Empty
    }

}