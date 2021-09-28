package com.example.notesapp.ktor

import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow

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
}