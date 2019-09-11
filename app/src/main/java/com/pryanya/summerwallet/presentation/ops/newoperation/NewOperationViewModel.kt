package com.pryanya.summerwallet.presentation.ops.newoperation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Category
import com.pryanya.summerwallet.data.entities.CategoryType
import com.pryanya.summerwallet.data.entities.Operation
import com.pryanya.summerwallet.data.entities.OperationType
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NewOperationViewModel(val repository: Repository, val type: Int) : ViewModel() {


    val categories: LiveData<List<Category>> = when (type) {
        CategoryType.REMITTANCE.id -> repository.remittanceCategories
        CategoryType.PROFIT.id -> repository.profitCategories
        else -> repository.expenseCategoties
    }


    val accounts = repository.allAccounts

    // val targets = repository.targets

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var sum: String? = null

    var date: Long = System.currentTimeMillis()

    var accountInd = -1

    var categoryInd = -1

    var targetInd = -1

    val buttonClick = MutableLiveData<Boolean>()

    fun addOperation() {
        viewModelScope.launch {
            var sum = java.lang.Double.parseDouble(sum!!)
            if (type != OperationType.PROFIT.id) {
                sum = -sum
            }

            val account = accounts.value!![accountInd]
            val category = categories.value!![categoryInd]
            val target = accounts.value!![targetInd]
            var targetId: Long? = target.id
            var targetName: String? = target.name
            if (type != OperationType.REMITTANCE.id) {
                targetId = null
                targetName = null
            }

            repository.addOperation(
                Operation(
                    date = date,
                    sum = sum,
                    type = type,
                    accountId = account.id,
                    categoryId = category.id,
                    toId = targetId,
                    to = targetName,
                    account = account.name,
                    category = category.name
                )
            )
        }
    }

    fun onButtonClick() {
        buttonClick.value = true
    }

    fun onStopClicking() {
        buttonClick.value = null
    }

}


class NewOperationViewModelFactory(val repository: Repository, val type: Int) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewOperationViewModel::class.java)) {
            return NewOperationViewModel(repository, type) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}