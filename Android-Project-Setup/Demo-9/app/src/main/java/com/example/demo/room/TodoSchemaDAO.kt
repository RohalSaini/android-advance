package com.example.demo.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoSchemaDAO {

    @Insert
    suspend fun addTodo(todo: TodoSchema)

    @Query("SELECT * FROM todo order by id ")
    fun readAllData():LiveData<List<TodoSchema>>


    @Query("SELECT * FROM todo WHERE email LIKE :email LIMIT :limit OFFSET :index")
    fun getTodoByEmail(email:String,index:Int,limit:Int?=4): LiveData<List<TodoSchema>>

    @Delete
    fun deleteUsers(todo: TodoSchema)

    @Update
    fun updateUsers( todo:TodoSchema)

}