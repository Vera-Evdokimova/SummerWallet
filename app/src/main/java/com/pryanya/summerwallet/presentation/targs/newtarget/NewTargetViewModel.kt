package com.pryanya.summerwallet.presentation.targs.newtarget

import android.util.Log
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

class NewTargetViewModel(val repository: Repository) : ViewModel() {

    var name: String? = null
    var sum: String = "0.0"
    var date: Long = System.currentTimeMillis()


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun addTarget() {
        viewModelScope.launch {
            val sum = java.lang.Double.parseDouble(sum)
            Log.i("New Account", "ac id")
            repository.addAccount(
                Account(
                    name = name!!,
                    targetSum = sum,
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

class NewTargetViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTargetViewModel::class.java)) {
            return NewTargetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}