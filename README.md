# 刷题 APP

一个简洁、高效的开源安卓刷题应用，帮助你随时随地巩固知识。

## ✨ 特性

- 📚 **多题型支持** - 单选、多选、判断、填空、简答题
- 🎯 **智能分类** - 按知识点、难度、标签分类刷题
- 🎲 **随机练习** - 随机抽题，打破记忆惯性
- ❌ **错题本** - 自动收集错题，针对性复习
- ⭐ **收藏功能** - 收藏重点题目
- 📊 **数据统计** - 答题记录、正确率分析
- 💾 **本地存储** - 数据本地保存，无需网络
- 📥 **题库导入** - 支持 JSON 格式批量导入题目

## 🛠️ 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **架构**: MVVM + Clean Architecture
- **数据库**: Room
- **依赖注入**: Hilt
- **异步处理**: Coroutines + Flow
- **构建工具**: Gradle (Kotlin DSL)

## 📱 截图

_截图待补充_

## 🚀 快速开始

### 环境要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- JDK 17
- Android SDK 34
- Kotlin 1.9.0+

### 构建项目

1. 克隆仓库
```bash
git clone https://github.com/yourusername/shuat.git
cd shuat
```

2. 用 Android Studio 打开项目

3. 同步 Gradle 依赖

4. 运行应用（最低支持 Android 7.0，API 24）

## 📂 项目结构

```
app/
├── data/              # 数据层
│   ├── local/        # 本地数据源（Room）
│   ├── repository/   # 仓库实现
│   └── model/        # 数据模型
├── domain/           # 业务逻辑层
│   ├── model/        # 领域模型
│   ├── repository/   # 仓库接口
│   └── usecase/      # 用例
├── presentation/     # 展示层
│   ├── home/         # 首页
│   ├── practice/     # 练习模块
│   ├── review/       # 复习模块（错题本/收藏）
│   ├── statistics/   # 统计模块
│   └── settings/     # 设置模块
└── di/               # 依赖注入配置
```

## 📖 题库格式

支持 JSON 格式导入题目，示例格式：

```json
{
  "questions": [
    {
      "id": "1",
      "type": "single_choice",
      "category": "Android",
      "difficulty": "medium",
      "tags": ["Activity", "生命周期"],
      "question": "Activity 的 onCreate() 方法在什么时候被调用？",
      "options": [
        "Activity 第一次创建时",
        "Activity 从后台返回前台时",
        "Activity 被销毁时",
        "Activity 暂停时"
      ],
      "answer": "0",
      "explanation": "onCreate() 在 Activity 第一次创建时调用，用于初始化操作。"
    }
  ]
}
```

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

## 🙏 致谢

感谢所有贡献者的付出！

---

**Star ⭐ 这个项目如果它对你有帮助！**
