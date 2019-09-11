package com.pryanya.summerwallet.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pryanya.summerwallet.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Account::class, Category::class, Operation::class],
    version = 1,
    exportSchema = false
)
abstract class MoneyDatabase : RoomDatabase() {


    abstract val accountsDao: AccountsDao
    abstract val operationDao: OperationDao
    abstract val categoryDao: CategoryDao


    companion object {
        @Volatile
        private var INSTANCE: MoneyDatabase? = null

        fun getInstance(context: Context): MoneyDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MoneyDatabase::class.java,
                        "money_history_database"
                    ).fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                CoroutineScope(Dispatchers.IO).launch {
                                    INSTANCE!!.prepopulate()
                                }
                            }
                        }).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

    private fun prepopulate() {
        Log.i("DATABASE", "PREPOPULATE")
        val list = listOf(
            Category(
                PrebuiltCategories.TRANSPORT.id,
                PrebuiltCategories.TRANSPORT.catName,
                true,
                CategoryType.EXPENSE.id
            ),
            Category(
                PrebuiltCategories.FOOD.id,
                PrebuiltCategories.FOOD.catName,
                true,
                CategoryType.EXPENSE.id
            ),
            Category(
                PrebuiltCategories.ENTERTAINMENT.id,
                PrebuiltCategories.ENTERTAINMENT.catName,
                true,
                CategoryType.EXPENSE.id
            ),
            Category(
                PrebuiltCategories.NO_CATEGORY_PROFIT.id,
                PrebuiltCategories.NO_CATEGORY_PROFIT.catName,
                true,
                CategoryType.PROFIT.id
            ),
            Category(
                PrebuiltCategories.NO_CATEGORY_EXPENSE.id,
                PrebuiltCategories.NO_CATEGORY_EXPENSE.catName,
                true,
                CategoryType.EXPENSE.id
            ),
            Category(
                PrebuiltCategories.REMITTANCE.id,
                PrebuiltCategories.REMITTANCE.catName,
                true,
                CategoryType.REMITTANCE.id
            ),
            Category(
                PrebuiltCategories.SALARY.id,
                PrebuiltCategories.SALARY.catName,
                true,
                CategoryType.PROFIT.id
            )
        )
        INSTANCE?.categoryDao?.insert(*list.toTypedArray())

        INSTANCE?.accountsDao?.insert(
            Account(
                id = PrebuiltAccounts.CASH_ACCOUNT.id,
                name = PrebuiltAccounts.CASH_ACCOUNT.accName,
                type = AccountType.CASH.id,
                sum = 0.0,
                isCountable = true
            )
        )
    }
}


