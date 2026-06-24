package com.example.shuat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shuat.data.local.dao.AnswerRecordDao
import com.example.shuat.data.local.dao.QuestionDao
import com.example.shuat.data.local.entity.AnswerRecordEntity
import com.example.shuat.data.local.entity.QuestionEntity

/**
 * 应用数据库
 */
@Database(
    entities = [QuestionEntity::class, AnswerRecordEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ShuaTiDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun answerRecordDao(): AnswerRecordDao
}
