package com.pryanya.summerwallet.presentation.accs.editaccount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EditAccountViewModel(val repository: Repository, val id: Long) : ViewModel() {


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var name: String = ""
//    var countable: Boolean = false
    var type = -1
    lateinit var account: Account

    init {
        viewModelScope.launch {
            account = repository.getAccountById(id)
            name = account.name
//            countable = account.isCountable
            type = account.type
        }
    }


    fun updateAccount() {
        viewModelScope.launch {
            repository.updateAccount(
                Account(
                    id = account.id,
                    name = name,
                    sum = account.sum,
                    type = type
//                    isCountable = countable
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

class EditAccountViewModelFactory(
    private val repository: Repository,
    val id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAccountViewModel::class.java)) {
            return EditAccountViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}