package com.example.notesapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.example.notesapp.modal.User

sealed class ApiStatus<out T : Any> {
    val <T> T.exhaustive: T
        get() = this
    object Empty : ApiStatus<Nothing>() // Empty
    data class Error(val message: String) : ApiStatus<Nothing>()  // input Error
    object Loading : ApiStatus<Nothing>()
    data class Success<out T : Any>(val data: User) : ApiStatus<T>() // Sucess with Data
    data class NetworkError(val message: String, val cause: Exception? = null) : ApiStatus<Nothing>()
}

fun hideKeybaord(v: View, requireActivity: FragmentActivity) {
    val inputMethodManager = requireActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
}