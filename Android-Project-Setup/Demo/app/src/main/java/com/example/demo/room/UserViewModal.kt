package com.example.demo.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.demo.LoginActivity
import com.example.demo.Session
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModal(application: Application):AndroidViewModel(application) {
    private val repository: UserSchemaRepostory
    private val readAllData: LiveData<List<UserSchema>>
    lateinit var userSchema: LiveData<UserSchema>
    init {
        val userSchemaDAO = Db.getDataBase(application).userSchemaDao()
        repository = UserSchemaRepostory(userSchemaDAO = userSchemaDAO)
        readAllData = repository.readAllData
        userSchema = MutableLiveData<UserSchema>()
    }
    override fun <T : Application?> getApplication(): T {
        return super.getApplication()
    }
    fun addUser(user: UserSchema) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user = user)
        }
    }

    private suspend fun  getUserWithEmail(email: String): LiveData<UserSchema> {
        return withContext(viewModelScope.coroutineContext) {
            repository.findUserByEmail(email = email)
        }
    }

    sealed class LoginUiState{
        data class Success(val user:UserSchema): LoginUiState()
        data class Error(val message:String):LoginUiState()
        object Loading :LoginUiState()
        object  Empty: LoginUiState()
    }
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState:StateFlow<LoginUiState> = _loginUiState

    fun login(email:String, password:String,context:LoginActivity) {
        _loginUiState.value = LoginUiState.Loading
        if (email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _loginUiState.value = LoginUiState.Error("Please enter valid email address")
            return
        }
        if (password.trim().isEmpty()) {
            _loginUiState.value = LoginUiState.Error("Please enter valid password")
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            var user = getUserWithEmail(email = email)
            user.observe(context, Observer {
                user ->
                if (user == null) {
                    _loginUiState.value =LoginUiState.Error("Database have no entry with given email address")
                    return@Observer
                }
                if (user.password != password) {
                    _loginUiState.value = LoginUiState.Error("Password Error")
                    return@Observer
                }
                val session = Session(getApplication())
                session.setLoggedIn(
                    loggedIn = true,
                    email= user.email,
                    password = user.password,
                    username = user.username,
                    id= "0")
                _loginUiState.value = LoginUiState.Success(user = user)
            })
        }
    }
    fun loginStatus():Boolean {
        return Session(getApplication()).loggedIn()
    }
    fun getLoginDetail():UserSchema {
        var session = Session(getApplication())
        return UserSchema(
            username = session.getName().toString(),
            password = session.getPassword().toString(),
            email = session.getEmail().toString()
        )
    }
}
