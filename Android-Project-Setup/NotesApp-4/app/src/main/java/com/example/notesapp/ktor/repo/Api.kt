package com.example.notesapp.ktor.repo

import com.example.notesapp.modal.User
import com.example.notesapp.modal.UserDetail
import kotlinx.coroutines.flow.Flow


sealed interface Api {

    // User Related Api
    suspend fun postUser(obj: UserDetail): Flow<User>
    suspend fun postlLogin(obj: UserDetail): Flow<User>


}

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()
}