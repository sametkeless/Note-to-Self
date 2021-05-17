package com.tolerans.notetoself.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var noteID:Int = 0,
    var title:String,
    var description:String,
     var noteImage:String? = null,
    var status:Int

)
