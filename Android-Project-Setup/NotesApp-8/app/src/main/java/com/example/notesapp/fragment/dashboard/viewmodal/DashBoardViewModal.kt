package com.example.notesapp.fragment.dashboard.viewmodal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Repository.Todo.TodoRepository
import com.example.notesapp.Repository.User.Either
import com.example.notesapp.Repository.User.UserRepository
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.modal.*
import com.example.notesapp.webserver.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class DashBoardViewModal: ViewModel() {
 
}