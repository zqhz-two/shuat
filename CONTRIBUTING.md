# 开发指南

## 项目架构

本项目采用 **Clean Architecture** + **MVVM** 架构模式：

```
presentation (UI层)
    ↓
domain (业务逻辑层)
    ↓
data (数据层)
```

### 各层职责

**Presentation 层**
- UI 组件（Jetpack Compose）
- ViewModel（状态管理）
- Navigation（导航）

**Domain 层**
- 业务模型（Model）
- 仓库接口（Repository Interface）
- 用例（Use Case，可选）

**Data 层**
- 数据源（Room Database）
- 仓库实现（Repository Implementation）
- 数据实体（Entity）

## 添加新功能

### 1. 添加新的题目类型

**步骤：**

1. 在 `domain/model/Models.kt` 中添加新的 `QuestionType` 枚举值
2. 更新 `presentation/practice/PracticeScreen.kt` 中的题目渲染逻辑
3. 如需特殊 UI，创建对应的 Composable 组件

**示例：添加填空题**

```kotlin
// 1. 添加枚举
enum class QuestionType {
    // ...
    FILL_BLANK  // 新增
}

// 2. 在 PracticeScreen 中添加处理
if (question.type == QuestionType.FILL_BLANK) {
    FillBlankInput(
        onAnswerChanged = { viewModel.selectAnswer(it) }
    )
}
```

### 2. 添加新的统计维度

1. 在 `domain/model/Models.kt` 中定义新的统计模型
2. 在 `QuestionRepository` 接口中添加新的查询方法
3. 在 `QuestionRepositoryImpl` 中实现该方法
4. 在 `StatisticsViewModel` 中加载数据
5. 在 `StatisticsScreen` 中展示

### 3. 添加题目导入功能

当前项目支持通过 JSON 文件导入题目，要添加 UI 导入功能：

```kotlin
// 在 ViewModel 中添加
fun importQuestions(json: String) {
    viewModelScope.launch {
        try {
            val questionData = Gson().fromJson(json, QuestionData::class.java)
            repository.insertQuestions(questionData.questions)
        } catch (e: Exception) {
            // 处理错误
        }
    }
}
```

## 数据库操作

### 查询题目

```kotlin
// 获取所有题目
repository.getAllQuestions().collect { questions ->
    // 处理题目列表
}

// 按分类查询
repository.getQuestionsByCategory("Android").collect { questions ->
    // 处理分类题目
}

// 随机抽题
repository.getRandomQuestions(20).collect { questions ->
    // 处理随机题目
}
```

### 添加题目

```kotlin
val question = Question(
    id = UUID.randomUUID().toString(),
    type = QuestionType.SINGLE_CHOICE,
    category = "Android",
    difficulty = DifficultyLevel.MEDIUM,
    tags = listOf("Activity", "生命周期"),
    question = "题目内容",
    options = listOf("选项1", "选项2", "选项3", "选项4"),
    answer = "0",
    explanation = "解析内容"
)

repository.insertQuestion(question)
```

### 记录答题

```kotlin
val record = AnswerRecord(
    questionId = question.id,
    userAnswer = "0",
    correctAnswer = "0",
    isCorrect = true,
    timeSpent = 30 // 秒
)

repository.insertAnswerRecord(record)
```

## UI 组件规范

### 颜色使用

```kotlin
// 主题色
MaterialTheme.colorScheme.primary        // 主色调
MaterialTheme.colorScheme.secondary      // 次要色

// 状态色
Success  // 成功/正确
Error    // 错误/失败
Warning  // 警告
Info     // 信息

// 背景和表面
MaterialTheme.colorScheme.background     // 页面背景
MaterialTheme.colorScheme.surface        // 卡片表面
```

### 间距规范

```kotlin
// 标准间距
4.dp   // 极小间距
8.dp   // 小间距
12.dp  // 中小间距
16.dp  // 标准间距（常用）
20.dp  // 中等间距
24.dp  // 大间距
32.dp  // 很大间距
```

### 圆角规范

```kotlin
8.dp   // 小圆角（按钮、小卡片）
12.dp  // 标准圆角（卡片）
16.dp  // 大圆角（特殊组件）
```

## 测试

### 单元测试

```kotlin
@Test
fun `test question repository`() = runTest {
    val question = createTestQuestion()
    repository.insertQuestion(question)
    
    val result = repository.getQuestionById(question.id).first()
    assertEquals(question, result)
}
```

### UI 测试

```kotlin
@Test
fun homeScreen_displaysStatistics() {
    composeTestRule.setContent {
        HomeScreen(
            onNavigateToPractice = {},
            onNavigateToReview = {},
            onNavigateToStatistics = {}
        )
    }
    
    composeTestRule.onNodeWithText("刷题").assertIsDisplayed()
}
```

## 性能优化建议

1. **使用 Flow 而不是 LiveData**：更好的协程支持
2. **合理使用 remember 和 derivedStateOf**：避免不必要的重组
3. **LazyColumn 使用 key**：提高列表性能
4. **数据库操作使用索引**：加快查询速度
5. **图片加载使用 Coil**：如需添加图片功能

## 发布流程

### 1. 更新版本号

在 `app/build.gradle.kts` 中：

```kotlin
versionCode = 2
versionName = "1.1.0"
```

### 2. 生成签名密钥

```bash
keytool -genkey -v -keystore release.keystore -alias shuat -keyalg RSA -keysize 2048 -validity 10000
```

### 3. 配置签名

创建 `keystore.properties`：

```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=shuat
storeFile=release.keystore
```

### 4. 构建 Release 版本

```bash
./gradlew assembleRelease
```

APK 位置：`app/build/outputs/apk/release/app-release.apk`

## 常见问题

**Q: 如何添加网络题库同步功能？**

A: 
1. 添加 Retrofit 依赖
2. 创建 API 接口
3. 在 Repository 中添加网络数据源
4. 实现本地和远程数据同步逻辑

**Q: 如何支持题目搜索？**

A: 已实现 `searchQuestions(query)` 方法，可在 HomeScreen 添加搜索框调用。

**Q: 如何导出学习数据？**

A: 
1. 从 Room 导出数据为 JSON
2. 使用 Storage Access Framework 保存文件
3. 添加分享功能

## 贡献代码

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 发起 Pull Request

请确保：
- 代码符合 Kotlin 代码规范
- 添加必要的注释
- 通过所有测试
- 更新相关文档
