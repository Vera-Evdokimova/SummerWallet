package com.pryanya.summerwallet.utils

import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.entities.*
import java.text.SimpleDateFormat
import java.util.*


//for categories
@BindingAdapter("catImage")
fun ImageView.accImage(category: Category) {
    this.setImageResource(
        when (category.id) {
            PrebuiltCategories.ENTERTAINMENT.id -> R.drawable.ic_entertainment
            PrebuiltCategories.TRANSPORT.id -> R.drawable.ic_transport
            PrebuiltCategories.FOOD.id -> R.drawable.ic_cafe
            PrebuiltCategories.REMITTANCE.id -> R.drawable.ic_remmitance
            PrebuiltCategories.SALARY.id -> R.drawable.ic_salary
            else -> when (category.type) {
                CategoryType.EXPENSE.id -> R.drawable.ic_other_expense
                CategoryType.PROFIT.id -> R.drawable.ic_other_profit
                else -> R.drawable.ic_no_category
            }
        }
    )
}

@BindingAdapter("catType")
fun TextView.catType(category: Category) {
    text = when {
        category.type == CategoryType.PROFIT.id -> context.getString(R.string.profit_cat_type)
        category.type == CategoryType.EXPENSE.id -> context.getString(R.string.expense_cat_type)
        else -> context.getString(R.string.remittance_cat_type)
    }
}

@BindingAdapter("image")
fun ImageView.catTypeImage(category: Category){
    this.setImageResource(
        when (category.type){
            CategoryType.EXPENSE.id -> R.drawable.ic_other_expense
            CategoryType.PROFIT.id -> R.drawable.ic_other_profit
            else -> R.drawable.ic_settings
        }
    )
}

//for targets

@BindingAdapter("targetMessage")
fun TextView.targetMessage(state: Int) {
    text = when {
        state > 0 -> context.getString(R.string.target_message_success)
        state == 0 -> context.getString(R.string.target_message_daily)
        else -> context.getString(R.string.target_message_fail)
    }
}

@BindingAdapter("getTargSum")
fun TextView.getTargSum(account: Account) {
    text = String.format("%.2f P", account.targetSum)
}

@BindingAdapter("formatDateTarg")
fun TextView.formatDateTarg(item: Account) {
    text = formatter.format(Date(item.endDate!!))
}

//for accounts

@BindingAdapter("accType")
fun TextView.accType(account: Account) {
    text = when {
        account.type == AccountType.CASH.id -> context.getString(R.string.cash_acc_type)
        account.type == AccountType.CREDIT_CARD.id -> context.getString(R.string.credit_acc_type)
        else -> context.getString(R.string.target_acc_type)
    }
}

@BindingAdapter("accImage")
fun ImageView.accImage(account: Account) {
    this.setImageResource(
        when {
            account.type == AccountType.CASH.id -> R.drawable.ic_cash
            account.type == AccountType.CREDIT_CARD.id -> R.drawable.ic_creditcard
            else -> R.drawable.ic_target
        }
    )
}


@BindingAdapter("getSumAcc")
fun TextView.getSumAcc(account: Account) {
    text = String.format("%.2f P", account.sum)
}

@BindingAdapter("getAccId")
fun TextView.getAccId(account: Account) {
    text = account.id.toString()
}


// for operations

@BindingAdapter("categoryImage")
fun ImageView.setCategoryImage(item: Operation) {
    setImageResource(
        when (item.categoryId) {
            PrebuiltCategories.ENTERTAINMENT.id -> R.drawable.ic_entertainment
            PrebuiltCategories.TRANSPORT.id -> R.drawable.ic_transport
            PrebuiltCategories.FOOD.id -> R.drawable.ic_cafe
            PrebuiltCategories.REMITTANCE.id -> R.drawable.ic_remmitance
            PrebuiltCategories.SALARY.id -> R.drawable.ic_salary
            else -> when (item.type) {
                OperationType.EXPENSE.id -> R.drawable.ic_other_expense
                OperationType.PROFIT.id -> R.drawable.ic_other_profit
                else -> R.drawable.ic_no_category
            }
        }
    )

}

val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

@BindingAdapter("formatDate")
fun TextView.formatDate(item: Operation) {
    text = formatter.format(Date(item.date))
}

@BindingAdapter("categoryText")
fun TextView.setCategoryText(item: Operation) {
    text =
        when (item.categoryId) {
            PrebuiltCategories.NO_CATEGORY_PROFIT.id -> context.getString(R.string.no_category)
            PrebuiltCategories.ENTERTAINMENT.id -> context.getString(R.string.entertainment)
            PrebuiltCategories.TRANSPORT.id -> context.getString(R.string.transport)
            PrebuiltCategories.FOOD.id -> context.getString(R.string.food)
            PrebuiltCategories.REMITTANCE.id -> context.getString(R.string.remittance)
            else -> context.getString(R.string.other)
        }
}

@BindingAdapter("getSum")
fun TextView.getSum(operation: Operation) {
    text = String.format("%.2f P", operation.sum)
}

@BindingAdapter("getBudget")
fun TextView.getBudget(budget: Double) {
    text = String.format("%.2f P", budget)
}

@BindingAdapter("invisibleIf")
fun Spinner.invisibleIf(type: Int) {
    if (type != OperationType.REMITTANCE.id)
        visibility = View.GONE
}

