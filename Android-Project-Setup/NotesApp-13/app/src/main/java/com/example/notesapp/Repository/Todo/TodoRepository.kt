package com.example.notesapp.Repository.Todo

import android.util.Log
import com.example.notesapp.Repository.Ktor
import com.example.notesapp.Repository.User.Either
import com.example.notesapp.modal.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class TodoRepository:TodoApi {
    var ktor: Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"
    override suspend fun postGetAllJobs(obj: Data): Either<Exception, Todo> {
        return try {
            var list = ktor.client.post<Todo>{
                contentType(ContentType.Application.Json)
                body = obj
                url("$uri/userAllJobs")
            }
           Either.Right(list)
        }catch (error:Exception) {
            Log.d("TAG",error.message.toString())
            Either.Left(error)
        }
    }


    override suspend fun postAddJob(todo: TodoData): Flow<AddTodo> = flow {
        emit(ktor.client.post{
            contentType(ContentType.Application.Json)
            body = todo
            url("$uri/addJob")
        })
    }

    override suspend fun postDeleteJob(todo: TodoData): Flow<AddTodo> = flow {
        emit(ktor.client.post{
            contentType(ContentType.Application.Json)
            body = todo
            url("$uri/deleteJob")
        })
    }

    override suspend fun postUpdateJob(todo: TodoData): Flow<AddTodo> = flow {
        emit(ktor.client.post{
            contentType(ContentType.Application.Json)
            body = todo
            url("$uri/updateJob")
        })
    }
}