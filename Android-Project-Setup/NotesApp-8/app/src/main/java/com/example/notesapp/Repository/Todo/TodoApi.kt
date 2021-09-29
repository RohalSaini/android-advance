package com.example.notesapp.Repository.Todo

import com.example.notesapp.Repository.User.Either
import com.example.notesapp.modal.*
import kotlinx.coroutines.flow.Flow

sealed interface TodoApi {
    suspend fun postGetAllJobs(obj: Data): Either<Throwable,Todo>
    suspend fun postAddJob(todo: TodoData):Flow<AddTodo>
}