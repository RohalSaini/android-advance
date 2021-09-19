package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demo.databinding.*
import com.example.demo.room.UserSchema
import com.example.demo.room.UserViewModal
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val TAG = "LOGIN ACTIVITY"
    lateinit var viewModal:UserViewModal
    private lateinit var view:ScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        viewModal = ViewModelProvider(this)[UserViewModal::class.java]
        settingOnClickListener()
        viewModal.loginStatus()
        if(viewModal.loginStatus()) {
           var obj = viewModal.getLoginDetail()
            callToActivity(obj)
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
            var intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
    private fun callToActivity(obj: UserSchema) {
        var intent = Intent(this,DashboardActivity::class.java)
        intent.putExtra("obj",obj)
        startActivity(intent)
    }

    private fun hideKeyboard(v: View) {
        val inputMethodManager:InputMethodManager= getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    override fun onRestart() {
        super.onRestart()
        binding.editextEmailAddress.text.clear()
        binding.editextPasssword.text.clear()
    }
}