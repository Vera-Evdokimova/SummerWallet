package com.pryanya.summerwallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pryanya.summerwallet.data.entities.Operation

@Dao
interface OperationDao {
    @Insert
    fun insert(vararg operations: Operation)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg operations: Operation)

    @Query("DELETE FROM Operation")
    fun clear()

    @Delete
    fun delete(vararg operations: Operation)

    @Query("SELECT * FROM Operation ORDER BY date DESC")
    fun getAllOperationsLiveData(): LiveData<List<Operation>>


    @Query("SELECT * FROM Operation WHERE id = :key")
    fun getOperationWithIdLiveData(key: Long): LiveData<Operation>

    @Query("SELECT * FROM Operation WHERE accountId = :key")
    fun getAllWithAccountIdLiveData(key: Long): LiveData<List<Operation>>

    @Query("SELECT * FROM Operation WHERE categoryId = :key")
    fun getAllWithCategoryIdLiveData(key: Long): LiveData<List<Operation>>

    @Query("SELECT * FROM Operation WHERE accountId = :key")
    fun getAllWithAccountId(key: Long): List<Operation>

    @Query("SELECT * FROM Operation WHERE toId = :key")
    fun getAllWithTargetId(key: Long): List<Operation>

    @Query("SELECT * FROM Operation WHERE categoryId = :key")
    fun getAllWithCategoryId(key: Long): List<Operation>
}