package com.example.notesapp.fragment.dashboard.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Repository.Todo.TodoRepository
import com.example.notesapp.Repository.User.UserRepository
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.modal.Data
import com.example.notesapp.modal.Todo
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.webserver.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashBoardViewModal: ViewModel() {
    var todoApi = TodoRepository()
    private var _apiStateFlow: MutableStateFlow<ApiStatus<Todo>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<Todo>> = _apiStateFlow
     suspend fun postGetAllTodo(user:Data) = viewModelScope.launch(Dispatchers.IO) {
        todoApi.postGetAllJobs(obj = user)
            .catch {
            error ->
                try {
                    _apiStateFlow.value = ApiStatus.NetworkError(message = error.message.toString())
                    Log.e(TAG,"POST-USER-API -> has Error ${error}!!!")
                }catch (e:java.lang.IllegalArgumentException ) {
                    Log.d("TAG",e.message.toString())
                }
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