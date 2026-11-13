# 💰 Premium Offline Personal Expense Tracker Android App

A complete, production-grade, offline personal finance management application built for Android using **Kotlin**, **Jetpack Compose**, **Material Design 3**, and **Clean Architecture**.

---

## 🌟 Key Features

- **100% Offline Integrity**: Operates completely without remote backend servers, cloud accounts, or internet connections.
- **Accurate Minor-Units Monetary Math**: All currency calculations are stored as integer minor units (`Long`) to prevent floating-point rounding errors.
- **Custom Interactive Compose Canvas Charts**:
  - **Donut & Pie Charts**: Interactive category expense distribution with touch segment inspection.
  - **Grouped & Stacked Bar Charts**: Monthly/daily income vs. expense comparisons.
  - **Line & Area Charts**: Cumulative financial trend lines with Bezier curve rendering and touch tooltips.
  - **Heatmap Calendar**: Daily spending intensity visualization.
  - **Waterfall Chart**: Cumulative balance flow tracking.
  - **Progress Charts**: Real-time budget utilization and savings goals progress.
- **Multiple Accounts & Transfer Management**: Track cash, bank accounts, savings, credit cards, and digital wallets with atomic balance updates that do not double-count transfers as income or expenses.
- **Budgeting & Rollover**: Set monthly/weekly/yearly limits with visual progress bars and warning notifications at 70%, 80%, 90%, and 100%.
- **Savings Goals**: Target tracking with automated monthly required savings calculation and deposit/withdrawal management.
- **WorkManager Recurring Transactions**: Background auto-generation of due recurring bills (salary, rent, Netflix, subscriptions) with duplicate suppression.
- **Multi-Theme & Color Palette Engine**: Material Design 3 theme system with 4 Theme Modes (Light, Dark, System, AMOLED Black) and 10 dynamic color schemes.
- **Offline Financial Insights**: Automated local spending advice cards (highest spending day, category anomalies, savings rate).
- **Data Export & SAF Backup/Restore**: Export transactions to CSV/JSON and create full offline database backups using Android Storage Access Framework.

---

## 🏗 Architecture & Technology Stack

### Tech Stack
- **Language**: Kotlin 2.2
- **UI Framework**: Jetpack Compose + Material Design 3
- **Architecture**: Clean Architecture (Data, Domain, Presentation) + MVVM
- **Database**: Room Database 2.6
- **Preferences**: DataStore Preferences
- **Dependency Injection**: Hilt (Dagger)
- **Background Jobs**: WorkManager + Hilt Worker
- **Testing**: JUnit4, MockK, Turbine, Kotlinx Coroutines Test

### Clean Architecture Layers
```
com.example.expensetracker/
├── core/              # Common utilities, Theme engine, DataStore, Navigation, Canvas Charts
├── data/              # Room Entities, DAOs, Database, Repository Implementations, Mappers
├── domain/            # Models, Interfaces, Business Use Cases
└── presentation/      # ViewModels, Compose Screens (Dashboard, Transactions, Accounts, Budgets, Reports, etc.)
```

---

## 🚀 Realistic Sample Data Generator

The application includes an optional built-in sample data generator. Users can seed 15+ realistic income, expense, and budget records during first launch to immediately explore filled dashboards and interactive charts, and can reset or clear sample data at any time in **Settings**.

---

## 🛠 Local Setup & Build Instructions

1. Open the project in **Android Studio Ladybug (or newer)**.
2. Select the `app` run configuration.
3. Sync Gradle and run on an Android Device or Emulator running Android 7.0 (API level 24) or higher.
4. Run Unit Tests via IDE test runner or `./gradlew test`.

---

## 📜 License

Licensed under the [MIT License](LICENSE).
