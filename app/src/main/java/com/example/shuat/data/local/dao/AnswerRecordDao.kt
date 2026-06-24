package com.example.shuat.data.local.dao

import androidx.room.*
import com.example.shuat.data.local.entity.AnswerRecordEntity
import kotlinx.coroutines.flow.Flow

/**
 * 答题记录数据访问对象
 */
@Dao
interface AnswerRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswerRecord(record: AnswerRecordEntity)

    @Query("SELECT * FROM answer_records WHERE questionId = :questionId ORDER BY answeredAt DESC")
    fun getAnswerRecordsByQuestion(questionId: String): Flow<List<AnswerRecordEntity>>

    @Query("SELECT * FROM answer_records ORDER BY answeredAt DESC")
    fun getAllAnswerRecords(): Flow<List<AnswerRecordEntity>>

    @Query("DELETE FROM answer_records")
    suspend fun deleteAllAnswerRecords()

    @Query("SELECT COUNT(*) FROM answer_records")
    suspend fun getAnswerRecordCount(): Int

    @Query("SELECT COUNT(*) FROM answer_records WHERE isCorrect = 1")
    suspend fun getCorrectAnswerCount(): Int

    @Query("SELECT COUNT(*) FROM answer_records WHERE isCorrect = 0")
    suspend fun getWrongAnswerCount(): Int

    @Query("SELECT AVG(timeSpent) FROM answer_records")
    suspend fun getAverageTimeSpent(): Long?

    @Query("SELECT COUNT(DISTINCT questionId) FROM answer_records")
    suspend fun getAnsweredQuestionCount(): Int
}
