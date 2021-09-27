package com.example.notesapp.fragment.register.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.ktor.Api
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.util.ApiStatus
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "Registration View Modal"
class RegisterViewModal:ViewModel() {
    var userApi = Api()

    internal var _apiStateFlow: MutableStateFlow<ApiStatus<User>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<User>> = _apiStateFlow

    fun getPost(obj: UserDetail) = viewModelScope.launch(Dispatchers.IO) {
        if(obj.username.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Username Required!!")
            return@launch
        }
        if(obj.password.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Password Required!!")
            return@launch
        }
        if (obj.email.trim().isEmpty()) {
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
            _apiStateFlow.value = ApiStatus.Error(message = error.message.toString())
            Log.e(TAG,"POST-USER-API -> has Error ${error.toString()}!!!")
        }.onCompletion {
            Log.d(TAG,"POST-USER-API -> has Completed!!!")
        }.collect {
                data ->
            Log.d(TAG,"POST-USER-API -> is collecting Data from collector !!!")
            data.error?.let {
                _apiStateFlow.value = ApiStatus.NetworkError(data.error.toString())
            }
            if(data.error.isNullOrEmpty()) {
                _apiStateFlow.value = ApiStatus.Success(data)
            }
        } /*
        userApi.postUser(obj = obj).onStart {
                _apiStateFlow.value = ApiStatus.Loading
                Log.d(TAG,"POST-USER-API -> is loading !!!")
        }.catch {
                error ->
                _apiStateFlow.value = ApiStatus.Error(message = error.message.toString())
                Log.e(TAG,"POST-USER-API -> has Error ${error.toString()}!!!")
        }.onCompletion {
                Log.d(TAG,"POST-USER-API -> has Completed!!!")
        }.collect {
                data ->
                Log.d(TAG,"POST-USER-API -> is collecting Data from collector !!!")
                data.error?.let {
                    _apiStateFlow.value = ApiStatus.NetworkError(data.error.toString())
                }
                if(data.error.isNullOrEmpty()) {
                    _apiStateFlow.value = ApiStatus.Success(data)
                }
        } */
    }
}