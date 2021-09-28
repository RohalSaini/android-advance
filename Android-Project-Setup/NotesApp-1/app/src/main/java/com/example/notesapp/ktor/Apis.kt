package com.example.notesapp.ktor

import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import kotlinx.coroutines.flow.Flow

sealed interface UserApi {
    // Api for creating User
    suspend fun postUser(obj: UserDetail):Flow<User>
}
