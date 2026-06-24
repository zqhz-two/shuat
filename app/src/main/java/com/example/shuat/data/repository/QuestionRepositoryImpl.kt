package com.example.shuat.data.repository

import com.example.shuat.data.local.dao.AnswerRecordDao
import com.example.shuat.data.local.dao.QuestionDao
import com.example.shuat.data.local.entity.AnswerRecordEntity
import com.example.shuat.data.local.entity.toDomainModel
import com.example.shuat.data.local.entity.toEntity
import com.example.shuat.domain.model.AnswerRecord
import com.example.shuat.domain.model.CategoryStatistics
import com.example.shuat.domain.model.DifficultyLevel
import com.example.shuat.domain.model.Question
import com.example.shuat.domain.model.QuestionType
import com.example.shuat.domain.model.Statistics
import com.example.shuat.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 题目仓库实现
 */
@Singleton
class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val answerRecordDao: AnswerRecordDao
) : QuestionRepository {

    override suspend fun insertQuestion(question: Question) {
        questionDao.insertQuestion(question.toEntity())
    }

    override suspend fun insertQuestions(questions: List<Question>) {
        questionDao.insertQuestions(questions.map { it.toEntity() })
    }

    override suspend fun deleteQuestion(questionId: String) {
        questionDao.deleteQuestionById(questionId)
    }

    override suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question.toEntity())
    }

    override fun getQuestionById(questionId: String): Flow<Question?> {
        return questionDao.getQuestionById(questionId).map { it?.toDomainModel() }
    }

    override fun getAllQuestions(): Flow<List<Question>> {
        return questionDao.getAllQuestions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getQuestionsByCategory(category: String): Flow<List<Question>> {
        return questionDao.getQuestionsByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getQuestionsByType(type: QuestionType): Flow<List<Question>> {
        return questionDao.getQuestionsByType(type.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getQuestionsByDifficulty(difficulty: DifficultyLevel): Flow<List<Question>> {
        return questionDao.getQuestionsByDifficulty(difficulty.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getQuestionsByTag(tag: String): Flow<List<Question>> {
        return questionDao.getQuestionsByTag(tag).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getFavoriteQuestions(): Flow<List<Question>> {
        return questionDao.getFavoriteQuestions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getWrongQuestions(): Flow<List<Question>> {
        return questionDao.getWrongQuestions().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getRandomQuestions(count: Int): Flow<List<Question>> {
        return questionDao.getRandomQuestions(count).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun toggleFavorite(questionId: String) {
        questionDao.toggleFavorite(questionId)
    }

    override suspend fun markAsWrong(questionId: String) {
        questionDao.markAsWrong(questionId)
    }

    override suspend fun removeFromWrong(questionId: String) {
        questionDao.removeFromWrong(questionId)
    }

    override fun searchQuestions(query: String): Flow<List<Question>> {
        return questionDao.searchQuestions(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAllCategories(): Flow<List<String>> {
        return questionDao.getAllCategories()
    }

    override fun getAllTags(): Flow<List<String>> {
        return questionDao.getAllTagsRaw().map { tagStrings ->
            tagStrings.flatMap { it.split(",") }
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
        }
    }

    override suspend fun insertAnswerRecord(record: AnswerRecord) {
        val entity = AnswerRecordEntity(
            id = record.id,
            questionId = record.questionId,
            userAnswer = record.userAnswer,
            correctAnswer = record.correctAnswer,
            isCorrect = record.isCorrect,
            timeSpent = record.timeSpent,
            answeredAt = record.answeredAt
        )
        answerRecordDao.insertAnswerRecord(entity)
    }

    override fun getAnswerRecordsByQuestion(questionId: String): Flow<List<AnswerRecord>> {
        return answerRecordDao.getAnswerRecordsByQuestion(questionId).map { entities ->
            entities.map { entity ->
                AnswerRecord(
                    id = entity.id,
                    questionId = entity.questionId,
                    userAnswer = entity.userAnswer,
                    correctAnswer = entity.correctAnswer,
                    isCorrect = entity.isCorrect,
                    timeSpent = entity.timeSpent,
                    answeredAt = entity.answeredAt
                )
            }
        }
    }

    override fun getAllAnswerRecords(): Flow<List<AnswerRecord>> {
        return answerRecordDao.getAllAnswerRecords().map { entities ->
            entities.map { entity ->
                AnswerRecord(
                    id = entity.id,
                    questionId = entity.questionId,
                    userAnswer = entity.userAnswer,
                    correctAnswer = entity.correctAnswer,
                    isCorrect = entity.isCorrect,
                    timeSpent = entity.timeSpent,
                    answeredAt = entity.answeredAt
                )
            }
        }
    }

    override suspend fun deleteAllAnswerRecords() {
        answerRecordDao.deleteAllAnswerRecords()
    }

    override fun getStatistics(): Flow<Statistics> {
        return kotlinx.coroutines.flow.flow {
            val totalQuestions = questionDao.getQuestionCount()
            val answeredQuestions = answerRecordDao.getAnsweredQuestionCount()
            val correctAnswers = answerRecordDao.getCorrectAnswerCount()
            val wrongAnswers = answerRecordDao.getWrongAnswerCount()
            val favoriteCount = questionDao.getFavoriteCount()
            val wrongQuestionCount = questionDao.getWrongQuestionCount()
            val averageTimeSpent = answerRecordDao.getAverageTimeSpent() ?: 0L
            val accuracy = if (answeredQuestions > 0) {
                correctAnswers.toFloat() / (correctAnswers + wrongAnswers)
            } else 0f

            emit(
                Statistics(
                    totalQuestions = totalQuestions,
                    answeredQuestions = answeredQuestions,
                    correctAnswers = correctAnswers,
                    wrongAnswers = wrongAnswers,
                    favoriteCount = favoriteCount,
                    wrongQuestionCount = wrongQuestionCount,
                    accuracy = accuracy,
                    averageTimeSpent = averageTimeSpent
                )
            )
        }
    }

    override fun getCategoryStatistics(): Flow<List<CategoryStatistics>> {
        return questionDao.getAllCategories().map { categories ->
            categories.map { category ->
                // 这里简化处理，实际可以通过更复杂的查询获取每个分类的统计
                CategoryStatistics(
                    category = category,
                    totalQuestions = 0,
                    answeredQuestions = 0,
                    correctAnswers = 0,
                    accuracy = 0f
                )
            }
        }
    }
}
