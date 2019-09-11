package com.pryanya.summerwallet.presentation.accs.newaccount

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.entities.AccountType
import com.pryanya.summerwallet.data.entities.OperationType
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NewAccountViewModel(val repository: Repository) : ViewModel() {

    var name: String? = null
    var sum: String = "0.0"
//    var countable: Boolean = false
    var type = AccountType.CREDIT_CARD.id




    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun addAccount() {
        viewModelScope.launch {
            val sum = java.lang.Double.parseDouble(sum)
            Log.i("New Account", "ac id")
            repository.addAccount(
                Account(
                    name = name!!,
                    sum = sum,
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

class NewAccountViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewAccountViewModel::class.java)) {
            return NewAccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
