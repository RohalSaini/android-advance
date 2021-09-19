package com.example.demo.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todo")
@Parcelize
data class TodoSchema (
        @PrimaryKey
        var id:String,
        var description:String,
        var job:String,
        var email:String):Parcelable