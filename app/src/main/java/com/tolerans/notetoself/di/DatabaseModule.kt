package com.tolerans.notetoself.di

import android.content.Context
import androidx.room.Room
import com.tolerans.notetoself.db.dao.NotesDao
import com.tolerans.notetoself.db.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesNotesDao(notesDatabase: NotesDatabase): NotesDao = notesDatabase.notesDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context):NotesDatabase{
        return Room.databaseBuilder(
            applicationContext,
        NotesDatabase::class.java,
        NotesDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}