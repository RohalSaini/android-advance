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
import com.example.notesapp.databinding.FragmentDashboardBinding
import com.example.notesapp.databinding.FragmentLoginBinding
import com.example.notesapp.fragment.dashboard.DashboardFragment
import com.example.notesapp.fragment.login.viewmodal.LoginViewModal
import com.example.notesapp.fragment.register.RegistrationFragment
import com.example.notesapp.modal.Data
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.util.Session
import com.example.notesapp.webserver.ApiStatus
import com.example.notesapp.webserver.hideKeybaord
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
        if(Session(requireContext()).loggedIn()) {
            var session = Session(requireContext())
            var user:Data = Data(_id = session.getId().toString(),username = session.getName().toString(),password = session.getPassword().toString(),email = session.getEmail().toString())
            val bundle = Bundle()
            bundle.putParcelable("user",user)
            var fragment = DashboardFragment()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container,fragment)
            transaction.commit()
        }
        setOnClickListener()
    }
    private fun setOnClickListener() {
        binding.registerHere.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container,RegistrationFragment())
            transaction.commit()
        }
        binding.btnLogin.setOnClickListener {
            binding.loader.visibility = View.VISIBLE
            var email = binding.emailAddressEdt.text.trim().toString()
            var password =binding.passswordEdt.text.trim().toString()
            var obj = UserDetail(email=email,password = password,username = "")
            hideKeybaord(it,requireActivity())
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

}