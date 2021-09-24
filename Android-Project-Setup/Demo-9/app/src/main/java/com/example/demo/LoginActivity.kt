package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demo.Adapter.Car
import com.example.demo.databinding.*
import com.example.demo.room.UserSchema
import com.example.demo.room.UserViewModal
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val TAG = "LOGIN ACTIVITY"
    val viewModal:UserViewModal by viewModels()
    private lateinit var view:ScrollView

    @Inject
    @Named("app_name")
    lateinit var name:String

    @Inject
    lateinit var car:Car
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        Log.d("activity",name)
        car.getName()
        settingOnClickListener()
        if(viewModal.loginStatus()) {
            callToActivity(obj= viewModal.getLoginDetail())
        }
    }

    private fun settingOnClickListener() {
        binding.editextEmailAddress.setOnClickListener {
            binding.editextPasssword.requestFocus()
        }
        binding.editextPasssword.setOnClickListener {
            hideKeyboard(it)
            binding.btnLogin.requestFocus()
        }
        binding.btnLogin.setOnClickListener {
            hideKeyboard(it)
            viewModal.login(
                email = binding.editextEmailAddress.text.trim().toString(),
                password = binding.editextPasssword.text.toString(),
                context = this
            )
            viewModal.viewModelScope.launch {
                viewModal.loginUiState.collect {
                    data ->
                    when(data) {
                        is UserViewModal.LoginUiState.Loading -> {
                            Snackbar.make(view,"Loading",Snackbar.LENGTH_SHORT).show()
                        }
                        is UserViewModal.LoginUiState.Error -> {
                            Snackbar.make(view,data.message,Snackbar.LENGTH_SHORT).show()
                        }
                        is UserViewModal.LoginUiState.Success -> {
                            var user = data.user
                            Snackbar.make(view,"Successfully User login",Snackbar.LENGTH_SHORT).show()
                            callToActivity(obj= user)
                        }
                        else ->  {
                            Snackbar.make(view,"Nothing to do!!",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.registerHere.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
    }
    private fun callToActivity(obj: UserSchema) {
        startActivity(
            Intent(this,DashboardActivity::class.java)
                .putExtra("obj",obj))
    }

    private fun hideKeyboard(v: View) {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    override fun onRestart() {
        super.onRestart()
        binding.editextEmailAddress.text.clear()
        binding.editextPasssword.text.clear()
    }
}