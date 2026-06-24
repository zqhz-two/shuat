package com.example.shuat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shuat.domain.model.DifficultyLevel
import com.example.shuat.domain.model.Question
import com.example.shuat.domain.model.QuestionType

/**
 * 题目数据库实体
 */
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val category: String,
    val difficulty: String,
    val tags: String, // 用逗号分隔
    val question: String,
    val options: String, // JSON 数组字符串
    val answer: String,
    val explanation: String,
    val isFavorite: Boolean = false,
    val isWrong: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 答题记录实体
 */
@Entity(tableName = "answer_records")
data class AnswerRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionId: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val timeSpent: Long,
    val answeredAt: Long = System.currentTimeMillis()
)

// 扩展函数：实体 -> 领域模型
fun QuestionEntity.toDomainModel(): Question {
    return Question(
        id = id,
        type = QuestionType.valueOf(type),
        category = category,
        difficulty = DifficultyLevel.valueOf(difficulty),
        tags = tags.split(",").filter { it.isNotBlank() },
        question = question,
        options = com.google.gson.Gson().fromJson(options, Array<String>::class.java).toList(),
        answer = answer,
        explanation = explanation,
        isFavorite = isFavorite,
        isWrong = isWrong,
        createdAt = createdAt
    )
}

// 扩展函数：领域模型 -> 实体
fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        type = type.name,
        category = category,
        difficulty = difficulty.name,
        tags = tags.joinToString(","),
        question = question,
        options = com.google.gson.Gson().toJson(options),
        answer = answer,
        explanation = explanation,
        isFavorite = isFavorite,
        isWrong = isWrong,
        createdAt = createdAt
    )
}
