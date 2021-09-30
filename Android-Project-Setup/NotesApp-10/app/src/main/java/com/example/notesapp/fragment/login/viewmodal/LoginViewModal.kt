package com.example.notesapp.fragment.login.viewmodal

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.Repository.User.UserRepository
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.webserver.ApiStatus
import com.example.notesapp.util.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModal :ViewModel() {
    var userApi = UserRepository()
    private var _apiStateFlow: MutableStateFlow<ApiStatus<User>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow: StateFlow<ApiStatus<User>> = _apiStateFlow

    fun getLogin(obj: UserDetail) = viewModelScope.launch(Dispatchers.IO) {
        if (obj.email.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Email Required!!")
            return@launch
        }
        if(obj.password.trim().isEmpty()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Password Required!!")
            return@launch
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(obj.email.trim()).matches()) {
            _apiStateFlow.value = ApiStatus.Error(message = " Enter valid email address!!")
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

     fun saveUserToSession(response: User,context:Context) {
        var session:Session = Session(context)
        session.setLoggedIn(loggedIn = true,
                            email = response.data!!.email,
                            password = response.data!!.password,
                            username = response.data!!.password,
                            id = response.data!!._id)
    }

    fun setValueToDefault() {
        _apiStateFlow.value = ApiStatus.Empty
    }

}