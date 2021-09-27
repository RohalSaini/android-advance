package com.example.notesapp.fragment.login.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.ktor.Api
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.util.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModal :ViewModel(){
    var userApi = Api()

    internal var _apiStateFlow: MutableStateFlow<ApiStatus<User>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<User>> = _apiStateFlow

    fun getLogin(obj: UserDetail) = viewModelScope.launch(Dispatchers.IO) {
        if (obj.email.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Email Required!!")
            return@launch
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(obj.email.trim()).matches()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Enter valid email address!!")
            return@launch
        }
        if(obj.password.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Password Required!!")
            return@launch
        }
        userApi.postlLogin(obj = obj).flowOn(Dispatchers.Main).onStart {
            _apiStateFlow.value = ApiStatus.Loading
            Log.d(TAG,"POST-USER-API -> is loading !!!")
        }.catch {
                error ->
                _apiStateFlow.value = ApiStatus.NetworkError(message = error.message.toString())
                Log.e(TAG,"POST-USER-API -> has Error ${error}!!!")
        }.onCompletion {
                Log.d(TAG,"POST-USER-API -> has Completed!!!")
        }.collect {
                data ->
                Log.d(TAG,"POST-USER-API -> is collecting Data from collector !!!")
                 if(data.statusCode == 200 || data.statusCode == 201) {
                    _apiStateFlow.value = ApiStatus.Success(data)

                } else {
                        data.error?.let {
                        _apiStateFlow.value = ApiStatus.NetworkError(data.error.toString())
                }
            }
        }
    }
}