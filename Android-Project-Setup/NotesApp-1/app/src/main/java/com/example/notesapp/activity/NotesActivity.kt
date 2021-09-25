package com.example.notesapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.fragment.SplashScreen
import com.example.notesapp.util.Session

class NotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var session:Session
    var status:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        session = Session(this)
        setContentView(binding.root)
        if (status) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container,SplashScreen())
            transaction.commit()
        } // else if (session.loggedin()) {
            //LoginDataUser.instance.token = session.getToken()
            //val transaction = supportFragmentManager.beginTransaction()
            //transaction.replace(R.id.container, FragmentDashboard())
            //transaction.commit()
        //}
    }
}