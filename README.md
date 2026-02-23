# Financial Tracker & Data Aggregator

Консольное Java-приложение для управления финансами, анализа транзакций и расчета глубокой аналитики. Приложение позволяет фильтровать, группировать и агрегировать данные, сохраняя результаты в формате JSON.

## 🚀 Функциональные возможности

*   Учет транзакций разных типов:
    *   Обычные транзакции.
    *   Облагаемые налогом (автоматический вычет налога).
    *   Валютные (конвертация по курсу).
    *   Рекуррентные (повторяющиеся платежи).
    *   Комментируемые (поддержка текстовых заметок).
*   Гибкая система фильтрации: Поиск по датам, категориям, диапазону сумм и ключевым словам в комментариях.
*   Группировка данных: По месяцам, годам, дням недели, категориям, типам счетов и ID пользователей.
*   Агрегация: Расчет суммы, среднего значения или количества транзакций.
*   Экспорт аналитики: Сохранение результатов в формате JSON с использованием Jackson.

## 🛠 Технологический стек

*   Java 17 (или выше)
*   Gradle (система сборки)
*   Lombok (для уменьшения шаблонного кода)
*   Jackson Databind (для работы с JSON и поддержкой JavaTime)
*   JUnit / Mockito (предусмотрено для тестирования)

## 📁 Структура проекта
               
├── src/
│   ├── main/java/      # Исходный код приложения
│   └── main/data/      # Папка с исходными данными (.txt)
├── build.gradle        # Конфигурация сборки Gradle
└── README.md
## ⚙️ Запуск приложения

Приложение принимает 3 обязательных аргумента командной строки:
1. Путь к файлу со счетами (accounts.txt).
2. Путь к файлу с транзакциями (transactions.txt).
3. Путь к файлу для выгрузки аналитики (analytics.json).

### Пример запуска из терминала:
java -jar build/libs/fintracker-1.0-SNAPSHOT.jar data/accounts.txt data/transactions.txt data/analytics.json
### Настройка в IntelliJ IDEA:
1. Перейдите в Run -> Edit Configurations.
2. В поле Program arguments вставьте:
   data/accounts.txt data/transactions.txt data/analytics.json

## 📊 Формат входных данных

### 1. Счета (accounts.txt)
Формат: ID_счета,Тип_счета,ID_пользователя
*Типы: 0 — Текущий, 1 — Сберегательный, 2 — Кредитный.*
1,0,101
2,1,101
### 2. Транзакции (transactions.txt)
Формат зависит от типа транзакции (общие поля + специфичные данные):
*   Regular: userId,id,date,category,amount,Regular
*   Taxable: userId,id,date,category,amount,Taxable,taxRate
*   ForeignCurrency: userId,id,date,category,amount,ForeignCurrency,exchangeRate
*   Commentable: userId,id,date,category,amount,Commentable,comment1;comment2
*   Recurrent: userId,id,date,category,amount,Recurrent,pattern;occurrences

Пример:
101,1,2023-10-01T10:00:00,Food,500.0,Regular
101,2,2023-10-02T12:00:00,Salary,3000.0,Taxable,0.13
101,3,2023-10-03T15:00:00,Freelance,100.0,ForeignCurrency,95.5
## 📝 Особенности реализации

*   ООП паттерны: Использование абстрактных классов и интерфейсов для обработки различных типов транзакций.
*   Stream API: Вся логика фильтрации и группировки реализована на стримах, что обеспечивает лаконичность кода.
*   Custom Exceptions: Реализована обработка ошибок ввода, например IncorrectArgumentsNumberException.
*   Strategy-like Menu: Разделение логики меню на специализированные контроллеры (SearchMenuController, GroupMenuController и т.д.).

---
