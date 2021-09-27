package com.example.notesapp.ktor

import android.util.Log
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class Api :UserApi {
    var ktor: Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"

    override suspend fun postUser(obj: UserDetail) = flow <User>{
        emit(ktor.client.post {
            contentType(ContentType.Application.Json)
            body = obj
            url("$uri/registerUser")
        })
    }

    override suspend fun postlLogin(obj: UserDetail) =flow<User> {
        try {
           var list = ktor.client.post<User> {
               contentType(ContentType.Application.Json)
               body = obj
               url("$uri/login")
           }
           emit(list)
        }catch (error: Exception) {
            Log.d("error: ","Error due to suspended object in try catch ${error.message}")
        }
    }.flowOn(Dispatchers.IO)
        .catch {
            error ->
            Log.d("error: ","Error due to suspended object ${error.message}")
        }
}
