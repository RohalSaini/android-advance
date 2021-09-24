package com.example.demo.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todo")
@Parcelize
data class TodoSchema (
        @PrimaryKey(autoGenerate = true)
        var id:Long = 0,
        var description:String,
        var job:String,
        var email:String):Parcelable