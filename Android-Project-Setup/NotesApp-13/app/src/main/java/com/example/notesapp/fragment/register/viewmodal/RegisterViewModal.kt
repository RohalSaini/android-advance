package com.example.notesapp.fragment.register.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Repository.User.UserRepository
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.webserver.ApiStatus
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val TAG = "Registration View Modal"
class RegisterViewModal:ViewModel() {
    var userApi = UserRepository()

    private var _apiStateFlow: MutableStateFlow<ApiStatus<User>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<User>> = _apiStateFlow

    fun getPost(obj: UserDetail) = viewModelScope.launch(Dispatchers.IO) {
        if(obj.username.trim().isEmpty() || obj.username == "null") {
            _apiStateFlow.value = ApiStatus.Error(message = " Username Required!!")
            return@launch
        }
        if(obj.password.trim().isEmpty() || obj.username == "null") {
            _apiStateFlow.value = ApiStatus.Error(message = " Password Required!!")
            return@launch
        }
        if (obj.email.trim().isEmpty() || obj.username == "null") {
            _apiStateFlow.value = ApiStatus.Error(message = " Email Required!!")
            return@launch
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(obj.email.trim()).matches()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Enter valid email address!!")
            return@launch
        }
        userApi.postUser(obj = obj).flowOn(Dispatchers.IO).onStart {
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
                if(response.statusCode == 200 || response.statusCode == 201) {
                    _apiStateFlow.value = ApiStatus.Success(response)

                } else {
                    response.error?.let {
                        _apiStateFlow.value = ApiStatus.NetworkError(response.error.toString())
                    }
                }
                Log.d(TAG,"POST-USER-API -> is collecting Data from collector !!!")
        }
    }
    fun setValueToDefault() {
        _apiStateFlow.value = ApiStatus.Empty
    }
}