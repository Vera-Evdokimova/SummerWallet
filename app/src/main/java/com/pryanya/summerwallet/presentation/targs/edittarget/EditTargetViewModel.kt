package com.pryanya.summerwallet.presentation.targs.edittarget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.entities.AccountType
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EditTargetViewModel(val repository: Repository, val id: Long) : ViewModel() {


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var name: String = ""
    var sum: String = "0.0"
    var date: Long = System.currentTimeMillis()
    lateinit var target: Account

    init {
        viewModelScope.launch {
            target = repository.getAccountById(id)
            name = target.name
        }
    }


    fun updateTarget() {
        viewModelScope.launch {
            val targetSum = java.lang.Double.parseDouble(sum)
            repository.updateAccount(
                Account(
                    id = target.id,
                    name = name,
                    sum = target.sum,
                    targetSum = targetSum,
                    type = AccountType.TARGET.id,
                    endDate = date
                )
            )
        }
    }

    val buttonClick = MutableLiveData<Boolean>()

    fun onButtonClick() {
        buttonClick.value = true
    }

    fun onStopClicking() {
        buttonClick.value = null
    }

}

class EditTargetViewModelFactory(
    private val repository: Repository,
    val id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTargetViewModel::class.java)) {
            return EditTargetViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}