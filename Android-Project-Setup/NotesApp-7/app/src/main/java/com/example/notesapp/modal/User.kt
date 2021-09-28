package com.example.notesapp.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var statusCode:Int,
    var success:Boolean,
    var data:Data?= null,
    var error:String ?= null,

)
@Parcelize
@Serializable
data class Data (
    var _id:String,
    var username:String,
    var password:String,
    var email:String
    ):Parcelable

@Serializable
data class UserDetail(
    var username: String,
    var password:String,
    var email:String
    )

/* 200 201 -> api correct
*
* 400 ,5009*/