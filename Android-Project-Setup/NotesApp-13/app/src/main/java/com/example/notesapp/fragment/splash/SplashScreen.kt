package com.example.notesapp.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentSplashScreenBinding
import com.example.notesapp.fragment.login.LoginFragment

class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container,LoginFragment())
            transaction.commit()
        }, 3000)
    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
