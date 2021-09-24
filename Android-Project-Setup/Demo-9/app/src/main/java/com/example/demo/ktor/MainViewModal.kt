package com.example.demo.ktor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModal @Inject constructor() :ViewModel(){
    //https://medium.com/google-developer-experts/how-to-use-ktor-client-on-android-dcdeddc066b9
    //private val networkHandler: NetworkHandler,
    private val userApi =UserApi(ApiService())
    private var repository = MainRepository()
    private var _apiStateFlow: MutableStateFlow<ApiStatus<List<Post>>> = MutableStateFlow(ApiStatus.Empty)
    val apiStateFlow:StateFlow<ApiStatus<List<Post>>> = _apiStateFlow

    fun getPost() = viewModelScope.launch {
        repository.getPost().onStart {
            _apiStateFlow.value = ApiStatus.Loading
        }.catch {
            error ->
            _apiStateFlow.value = ApiStatus.Error(message = error.message.toString())
            Log.d("State Error",error.toString())
        }.collect {
            list ->
            _apiStateFlow.value = ApiStatus.Success(list)
        }
    }
    //when (networkHandler.isConnected) {
        //true -> {
            //try {
             //   Either.Right(userApi.getUserKtor(userId).toDomain())
           // } catch (e: Exception) {
           //     Either.Left(e.toCustomExceptions())
         //   }
       // }
      //  else -> Either.Left(Failure.NetworkConnection)
    //}
}