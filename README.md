# 💰 Expense Tracker — Premium Offline Personal Finance Android App

A complete, production-grade, 100% offline personal finance management application for Android. Built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, **Dagger Hilt**, **Room Database**, and strict **Clean Architecture** principles.

---

## 🌟 Key Features

### 🔒 100% Offline & Private
- Operates completely without remote backend servers, cloud sync, or external tracking.
- All financial data stays strictly on your local device.

### 🔢 Precise Minor-Units Monetary Math
- All monetary values are represented as integer minor units (`Long`) internally.
- Completely avoids floating-point/double rounding errors during balances, transfers, and aggregation calculations.

### 🎨 Custom Interactive Compose Canvas Charts
Built using 100% pure Jetpack Compose Canvas with zero third-party charting library dependencies:
- **Pie & Donut Charts**: Interactive category expense distribution with touch segment selection.
- **Grouped & Stacked Bar Charts**: Monthly and daily income vs. expense visual comparisons.
- **Line & Area Charts**: Bezier curve rendering with interactive touch tooltips for net worth and spending trends.
- **Heatmap Calendar & Daily Spending Intensity**: GitHub-style activity grid visualizing daily spending levels.
- **Waterfall Chart**: Cumulative balance flow tracking (Income $\rightarrow$ Expenses $\rightarrow$ Net Balance).
- **Radar Spending Chart**: Multi-dimensional spending comparison across categories.
- **Trend Bar-Line Chart & Progress Indicators**: Real-time budget utilization and savings goal trackers.

### 💳 Multi-Account & Atomic Transfer System
- Support for Cash, Bank Accounts, Credit Cards, Savings Accounts, and Digital Wallets.
- Inter-account money transfers with atomic balance synchronization that prevents double-counting transfers as income or expenses.

### 🎯 Smart Budgeting & Rollover Management
- Create period budgets (Monthly, Weekly, Yearly) with category-specific spending limits.
- Visual progress meters with automated threshold notification warnings at 70%, 80%, 90%, and 100%.

### 🏆 Savings Goals Tracking
- Set target goal amounts and target dates.
- Automated calculation of required monthly savings and deposit/withdrawal transaction histories.

### 🔄 WorkManager Recurring Bills & Subscriptions
- Automated background processing of due recurring transactions (salaries, subscriptions, rent, utility bills).
- Duplicate transaction suppression and local system notifications via Android `WorkManager`.

### 🧠 Offline Financial Insights Engine
- Analytical engine running locally on-device to highlight spending anomalies, top spending days, and savings rate performance.

### 📅 Financial Calendar View
- Day-by-day interactive transaction grid with daily totals header and quick date navigation.

### 🎨 Dynamic Material 3 Theme & Palette Engine
- 4 Theme Modes: **Light**, **Dark**, **System**, and **AMOLED**.
- 10 custom dynamic color schemes with full edge-to-edge support.

### 📁 Data Backup, Export & SAF Integration
- Export transactions to CSV and JSON formats using Android Storage Access Framework (SAF).
- Offline full database backup creation and restoration.

### 📱 Adaptive Navigation Layout
- Responsive UI adaptively switching between **Bottom Navigation Bar** on portrait phones and **Navigation Rail** on landscape viewports and tablets.

### 🎲 Built-In Realistic Sample Data Generator
- One-click sample data generation seeding 15+ realistic income, expense, category, budget, and goal records to explore filled dashboards upon initial setup.

---

## 🏗 Architecture & Project Structure

The project follows strict **Clean Architecture** combined with **MVVM** and **Unidirectional Data Flow (UDF)**.

