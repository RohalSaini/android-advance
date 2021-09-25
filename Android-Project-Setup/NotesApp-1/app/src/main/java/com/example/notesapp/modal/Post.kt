package com.example.notesapp.modal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post (
    @SerialName("id")
    var id:String,
    @SerialName("password")
    var password:String
)