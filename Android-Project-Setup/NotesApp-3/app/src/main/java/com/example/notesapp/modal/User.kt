package com.example.notesapp.modal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var statusCode:Int,
    var success:Boolean,
    var data:Data?= null,
    var error:String ?= null,

)
@Serializable
data class Data (
    var _id:String,
    var username:String,
    var password:String,
    var email:String
    )

@Serializable
data class UserDetail(
    var username: String,
    var password:String,
    var email:String
    )