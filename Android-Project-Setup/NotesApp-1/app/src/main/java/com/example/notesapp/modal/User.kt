package com.example.notesapp.modal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("success")
    var success:Boolean,

    @SerialName("data")
    var data:Data
)
@Serializable
data class Data (
    @SerialName("_id")
    var id:String,

    @SerialName("username")
    var username:String,

    @SerialName("password")
    var password:String,

    @SerialName("email")
    var email:String
    )

@Serializable
data class UserDetail(
    @SerialName("username")
    var username: String,
    @SerialName("password")
    var password:String,
    @SerialName("email")
    var email:String
    )