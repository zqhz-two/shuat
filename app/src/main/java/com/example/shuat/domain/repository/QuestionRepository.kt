package com.example.shuat.domain.repository

import com.example.shuat.domain.model.AnswerRecord
import com.example.shuat.domain.model.CategoryStatistics
import com.example.shuat.domain.model.DifficultyLevel
import com.example.shuat.domain.model.Question
import com.example.shuat.domain.model.QuestionType
import com.example.shuat.domain.model.Statistics
import kotlinx.coroutines.flow.Flow

/**
 * 题目仓库接口
 */
interface QuestionRepository {
    // 题目相关
    suspend fun insertQuestion(question: Question)
    suspend fun insertQuestions(questions: List<Question>)
    suspend fun deleteQuestion(questionId: String)
    suspend fun updateQuestion(question: Question)
    fun getQuestionById(questionId: String): Flow<Question?>
    fun getAllQuestions(): Flow<List<Question>>
    fun getQuestionsByCategory(category: String): Flow<List<Question>>
    fun getQuestionsByType(type: QuestionType): Flow<List<Question>>
    fun getQuestionsByDifficulty(difficulty: DifficultyLevel): Flow<List<Question>>
    fun getQuestionsByTag(tag: String): Flow<List<Question>>
    fun getFavoriteQuestions(): Flow<List<Question>>
    fun getWrongQuestions(): Flow<List<Question>>
    fun getRandomQuestions(count: Int): Flow<List<Question>>
    suspend fun toggleFavorite(questionId: String)
    suspend fun markAsWrong(questionId: String)
    suspend fun removeFromWrong(questionId: String)
    fun searchQuestions(query: String): Flow<List<Question>>
    fun getAllCategories(): Flow<List<String>>
    fun getAllTags(): Flow<List<String>>

    // 答题记录相关
    suspend fun insertAnswerRecord(record: AnswerRecord)
    fun getAnswerRecordsByQuestion(questionId: String): Flow<List<AnswerRecord>>
    fun getAllAnswerRecords(): Flow<List<AnswerRecord>>
    suspend fun deleteAllAnswerRecords()

    // 统计相关
    fun getStatistics(): Flow<Statistics>
    fun getCategoryStatistics(): Flow<List<CategoryStatistics>>
}
