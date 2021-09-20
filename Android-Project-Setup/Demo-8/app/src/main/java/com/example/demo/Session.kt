package com.example.demo

import android.content.Context
import android.content.SharedPreferences

class Session(var ctx: Context) {
    var prefs: SharedPreferences = ctx.getSharedPreferences("todo-app", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = prefs.edit()

    fun setLoggedIn(loggedIn:Boolean,email:String,password:String,username:String,id:String) {
        editor.putBoolean("loggedInMode",loggedIn)
        editor.putString("email", email)
        editor.putString("pass", password)
        editor.putString("name",username)
        editor.putString("id",id)
        editor.commit()
    }
    fun loggedIn(): Boolean {
        return prefs.getBoolean("loggedInMode", false)
    }

    fun getName(): String? {
        return prefs.getString("name","empty")
    }
    fun getId(): String? {
        return prefs.getString("id","empty")
    }
    fun getPassword(): String? {
        return prefs.getString("pass","empty")
    }
    fun getEmail(): String? {
        return prefs.getString("email","empty")
    }
    fun removeAll() {
        prefs.edit().remove("todo-app").commit()
    }
}