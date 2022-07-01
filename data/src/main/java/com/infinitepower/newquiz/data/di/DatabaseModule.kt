package com.infinitepower.newquiz.data.di

import android.content.Context
import androidx.room.Room
import com.infinitepower.newquiz.data.database.AppDatabase
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "app-database"
    ).build()

    @Provides
    @Singleton
    fun provideSavedQuestionsDao(
        appDatabase: AppDatabase
    ): SavedQuestionsDao = appDatabase.savedQuestionsDao()
}