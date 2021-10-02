package com.example.notesapp.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Session(var ctx: Context) {
    var prefs: SharedPreferences = ctx.getSharedPreferences("notes-app", Context.MODE_PRIVATE)
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
        prefs.edit().remove("todo-app").apply()
    }
    fun setDataFromSharedPreferences(list: ArrayList<com.example.notesapp.modal.TodoData>) {
        val gson = Gson()
        val jsonCurProduct: String = gson.toJson(list)
        editor.putString("list", jsonCurProduct)
        editor.commit()
    }
    fun getDataFromSharedPreferences(): ArrayList<com.example.notesapp.modal.TodoData> {
            val gson = Gson()
            val list:ArrayList<com.example.notesapp.modal.TodoData>
            val string: String? = prefs.getString("list","empty")
            val type: Type = object : TypeToken<ArrayList<com.example.notesapp.modal.TodoData?>?>() {}.type
            list = gson.fromJson<ArrayList<com.example.notesapp.modal.TodoData>>(string, type)
            return list
    }
}