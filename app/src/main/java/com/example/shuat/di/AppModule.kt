package com.example.shuat.di

import android.content.Context
import androidx.room.Room
import com.example.shuat.data.local.ShuaTiDatabase
import com.example.shuat.data.local.dao.AnswerRecordDao
import com.example.shuat.data.local.dao.QuestionDao
import com.example.shuat.data.repository.QuestionRepositoryImpl
import com.example.shuat.domain.repository.QuestionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 应用级依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShuaTiDatabase {
        return Room.databaseBuilder(
            context,
            ShuaTiDatabase::class.java,
            "shuat_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideQuestionDao(database: ShuaTiDatabase): QuestionDao {
        return database.questionDao()
    }

    @Provides
    @Singleton
    fun provideAnswerRecordDao(database: ShuaTiDatabase): AnswerRecordDao {
        return database.answerRecordDao()
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(
        questionDao: QuestionDao,
        answerRecordDao: AnswerRecordDao
    ): QuestionRepository {
        return QuestionRepositoryImpl(questionDao, answerRecordDao)
    }
}
