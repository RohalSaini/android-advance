package com.example.demo.room

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.demo.LoginActivity
import com.example.demo.RegistrationActivity
import com.example.demo.Session
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UserViewModal @Inject constructor(
    @ApplicationContext context:Context,
    @Named("session") var session: Session
    ):ViewModel()
{
    val TAG = "LOGIN ACTIVITY"
    private val repository: UserSchemaRepostory

    lateinit var activity: String
    private val readAllData: LiveData<List<UserSchema>>
    lateinit var userSchema: LiveData<UserSchema>
    init {
        val userSchemaDAO = Db.getDataBase(context.applicationContext).userSchemaDao()
        repository = UserSchemaRepostory(userSchemaDAO = userSchemaDAO)
        readAllData = repository.readAllData
        userSchema = MutableLiveData<UserSchema>()
    }

    private fun addUser(user: UserSchema) {
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
        object Empty: LoginUiState()
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
                    _loginUiState.value = LoginUiState.Error("Incorrect Password ,Please fill correct password")
                    return@Observer
                }
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
        return session.loggedIn()
    }
    fun getLoginDetail():UserSchema {
        return UserSchema(
            username = session.getName().toString(),
            password = session.getPassword().toString(),
            email = session.getEmail().toString()
        )
    }

    sealed class RegisterUiState{
        data class Success(val user:UserSchema): RegisterUiState()
        data class Error(val message:String):RegisterUiState()
        object Loading :RegisterUiState()
        object Empty: RegisterUiState()
    }
    private val _registerUiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Empty)
    val registerUiState:StateFlow<RegisterUiState> = _registerUiState
    fun registerUser(username:String,password:String,email:String,context: RegistrationActivity) {
        var TAG = "REGISTRATION ACTIVITY"
        _registerUiState.value = RegisterUiState.Loading
        if (username.isEmpty()) {
            _registerUiState.value = RegisterUiState.Error("Please enter username!!")
            return
        }
        if (password.isEmpty()) {
            _registerUiState.value = RegisterUiState.Error("Please enter password!!")
            return
        }
        if (email.isEmpty()) {
            _registerUiState.value = RegisterUiState.Error("Please enter email address")
            return
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _registerUiState.value = RegisterUiState.Error("Please enter valid email address")
            return
        }

            viewModelScope.launch(Dispatchers.Main) {
                var user = getUserWithEmail(email = email)
                user.observe(context, Observer { user ->
                    if (user != null) {
                        _loginUiState.value =
                            LoginUiState.Error("User is register with given email address ,Please use another email address")
                        return@Observer
                    } else {
                        try {
                            user?.let { addUser(it) }
                            _registerUiState.value = user?.let { RegisterUiState.Success(it) }!!
                        } catch (e: Exception) {
                            Log.e(TAG, e.message.toString())
                            _registerUiState.value = RegisterUiState.Error(e.message.toString())
                        }
                    }
                })
            }
        }
}
