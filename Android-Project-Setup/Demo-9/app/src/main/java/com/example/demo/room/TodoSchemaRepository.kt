package com.example.demo.room

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TodoSchemaRepository(private val todoSchemaDAO: TodoSchemaDAO) {
    val readAllData:LiveData<List<TodoSchema>> = todoSchemaDAO.readAllData()

    suspend fun addTodo(todo: TodoSchema) {
        todoSchemaDAO.addTodo(todo = todo)
    }

    fun findTodoByEmail(email:String,index:Int,limit:Int =4): LiveData<List<TodoSchema>> {
        return todoSchemaDAO.getTodoByEmail(email=email,index= index,limit=limit)
    }
    fun deleteTodoByID(todo:TodoSchema) {
        todoSchemaDAO.deleteUsers(todo =todo)
    }
    fun updateTodoByID(todo:TodoSchema) {
        todoSchemaDAO.updateUsers(todo = todo)
    }
}