package com.example.notesapp.Repository.Todo

import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import kotlinx.coroutines.flow.Flow

sealed interface TodoApi {
    suspend fun postGetTodo(obj: UserDetail): Flow<User>
}