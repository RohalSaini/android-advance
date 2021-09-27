package com.example.notesapp.ktor.repo

import com.example.notesapp.ktor.Ktor
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*

class Repository: Api {
    var ktor: Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"
    //registration for user
    override suspend fun postUser(obj: UserDetail) = flow <User>{
        emit(ktor.client.post{
            contentType(ContentType.Application.Json)
            body = obj
            url("$uri/registerUser")
        })
    }
    // logionn authentication check
    override suspend fun postlLogin(obj: UserDetail) =flow<User> {
        emit(ktor.client.post {
            contentType(ContentType.Application.Json)
            body = obj
            url("$uri/login")
        })
    }
       /*
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
        } */
}
