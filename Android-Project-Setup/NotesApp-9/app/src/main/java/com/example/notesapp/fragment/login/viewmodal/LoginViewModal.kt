package com.example.notesapp.fragment.login.viewmodal

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.fragment.register.viewmodal.TAG
import com.example.notesapp.repository.User.UserRepository
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.webserver.ApiStatus
import com.example.notesapp.util.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModal :ViewModel() {

}