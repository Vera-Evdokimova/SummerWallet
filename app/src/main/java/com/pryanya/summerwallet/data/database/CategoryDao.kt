package com.pryanya.summerwallet.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pryanya.summerwallet.data.entities.Category


@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg category: Category)

    @Update
    fun update(category: Category)

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM Category WHERE NOT prebuilt")
    fun clear()

    @Query("SELECT * FROM Category ORDER BY id DESC")
    fun getAllCategoriesLiveData(): LiveData<List<Category>>

    @Query("SELECT * FROM Category WHERE id = :key")
    fun getCategoryWithIdLiveData(key: Long): LiveData<Category>

    @Query("SELECT * FROM Category WHERE id = :key")
    fun getCategoryWithId(key: Long): Category

    @Query("SELECT * FROM Category ORDER BY id ASC")
    fun getAllCategories(): List<Category>

    // 0 - Type.Profit
    @Query("SELECT * FROM Category WHERE type = 0 ORDER BY id ASC")
    fun getProfitCategoriesLiveData(): LiveData<List<Category>>

    // 1 - Type.Expense
    @Query("SELECT * FROM Category WHERE type = 1 ORDER BY id ASC")
    fun getExpenseCategoriesLiveData():LiveData<List<Category>>

    // 2 - Type.Remittance
    @Query("SELECT * FROM Category WHERE type = 2 ORDER BY id ASC")
    fun getRemittanceCategoriesLiveData(): LiveData<List<Category>>
}