package com.example.notesapp.ktor

import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import io.ktor.client.request.*
import io.ktor.http.*

class Api {
    var ktor: Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"
    suspend fun postRegisterUser(obj: UserDetail): User {
        return ktor.client.post {
            contentType(ContentType.Application.Json)
            body = obj
            url(" $uri/registerUser")
        }
    }
}