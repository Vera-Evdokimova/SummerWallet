package com.pryanya.summerwallet.data.entities

enum class AccountType(val id: Int) {
    CASH(0),
    CREDIT_CARD(1),
    TARGET(2)
}

enum class CategoryType(val id: Int) {
    PROFIT(0),
    EXPENSE(1),
    REMITTANCE(2)
}

enum class OperationType(val id: Int) {
    PROFIT(0),
    EXPENSE(1),
    REMITTANCE(2)
}

enum class PrebuiltCategories(val id: Long, val catName: String) {
    NO_CATEGORY_PROFIT(1, "No category Profit"),
    TRANSPORT(2, "Transport"),
    FOOD(3, "Food"),
    ENTERTAINMENT(4, "Entertainment"),
    REMITTANCE(5, "Remittance"),
    SALARY(6, "Salary"),
    NO_CATEGORY_EXPENSE(7, "No category Expense")
}

enum class PrebuiltAccounts(val id: Long, val accName: String) {
    CASH_ACCOUNT(1L, "Cash"),
    CREDIT_CARD_ACCOUNT(2L, "Credit card"),
    DELETED(-1L, "Deleted")
}