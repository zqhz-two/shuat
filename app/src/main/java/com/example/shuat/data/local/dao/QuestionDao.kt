package com.example.shuat.data.local.dao

import androidx.room.*
import com.example.shuat.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * 题目数据访问对象
 */
@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE id = :questionId")
    suspend fun deleteQuestionById(questionId: String)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT * FROM questions WHERE id = :questionId")
    fun getQuestionById(questionId: String): Flow<QuestionEntity?>

    @Query("SELECT * FROM questions ORDER BY createdAt DESC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE category = :category ORDER BY createdAt DESC")
    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE type = :type ORDER BY createdAt DESC")
    fun getQuestionsByType(type: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE difficulty = :difficulty ORDER BY createdAt DESC")
    fun getQuestionsByDifficulty(difficulty: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE tags LIKE '%' || :tag || '%' ORDER BY createdAt DESC")
    fun getQuestionsByTag(tag: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isWrong = 1 ORDER BY createdAt DESC")
    fun getWrongQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :count")
    fun getRandomQuestions(count: Int): Flow<List<QuestionEntity>>

    @Query("UPDATE questions SET isFavorite = NOT isFavorite WHERE id = :questionId")
    suspend fun toggleFavorite(questionId: String)

    @Query("UPDATE questions SET isWrong = 1 WHERE id = :questionId")
    suspend fun markAsWrong(questionId: String)

    @Query("UPDATE questions SET isWrong = 0 WHERE id = :questionId")
    suspend fun removeFromWrong(questionId: String)

    @Query("SELECT * FROM questions WHERE question LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchQuestions(query: String): Flow<List<QuestionEntity>>

    @Query("SELECT DISTINCT category FROM questions ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT DISTINCT tags FROM questions")
    fun getAllTagsRaw(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE isFavorite = 1")
    suspend fun getFavoriteCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE isWrong = 1")
    suspend fun getWrongQuestionCount(): Int
}
