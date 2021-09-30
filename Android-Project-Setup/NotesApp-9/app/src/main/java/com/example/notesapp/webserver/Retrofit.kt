package com.example.notesapp.webserver
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit {
    private object Holder {
        val INSTANCE = com.example.notesapp.webserver.Retrofit()
    }
    companion object {
        val instance:com.example.notesapp.webserver.Retrofit by lazy { Holder.INSTANCE }
    }

    var  okHttpClient:OkHttpClient?=null
    var retrofit:Retrofit?=null
    fun httpClient() {
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    fun retrofitClient(): Retrofit? {
        httpClient()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://node-basic--app.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}