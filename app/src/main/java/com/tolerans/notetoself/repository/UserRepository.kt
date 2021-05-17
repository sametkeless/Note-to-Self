package com.tolerans.notetoself.repository

import com.tolerans.notetoself.util.EncryptionHelper
import com.tolerans.notetoself.db.dao.NotesDao
import com.tolerans.notetoself.db.entities.User
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(var dao: NotesDao, var encryptionHelper: EncryptionHelper){

    suspend fun saveUser(user: User) = flow{
        try {
            var encryptUser = User(username = user.username,password = encryptionHelper.encrypt(user.password)!!)
            dao.insertUsers(encryptUser)
            emit(true)
        }catch (exp:Exception){
            emit(false)
        }
    }

    fun login (loginUser:User) = flow {

            dao.loadUserByUsernameAndPassword(loginUser.username, encryptionHelper.encrypt(loginUser.password)!!)
                .catch {
                    val defaultUser = User(-1, "", "")
                    emit(defaultUser)
                }.collect { user ->
                    if (user == null) {
                        val defaultUser = User(-1, "", "")
                        emit(defaultUser)
                    } else
                        emit(user)
                }

    }

}