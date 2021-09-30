package com.example.notesapp.webserver


import com.example.notesapp.modal.Data
import com.example.notesapp.modal.Todo
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST

interface ApiService {
    @POST("userAllJobs ")
    fun getUserAllJobs(@Body user: Data): Call<Todo>
}