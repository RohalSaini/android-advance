package com.example.notesapp.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    var statusCode:Int,
    var success:Boolean,
    var data:ArrayList<TodoData>?= null,
    var error:String ?= null) 


@Serializable
data class AddTodo(
    var statusCode:Int,
    var success:Boolean,
    var data:TodoData ?= null,
    var error:String ?= null)

@Parcelize
@Serializable
data class TodoData (
    var _id:String,
    var job:String,
    var description:String,
    var email:String
):Parcelable