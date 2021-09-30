package com.example.notesapp.fragment.dashboard.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.notesapp.modal.UserDetail
import com.example.notesapp.webserver.ApiService
import com.example.notesapp.webserver.Retrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DashBoardViewModal: ViewModel() {
    fun getPost(user:UserDetail) {
        var api = Retrofit.instance.retrofitClient()?.create(ApiService::class.java)
            ?.getUserAllJobs(user = user)
        api?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                Log.d("TAG",response?.isSuccessful.toString())
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.d("TAG",t?.message.toString())
            }

        } )
    }
}