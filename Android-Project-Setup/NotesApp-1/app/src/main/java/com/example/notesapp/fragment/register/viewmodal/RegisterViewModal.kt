package com.example.notesapp.fragment.register.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.ktor.Api
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.util.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
const val TAG = "Registration View Modal"
class RegisterViewModal:ViewModel() {
    var userApi = Api()

    private var _apiStateFlow: MutableStateFlow<ApiStatus<User>> = MutableStateFlow(ApiStatus.Empty)
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

        userApi.postUser(obj = obj).onStart {
                _apiStateFlow.value = ApiStatus.Loading
                Log.d(TAG,"POST-USER-API -> is loading !!!")
        }.catch {
                error ->
                _apiStateFlow.value = ApiStatus.Error(message = error.message.toString())
                Log.e(TAG,"POST-USER-API -> has Error ${error.message.toString()}!!!")
        }.onCompletion {
                Log.e(TAG,"POST-USER-API -> has Completed!!!")
        }.collect {
                list ->
                Log.d(TAG,"POST-USER-API -> is collecting Data from collector !!!")
                _apiStateFlow.value = ApiStatus.Success(list)
        }
    }
}