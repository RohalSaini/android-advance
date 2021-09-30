package com.example.notesapp.repository.Todo

import com.example.notesapp.modal.*
import kotlinx.coroutines.flow.Flow

sealed interface TodoApi {
    fun postGetAllJobs(obj: Data): Flow<Todo>
    suspend fun postAddJob(todo: TodoData):Flow<AddTodo>
}