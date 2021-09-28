package com.example.notesapp.Repository.Todo

import com.example.notesapp.Repository.Ktor
import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoRepository:TodoApi {
    var ktor: Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"
    override suspend fun postGetTodo(obj: UserDetail): Flow<User> = flow {
        emit(ktor.client.post{
            contentType(ContentType.Application.Json)
            body = obj
            url("$uri/getUserWithEmail")
        })
    }
}