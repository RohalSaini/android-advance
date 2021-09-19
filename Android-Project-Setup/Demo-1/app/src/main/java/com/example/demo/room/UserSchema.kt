package com.example.demo.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "userData")
@Parcelize
data class UserSchema (
        var username:String,
        var password:String,
        @PrimaryKey
        var email:String):Parcelable