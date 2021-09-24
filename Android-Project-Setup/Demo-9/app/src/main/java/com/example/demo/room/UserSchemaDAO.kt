package com.example.demo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSchemaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserSchema)


    @Query("SELECT * FROM userData order by username ")
    fun readAllData():LiveData<List<UserSchema>>

    @Query("SELECT * FROM userData WHERE email LIKE :email")
    fun getUserByEmail(email:String): LiveData<UserSchema>
}