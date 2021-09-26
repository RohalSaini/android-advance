package com.example.notesapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentRegisterationBinding
import com.example.notesapp.ktor.Api
import com.example.notesapp.modal.UserDetail
import kotlinx.coroutines.runBlocking

private const val TAG = "Registration Fragment"
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegisterationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            try {
                var obj = Api().postRegisterUser(UserDetail(username = "ktor1234455",password = "ktor-1",email = "ktor7@ktor.com"))
                Log.i(TAG,obj.toString())
                println("User is:")
                println(obj)
            }catch (error:Exception) {
                Log.d(TAG,error.message.toString())
            }

        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View {
        _binding = FragmentRegisterationBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}