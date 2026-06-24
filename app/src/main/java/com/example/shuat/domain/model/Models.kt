package com.example.shuat.domain.model

/**
 * 题目类型
 */
enum class QuestionType {
    SINGLE_CHOICE,    // 单选题
    MULTIPLE_CHOICE,  // 多选题
    TRUE_FALSE,       // 判断题
    FILL_BLANK,       // 填空题
    SHORT_ANSWER      // 简答题
}

/**
 * 题目难度
 */
enum class DifficultyLevel {
    EASY,      // 简单
    MEDIUM,    // 中等
    HARD       // 困难
}

/**
 * 题目领域模型
 */
data class Question(
    val id: String,
    val type: QuestionType,
    val category: String,
    val difficulty: DifficultyLevel,
    val tags: List<String>,
    val question: String,
    val options: List<String>,
    val answer: String,
    val explanation: String,
    val isFavorite: Boolean = false,
    val isWrong: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 答题记录
 */
data class AnswerRecord(
    val id: Long = 0,
    val questionId: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val timeSpent: Long, // 答题耗时（秒）
    val answeredAt: Long = System.currentTimeMillis()
)

/**
 * 统计数据
 */
data class Statistics(
    val totalQuestions: Int,
    val answeredQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val favoriteCount: Int,
    val wrongQuestionCount: Int,
    val accuracy: Float,
    val averageTimeSpent: Long
)

/**
 * 分类统计
 */
data class CategoryStatistics(
    val category: String,
    val totalQuestions: Int,
    val answeredQuestions: Int,
    val correctAnswers: Int,
    val accuracy: Float
)
