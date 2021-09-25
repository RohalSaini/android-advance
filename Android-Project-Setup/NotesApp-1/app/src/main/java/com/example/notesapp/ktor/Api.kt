package com.example.notesapp.ktor

import com.example.notesapp.Ktor
import com.example.notesapp.modal.Post
import io.ktor.client.request.*
class Api {
    var ktor:Ktor = Ktor()
    var uri: String = "https://node-basic--app.herokuapp.com"
    suspend fun getPost() : List<Post> {
        return  ktor.client.get {
            url("$uri/readme")
        }
    }
}