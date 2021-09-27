package com.example.notesapp.fragment.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentLoginBinding
import com.example.notesapp.fragment.dashboard.DashboardFragment
import com.example.notesapp.fragment.login.viewmodal.LoginViewModal
import com.example.notesapp.fragment.register.RegistrationFragment
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.util.ApiStatus
import com.example.notesapp.util.hideKeybaord
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "LOGIN FRAGMENT"
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var modal : LoginViewModal
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modal = ViewModelProvider(this).get(LoginViewModal::class.java)
        setOnClickListener()
    }
    private fun setOnClickListener() {
        binding.registerHere.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container,RegistrationFragment())
            transaction.commit()
        }
        binding.btnLogin.setOnClickListener {
            var email = binding.emailAddressEdt.text.trim().toString()
            var password =binding.passswordEdt.text.trim().toString()
            var obj = UserDetail(email=email,password = password,username = "")
            modal.getLogin(obj)
            hideKeybaord(it,requireActivity())
            callToCoroutine()
        }
        binding.emailAddressEdt.setOnClickListener {
            binding.passswordEdt.requestFocus()
        }
        binding.passswordEdt.setOnClickListener {
            hideKeybaord(it,requireActivity())
        }
    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun callToCoroutine() {
        modal.viewModelScope.launch(Dispatchers.IO) {
            modal._apiStateFlow.value = ApiStatus.Loading
            modal.apiStateFlow.collect {
                    status ->
                when(status) {
                    is ApiStatus.Error -> {
                        Snackbar.make(binding.root, "Error: ${status.message}", Snackbar.LENGTH_LONG).show()
                        Log.e("Error from Registration Fragment",status.message)
                    }
                    is ApiStatus.Success -> {
                        Log.d("Success",status.data.toString())
                        Snackbar.make(binding.root, "Success: ${status.data}", Snackbar.LENGTH_LONG).show()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.container,DashboardFragment())
                        transaction.commit()
                    }
                    is ApiStatus.NetworkError -> {
                        Snackbar.make(binding.root, "Error: ${status.message}", Snackbar.LENGTH_LONG).show()
                    }
                    is ApiStatus.Loading -> Unit
                    is ApiStatus.Empty -> Unit
                }
            }
        }
    }
}