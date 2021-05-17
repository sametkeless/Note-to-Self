package com.tolerans.notetoself.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tolerans.notetoself.db.dao.NotesDao
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.db.entities.User

@Database(entities = arrayOf(Note::class, User::class) ,version = 1,exportSchema = false)
abstract class NotesDatabase :RoomDatabase(){
    abstract fun notesDao(): NotesDao

    companion object {
        val DATABASE_NAME = "note_db"
    }
}