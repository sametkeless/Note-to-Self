package com.tolerans.notetoself.repository

import com.tolerans.notetoself.db.dao.NotesDao
import com.tolerans.notetoself.db.entities.Note
import com.tolerans.notetoself.db.entities.User
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepository @Inject constructor(val dao: NotesDao){

    suspend fun saveNote(note: Note) = flow{
        try {
            dao.insertNotes(note)
            emit(true)
        }catch (exp:Exception){
            emit(false)
        }
    }

    fun getAllNotes() = dao.getAllNotes()

    fun getFilteredNotes(status:Int) = dao.getFilterNotes(status)

    fun getNoteDetail(noteID:Int) = dao.loadNoteById(noteID)

    suspend fun updateNote(note: Note) = flow{
        try {
            dao.updateNotes(note)
            kotlinx.coroutines.delay(1000)
            emit(true)
        }catch (exp:Exception){
            emit(false)
        }
    }

    suspend fun deleteNote(note: Note) = flow{
        try {
            dao.deleteNotes(note)
            emit(true)
        }catch (exp:Exception){
            emit(false)
        }
    }

}