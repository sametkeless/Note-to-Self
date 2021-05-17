package com.tolerans.notetoself.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithNotes(
    @Embedded val user:User,
    @Relation(
        parentColumn = "userID",
        entityColumn = "noteID"
    )
    val notes:List<Note>
    )