package com.example.notesapp.Repository.Todo

import android.util.Log
import com.example.notesapp.Repository.Ktor
import com.example.notesapp.modal.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class TodoRepository:TodoApi {
    var ktor: Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"

    override fun postGetAllJobs(obj: Data): Flow<Todo> = flow {
        try {
            emit(ktor.client.post{
                contentType(ContentType.Application.Json)
                body = obj
                url("$uri/userAllJobs")
            })
        }catch (error:java.lang.IllegalArgumentException) {
            Log.d("TAG",error.message.toString())
        }

    }

    override suspend fun postAddJob(todo: TodoData): Flow<AddTodo> = flow {
        emit(ktor.client.post{
            contentType(ContentType.Application.Json)
            body = todo
            url("$uri/addJob")
        })
    }
}