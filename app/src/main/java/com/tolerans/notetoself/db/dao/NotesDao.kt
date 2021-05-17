package com.tolerans.notetoself.db.dao

import androidx.room.*
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.db.entities.User
import com.tolerans.notetoself.db.entities.UserWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg user: User)

    @Update
    suspend fun updateUsers(vararg user: User)

    @Delete
    suspend fun deleteUsers(vararg user: User)

    @Query("SELECT * FROM users WHERE userID = :id")
     fun loadUserById(id:Int):Flow<User>

    @Query("SELECT * FROM users")
      fun getAllUsers(): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM users")
     fun getUsersWithNotes():Flow<List<UserWithNotes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(vararg note: Note)

    @Update
    suspend fun updateNotes(vararg note: Note)

    @Delete
    suspend fun deleteNotes(vararg note: Note)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE noteID = :id")
     fun loadNoteById(id:Int):Flow<Note>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
     fun loadUserByUsernameAndPassword(username:String,password:String):Flow<User>

     @Query("SELECT * FROM notes WHERE status = :status")
     fun getFilterNotes(status:Int) :Flow<List<Note>>

}