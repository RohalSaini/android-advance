package com.example.demo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserSchema::class,TodoSchema::class],version = 2,exportSchema = false)
abstract class Db :RoomDatabase() {

    abstract fun userSchemaDao():UserSchemaDAO
    abstract fun todoSchemaDao():TodoSchemaDAO

    companion object {
        @Volatile
        private var INSTANCE:Db? = null

        fun getDataBase(context:Context):Db {
            val tempInstance = INSTANCE
            if(tempInstance !=null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Db::class.java,
                    "todo_8"
                ).build()
                INSTANCE = instance
                return INSTANCE!!
            }
        }
    }
}