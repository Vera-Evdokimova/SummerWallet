package com.pryanya.summerwallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.entities.AccountType

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: Account)

    @Delete
    fun delete(account: Account)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(account: Account)

    @Query("DELETE FROM Account WHERE id <> 1")
    fun clear()

    @Query("SELECT * FROM Account ORDER BY id ASC")
    fun getAllAccountsLiveData(): LiveData<List<Account>>

    @Query("SELECT * FROM Account WHERE id = :key")
    fun getAccountWithId(key: Long): Account?

    @Query("SELECT * FROM Account ORDER BY id ASC")
    fun getAllAccounts(): List<Account>


    //2 - AccountType.TARGET.id, потому что нельзя использовать не константы в запросах
    @Query("SELECT * FROM Account WHERE type <> 2 ORDER BY id ASC")
    fun getNonTargetsLiveData(): LiveData<List<Account>>

    @Query("SELECT * FROM Account WHERE type = 2 ORDER BY id ASC")
    fun getTargetsLiveData(): LiveData<List<Account>>

}

