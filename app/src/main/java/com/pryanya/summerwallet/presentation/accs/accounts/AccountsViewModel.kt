package com.pryanya.summerwallet.presentation.accs.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AccountsViewModel(var repository: Repository) : ViewModel() {


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _accounts = repository.accounts
    val accounts: LiveData<List<Account>>
        get() = _accounts


    var accountToDelete: Account? = null

    fun deleteAccount() {
        viewModelScope.launch {
            accountToDelete?.let {
                repository.deleteAccount(accountToDelete!!)
            }
        }
    }

    val floatingButtonClick = MutableLiveData<Boolean>()

    fun onButtonClick() {
        floatingButtonClick.value = true
    }

    fun onStopClicking() {
        floatingButtonClick.value = null
    }

}


class AccountsViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsViewModel::class.java)) {
            return AccountsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}