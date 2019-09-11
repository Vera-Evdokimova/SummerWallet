package com.pryanya.summerwallet.data.repositories

import androidx.lifecycle.LiveData
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: MoneyDatabase) {

    val operations: LiveData<List<Operation>> = database.operationDao.getAllOperationsLiveData()

    val allAccounts = database.accountsDao.getAllAccountsLiveData()

    val allCategories = database.categoryDao.getAllCategoriesLiveData()

    val targets: LiveData<List<Account>> = database.accountsDao.getTargetsLiveData()

    val accounts: LiveData<List<Account>> = database.accountsDao.getNonTargetsLiveData()

    val profitCategories = database.categoryDao.getProfitCategoriesLiveData()

    val expenseCategoties = database.categoryDao.getExpenseCategoriesLiveData()

    val remittanceCategories = database.categoryDao.getRemittanceCategoriesLiveData()

    suspend fun getAccountById(id: Long): Account {
        var acc: Account? = null
        withContext(Dispatchers.IO) {
            acc = database.accountsDao.getAccountWithId(id)
        }
        return acc!!
    }

    suspend fun getCategoryById(id: Long): Category {
        var cat: Category? = null
        withContext(Dispatchers.IO) {
            cat = database.categoryDao.getCategoryWithId(id)
        }
        return cat!!
    }


    suspend fun deleteOperation(vararg operations: Operation) {
        withContext(Dispatchers.IO) {
            val acc = database.accountsDao.getAccountWithId(operations.first().accountId)!!
            operations.forEach {
                acc.sum -= it.sum
            }
            database.accountsDao.update(acc)
            operations.forEach { operation ->
                if (operation.toId != null) {
                    val target = database.accountsDao.getAccountWithId(operation.toId!!)
                    target?.let {
                        target.sum += operation.sum // минус на минус и бла бла бла
                        database.accountsDao.update(target)
                    }
                }
            }
            database.operationDao.delete(*operations)
        }
    }

    suspend fun deleteAccount(account: Account) {
        withContext(Dispatchers.IO) {
            val listTarg = database.operationDao.getAllWithTargetId(account.id)
            listTarg.forEach {
                it.to = null
                it.toId = null
            }
            database.operationDao.update(*listTarg.toTypedArray())
            database.operationDao.delete(
                *database.operationDao.getAllWithAccountId(account.id).toTypedArray()
            )
            database.accountsDao.delete(account)
        }
    }


    suspend fun deleteCategory(category: Category) {
        withContext(Dispatchers.IO) {
            database.categoryDao.delete(category)
            val ops = database.operationDao.getAllWithCategoryId(category.id).apply {
                forEach {
                    when (it.type) {
                        OperationType.PROFIT.id -> {
                            it.categoryId = PrebuiltCategories.NO_CATEGORY_PROFIT.id
                            it.category = PrebuiltCategories.NO_CATEGORY_PROFIT.catName
                        }
                        OperationType.EXPENSE.id -> {
                            it.categoryId = PrebuiltCategories.NO_CATEGORY_EXPENSE.id
                            it.category = PrebuiltCategories.NO_CATEGORY_EXPENSE.catName
                        }
                        else -> {
                            it.categoryId = PrebuiltCategories.REMITTANCE.id
                            it.category = PrebuiltCategories.REMITTANCE.catName
                        }
                    }
                }
            }
            database.operationDao.update(*ops.toTypedArray())
        }
    }

    suspend fun addOperation(operation: Operation) {
        withContext(Dispatchers.IO) {
            val acc = database.accountsDao.getAccountWithId(operation.accountId)!!
            acc.sum += operation.sum
            database.accountsDao.update(acc)
            if (operation.toId != null) {
                val target = database.accountsDao.getAccountWithId(operation.toId!!)!!
                target.sum -= operation.sum // минус на минус - плюс
                database.accountsDao.update(target)
            }
            database.operationDao.insert(operation)
        }
    }

    suspend fun addAccount(account: Account) {
        withContext(Dispatchers.IO) {
            database.accountsDao.insert(account)
        }
    }

    suspend fun addCategory(category: Category) {
        withContext(Dispatchers.IO) {
            database.categoryDao.insert(category)
        }
    }


    suspend fun updateAccount(account: Account) {
        withContext(Dispatchers.IO) {
            database.accountsDao.update(account)
            val listAcc = database.operationDao.getAllWithAccountId(account.id)
            listAcc.forEach {
                it.account = account.name
            }
            val listTarg = database.operationDao.getAllWithTargetId(account.id)
            listTarg.forEach {
                it.to = account.name
            }
            database.operationDao.update(*listAcc.toTypedArray())
            database.operationDao.update(*listTarg.toTypedArray())
        }
    }

    suspend fun updateCategory(category: Category) {
        withContext(Dispatchers.IO) {
            database.categoryDao.update(category)
            val list = database.operationDao.getAllWithCategoryId(category.id)
            list.forEach {
                it.category = category.name
            }
            database.operationDao.update(*list.toTypedArray())
        }
    }


}