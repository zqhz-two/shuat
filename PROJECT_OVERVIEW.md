# 项目总览

## 📋 项目信息

- **项目名称**：刷题 APP
- **版本**：1.0.0
- **最低 Android 版本**：7.0 (API 24)
- **目标 Android 版本**：14 (API 34)
- **开发语言**：Kotlin
- **UI 框架**：Jetpack Compose
- **架构模式**：MVVM + Clean Architecture

## 🎯 核心功能

### 已实现功能 ✅

1. **题库管理**
   - 支持多种题型（单选、多选、判断、填空、简答）
   - 题目分类管理
   - 标签系统
   - 难度分级

2. **练习模式**
   - 随机练习
   - 分类练习
   - 实时答题反馈
   - 答案解析

3. **复习功能**
   - 错题本自动收集
   - 收藏夹管理
   - 一键移除已掌握题目

4. **数据统计**
   - 总体学习进度
   - 正确率统计
   - 分类统计
   - 平均答题时间

5. **数据持久化**
   - Room 数据库存储
   - 答题记录保存
   - 本地数据管理

### 待实现功能 🚧

1. **题目导入**
   - UI 文件选择器
   - 批量导入题目
   - 导入进度提示

2. **题目编辑**
   - 在线编辑题目
   - 自定义题目创建
   - 题目删除功能

3. **高级功能**
   - 题目搜索
   - 学习计划
   - 每日目标
   - 学习提醒
   - 数据导出/备份

4. **社交功能**
   - 题库分享
   - 学习排行榜
   - 题目评论

5. **在线功能**
   - 云端题库同步
   - 多设备数据同步
   - 题库订阅

## 📁 项目结构

```
shuat/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/shuat/
│   │       │   ├── data/                 # 数据层
│   │       │   │   ├── local/           # 本地数据源
│   │       │   │   │   ├── dao/         # 数据访问对象
│   │       │   │   │   ├── entity/      # 数据库实体
│   │       │   │   │   └── ShuaTiDatabase.kt
│   │       │   │   └── repository/      # 仓库实现
│   │       │   ├── domain/              # 领域层
│   │       │   │   ├── model/           # 领域模型
│   │       │   │   └── repository/      # 仓库接口
│   │       │   ├── presentation/        # 展示层
│   │       │   │   ├── home/            # 首页
│   │       │   │   ├── practice/        # 练习
│   │       │   │   ├── review/          # 复习
│   │       │   │   ├── statistics/      # 统计
│   │       │   │   ├── navigation/      # 导航
│   │       │   │   └── theme/           # 主题
│   │       │   ├── di/                  # 依赖注入
│   │       │   ├── MainActivity.kt
│   │       │   └── ShuaTiApplication.kt
│   │       ├── res/                     # 资源文件
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── sample_questions.json              # 示例题目
├── README.md
├── CONTRIBUTING.md
└── LICENSE
```

## 🔧 技术栈详情

### 核心框架
- **Kotlin 1.9.22** - 现代化编程语言
- **Jetpack Compose** - 声明式 UI 框架
- **Material Design 3** - 现代化设计系统

### 架构组件
- **Room 2.6.1** - SQLite ORM 数据库
- **Hilt 2.50** - 依赖注入框架
- **Navigation Compose** - 导航组件
- **ViewModel** - 生命周期感知的状态管理
- **Coroutines & Flow** - 异步编程

### 工具库
- **Gson** - JSON 解析
- **DataStore** - 键值对存储（预留）

## 🚀 快速开始

### 1. 环境准备

确保已安装：
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34

### 2. 导入项目

```bash
git clone <your-repo-url>
cd shuat
```

在 Android Studio 中打开项目，等待 Gradle 同步完成。

### 3. 导入示例数据

项目根目录有 `sample_questions.json` 示例题库文件，包含 10 道题目：
- 7 道 Android 相关题目
- 3 道 Kotlin 相关题目
- 涵盖简单、中等、困难三个难度

**导入方式（开发阶段）：**

在 `ShuaTiApplication.onCreate()` 中添加初始化代码：

```kotlin
class ShuaTiApplication : Application() {
    @Inject
    lateinit var repository: QuestionRepository
    
    override fun onCreate() {
        super.onCreate()
        // 首次运行时导入示例数据
        importSampleData()
    }
    
    private fun importSampleData() {
        CoroutineScope(Dispatchers.IO).launch {
            // 检查是否已有数据
            val count = repository.getAllQuestions().first().size
            if (count == 0) {
                // 从 assets 读取并导入
                // 实现代码...
            }
        }
    }
}
```

### 4. 运行应用

点击 Run 按钮或按 `Shift + F10`，选择模拟器或真机运行。

## 📱 功能截图预览位置

建议添加以下截图到 `screenshots/` 目录：
- `home.png` - 首页
- `practice.png` - 练习页面
- `review.png` - 复习页面
- `statistics.png` - 统计页面
- `question_correct.png` - 答对时的界面
- `question_wrong.png` - 答错时的界面

## 🎨 设计规范

### 颜色方案
- **主色调**：`#3C5A78` (沉稳的蓝灰色)
- **背景色**：`#F7F5F1` (温暖的米白色)
- **表面色**：`#FFFFFF` (纯白)
- **成功色**：`#10B981` (绿色)
- **错误色**：`#EF4444` (红色)

### 字体
- **标题**：Serif 字体（Playfair Display）
- **正文/UI**：Sans-serif 字体（Inter）

### 圆角
- 小组件：8dp
- 卡片：12dp
- 大容器：16dp

## 📊 数据模型

### Question（题目）
```kotlin
data class Question(
    val id: String,              // 唯一标识
    val type: QuestionType,      // 题型
    val category: String,        // 分类
    val difficulty: DifficultyLevel,  // 难度
    val tags: List<String>,      // 标签
    val question: String,        // 题目内容
    val options: List<String>,   // 选项
    val answer: String,          // 答案
    val explanation: String,     // 解析
    val isFavorite: Boolean,     // 是否收藏
    val isWrong: Boolean         // 是否为错题
)
```

### AnswerRecord（答题记录）
```kotlin
data class AnswerRecord(
    val id: Long,
    val questionId: String,
    val userAnswer: String,      // 用户答案
    val correctAnswer: String,   // 正确答案
    val isCorrect: Boolean,      // 是否正确
    val timeSpent: Long,         // 答题耗时（秒）
    val answeredAt: Long         // 答题时间戳
)
```

## 🧪 测试

```bash
# 运行单元测试
./gradlew test

# 运行 UI 测试
./gradlew connectedAndroidTest
```

## 📦 构建

```bash
# Debug 版本
./gradlew assembleDebug

# Release 版本
./gradlew assembleRelease
```

## 🤝 参与贡献

欢迎提交 Issue 和 Pull Request！详见 [CONTRIBUTING.md](CONTRIBUTING.md)

## 📄 开源协议

本项目采用 MIT License，详见 [LICENSE](LICENSE)

## 🔗 相关链接

- [Android 开发文档](https://developer.android.com)
- [Jetpack Compose 文档](https://developer.android.com/jetpack/compose)
- [Kotlin 文档](https://kotlinlang.org/docs/home.html)
- [Material Design 3](https://m3.material.io)

## 📮 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 GitHub Issue
- 邮件：your-email@example.com

---

**开始你的刷题之旅吧！** 🎓