```
com.example.expensetracker/
├── core/                                # Shared infrastructure & design system
│   ├── common/                          # Formatters (Currency, Date, MinorUnits)
│   ├── designsystem/                    # Material 3 Design System (Typography, Dimensions, 40+ atomic components, Color Palettes)
│   ├── export/                          # Storage Access Framework (SAF) Data Exporters (CSV, JSON)
│   ├── navigation/                      # Screen destinations & adaptive NavGraph (BottomBar / NavRail)
│   ├── notifications/                   # NotificationHelper & WorkManager Recurring Transaction Worker
│   └── utils/                           # Extension functions & Compose Debounce Modifiers
│
├── data/                                # Data Layer Implementation
│   ├── datastore/                       # UserPreferencesRepositoryImpl (DataStore Preferences)
│   ├── local/                           # Room Database setup
│   │   ├── dao/                         # DAOs (Account, Budget, Category, Transaction, Recurring, Savings, etc.)
│   │   ├── database/                    # ExpenseTrackerDatabase, DefaultData, & SampleDataGenerator
│   │   ├── entity/                      # Room Database Entities
│   │   └── model/                       # Local query projection models
│   ├── mapper/                          # Entity <-> Domain Model Mappers
│   └── repository/                      # Concrete Repository Implementations
│
├── di/                                  # Dependency Injection (Dagger Hilt Modules)
│   ├── DatabaseModule.kt                # Database & DAO providers
│   └── RepositoryModule.kt              # Repository interface bindings
│
├── domain/                              # Pure Kotlin Domain Layer (Framework-Independent)
│   ├── model/                           # Domain Entities (Account, Transaction, Budget, SavingsGoal, etc.)
│   ├── repository/                      # Repository Interfaces
│   └── usecase/                         # 20+ Business Use Cases (SaveTransaction, GetDashboardSummary, etc.)
│
└── presentation/                        # UI Layer (Jetpack Compose + Hilt ViewModels)
    ├── accounts/                        # Accounts overview & transfer dialogs
    ├── budgets/                         # Budget limits, period selectors & goal bottom sheets
    ├── calendar/                        # Transaction calendar view & daily header summary
    ├── categories/                      # Categories & subcategories management grid
    ├── components/charts/               # Pure Compose Canvas custom chart components
    ├── dashboard/                       # Customizable widget-based home dashboard
    ├── insights/                        # Offline financial health cards & tips
    ├── recurring/                       # Recurring bills management & setup
    ├── reports/                         # Financial analytics, KPIs & chart breakdowns
    ├── savings/                         # Savings goals tracking & deposit sheets
    ├── settings/                        # Theme, palette engine, SAF backup/export & sample data controls
    └── transactions/                    # Transaction list, filter bottom sheet, custom numeric keypad & add/edit form
```

---

## 🛠 Tech Stack & Dependencies

| Category | Technology / Library | Version |
| :--- | :--- | :--- |
| **Language** | Kotlin | `2.2.10` |
| **Build Tool** | Android Gradle Plugin (AGP) | `9.5.1` |
| **Annotation Processing**| KSP (Kotlin Symbol Processing) | `2.2.10-2.0.2` |
| **Compile & Target SDK** | Android 15 (API 36) | `36` |
| **Minimum SDK** | Android 7.0 Nougat (API 24) | `24` |
| **UI Framework** | Jetpack Compose (BOM `2024.10.00`) | Material 3 |
| **Dependency Injection** | Dagger Hilt | `2.60.1` |
| **Database** | Room Database | `2.8.4` |
| **Preferences** | DataStore Preferences | `1.1.1` |
| **Background Tasks** | WorkManager | `2.10.0` |
| **Navigation** | Navigation Compose & Hilt Navigation | `2.8.4` / `1.2.0` |
| **Scalable UI Layout** | SDP & SSP Scalable Units | `1.1.1` |
| **Testing** | JUnit4, AndroidX Test, Espresso Core | `4.13.2` / `1.2.1` |

---

## 📱 Manifest & Android System Integration

- **Permissions**:
  - `android.permission.POST_NOTIFICATIONS`: For budget alert thresholds (70%, 80%, 90%, 100%) and recurring transaction execution.
  - `android.permission.SCHEDULE_EXACT_ALARM`: For precise background recurring transaction alerts.
- **Edge-to-Edge**: Enabled with `enableEdgeToEdge()` in `MainActivity`.
- **Soft Input Handling**: Configured with `windowSoftInputMode="adjustResize"` for seamless keypad and form scrolling.

---

## 🚀 Getting Started & Local Setup

### Prerequisites
- **Android Studio Ladybug (2024.2.1) or newer**
- **JDK 17** configured in Android Studio

### Build Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/hammad-ahmed-android-engineer/ExpenseTracker.git
   cd ExpenseTracker
   ```
2. Open the project in Android Studio.
3. Sync Gradle project files.
4. Select the `app` run configuration and launch on an emulator or physical device running **Android 7.0 (API 24)** or higher.

### Running Unit Tests
Execute the unit test suite via Gradle CLI:
```bash
./gradlew test
```

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).
