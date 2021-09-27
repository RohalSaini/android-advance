package com.example.notesapp.fragment.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentRegisterationBinding
import com.example.notesapp.fragment.login.LoginFragment
import com.example.notesapp.fragment.register.viewmodal.RegisterViewModal
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.util.ApiStatus
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.notesapp.util.hideKeybaord


private const val TAG = "Registration Fragment"
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegisterationBinding? = null
    private val binding get() = _binding!!
    private lateinit var modal:RegisterViewModal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modal = ViewModelProvider(this).get(RegisterViewModal::class.java)
        setOnClickListener()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View {
        _binding = FragmentRegisterationBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListener() {
        binding.buttonLogin.setOnClickListener {
            var username = binding.usernameEdt.text.trim().toString()
            var password = binding.passwordEdt.text.trim().toString()
            var email = binding.emailAddressEdt.text.trim().toString()
            modal.getPost(UserDetail(username = username,password=password,email=email))
            hideKeybaord(it,requireActivity())
            callToCoroutine()
        }
        binding.usernameEdt.setOnClickListener {
            view ->
            binding.passwordEdt.requestFocus()
            hideKeybaord(view,requireActivity())
        }
        binding.passwordEdt.setOnClickListener {
                view ->
                binding.emailAddressEdt.requestFocus()
                hideKeybaord(view, requireActivity())

        }
        binding.emailAddressEdt.setOnClickListener {
                view ->
                hideKeybaord(view, requireActivity())
        }
    }
    private fun callToCoroutine() {
        modal.viewModelScope.launch(Dispatchers.IO) {
            modal._apiStateFlow.value = ApiStatus.Loading
            modal.apiStateFlow.collect {
                    status ->
                when(status) {
                    is ApiStatus.Error -> {
                        Snackbar.make(binding.root, "Error: ${status.message}",Snackbar.LENGTH_LONG).show()
                        Log.e("Error from Registration Fragment",status.message)
                    }
                    is ApiStatus.Success -> {
                        Log.d("Success",status.data.toString())
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.container,LoginFragment())
                        transaction.commit()

                    }
                    is ApiStatus.NetworkError -> {
                        Snackbar.make(binding.root, "Error: ${status.message}",Snackbar.LENGTH_LONG).show()
                    }
                    is ApiStatus.Loading -> Unit
                    is ApiStatus.Empty -> Unit
                }
            }
        }
    }
}