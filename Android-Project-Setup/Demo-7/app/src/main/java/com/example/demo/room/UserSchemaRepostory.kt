package com.example.demo.room

    import androidx.lifecycle.LiveData
    import kotlinx.coroutines.flow.Flow

class UserSchemaRepostory(private val userSchemaDAO: UserSchemaDAO) {

        val readAllData: LiveData<List<UserSchema>> = userSchemaDAO.readAllData()

        suspend fun addUser(user: UserSchema) {
            userSchemaDAO.addUser(user)
        }

        fun findUserByEmail(email:String): LiveData<UserSchema> {
            return userSchemaDAO.getUserByEmail(email=email)
        }
    }