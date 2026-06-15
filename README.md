# 💰 Expense Tracker App

A personal finance Android application built with **Java** and **SQLite** that helps users manage their income and expenses efficiently with a clean, modern UI.

---

## 📱 Screenshots

> Home Screen | Add Transaction | Statistics

*(Add your screenshots here)*

---

## ✨ Features

- 🔐 **User Authentication** — Register & Login with SQLite-backed credential storage
- 💵 **Income Tracking** — Add income with amount and date
- 🛒 **Expense Tracking** — Add expenses across multiple categories
- 📊 **Statistics Screen** — Interactive line chart showing spending trends
- 🏆 **Top Spending** — View your biggest expenses at a glance
- 🌙 **Smart Greeting** — Dynamic greeting based on time of day
- 🗑️ **Clear Transactions** — Reset all data with one tap
- 💾 **Remember Me** — Stay logged in across app sessions
- 📱 **Responsive UI** — ConstraintLayout-based design that fits all screen sizes

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| Database | SQLite |
| UI Layout | ConstraintLayout |
| Architecture | Activity-based |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 34 |

---

## 📂 Project Structure

```
app/src/main/
├── java/com/example/expensetracker/
│   ├── LoginActivity.java          # Login screen
│   ├── RegisterActivity.java       # Register screen
│   ├── MainActivity.java           # Home screen
│   ├── AddExpenseActivity.java     # Add income/expense
│   ├── StatisticsActivity.java     # Charts & top spending
│   ├── DatabaseHelper.java         # SQLite operations
│   ├── Transaction.java            # Transaction model
│   └── TransactionAdapter.java     # RecyclerView adapter
├── res/
│   ├── layout/
│   │   ├── activity_login.xml
│   │   ├── activity_register.xml
│   │   ├── activity_main.xml
│   │   ├── activity_add_expense.xml
│   │   ├── activity_statistics.xml
│   │   └── item_transaction.xml
│   ├── drawable/                   # Icons & backgrounds
│   └── values/
│       ├── colors.xml
│       ├── strings.xml
│       └── themes.xml
└── AndroidManifest.xml
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version)
- JDK 8 or higher
- Android device or emulator (API 24+)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/expense-tracker.git
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select the cloned folder
   ```

3. **Add JitPack to `settings.gradle`**
   ```gradle
   dependencyResolutionManagement {
       repositories {
           google()
           mavenCentral()
           maven { url 'https://jitpack.io' }
       }
   }
   ```

4. **Sync Gradle and Run**
   ```
   Click Sync Now → Run on emulator or device
   ```

---

## 📦 Dependencies

```gradle
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
```

---

## 🔑 App Flow

```
Launch
  ↓
LoginActivity ──── Remember Me ────→ MainActivity
  ↓
RegisterActivity → Create Account → LoginActivity
  ↓
MainActivity (Home)
  ├── FAB (+) → AddExpenseActivity (Expense / Income tabs)
  ├── See All / Stats icon → StatisticsActivity
  └── 3 Dots Menu
        ├── Clear → Confirm → Delete all transactions
        └── Logout → Confirm → Back to LoginActivity
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Sohail Sheikh**
- GitHub: [@sheikh-sohail](https://github.com/sheikh-sohail)

---

⭐ If you found this project helpful, please give it a star!
