package com.example.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demo.databinding.ActivityRegistrationBinding
import com.example.demo.room.UserViewModal
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityRegistrationBinding
    lateinit var radioButton_gender: RadioButton
    lateinit var countries:Array<String>
    lateinit var city:String
    private val TAG = "RegistrationActivity"
    val viewModal:UserViewModal by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        countries = resources.getStringArray(R.array.cities_array)
        binding.editextCity.onItemSelectedListener= this

        ArrayAdapter(this,android.R.layout.simple_spinner_item,countries)
            .also { adapter ->  binding.editextCity.adapter = adapter }

        binding.buttonLogin.setOnClickListener {
            viewModal.registerUser(
                        username = binding.editextUsername.text.trim().toString(),
                        password = binding.editextPasssword.text.trim().toString(),
                        email = binding.editextEmailAddress.text.trim().toString(),
                        context = this
            )
            viewModal.viewModelScope.launch {
                viewModal.registerUiState.collect {
                        data ->
                        when(data) {
                            is UserViewModal.RegisterUiState.Loading -> {
                                Snackbar.make(view,"Loading", Snackbar.LENGTH_SHORT).show()
                            }
                            is UserViewModal.RegisterUiState.Error -> {
                                Snackbar.make(view,data.message, Snackbar.LENGTH_SHORT).show()
                            }
                            is UserViewModal.RegisterUiState.Success -> {
                                var user = data.user
                                Log.d("Register User",user.toString())
                                Snackbar.make(view,"Successfully User Created", Snackbar.LENGTH_SHORT).show()
                                callingToActivity()
                            }
                            else ->  {
                                Snackbar.make(view,"Nothing to do!!", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }

        binding.editextUsername.setOnClickListener {
            binding.editextPasssword.requestFocus()
        }
        binding.editextPasssword.setOnClickListener {
            binding.editextEmailAddress.requestFocus()
        }
        binding.editextEmailAddress.setOnClickListener {
            hideKeyboard(it)
            binding.editextCity.requestFocus()
        }
        binding.checkboxUpdates.setOnClickListener {
            binding.radioMale.requestFocus()
        }
        binding.radioMale.setOnClickListener {
            binding.buttonLogin.requestFocus()
        }
        binding.radioFemale.setOnClickListener {
            binding.buttonLogin.requestFocus()
        }
    }



    private fun callingToActivity() {
        startActivity(Intent(this,LoginActivity::class.java))
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        this.city = countries[position]
        binding.checkboxUpdates.requestFocus()
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
        this.city= countries[0]
        binding.checkboxUpdates.requestFocus()
    }

    private fun hideKeyboard(v: View) {
        val inputMethodManager: InputMethodManager = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}