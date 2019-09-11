package com.pryanya.summerwallet.presentation.targs.targets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TargetsViewModel(var repository: Repository) : ViewModel() {


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val targets = repository.targets


    var targetToDelete: Account? = null

    fun deleteTarget() {
        viewModelScope.launch {
            targetToDelete?.let {
                repository.deleteAccount(targetToDelete!!)
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


class TargetsViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TargetsViewModel::class.java)) {
            return TargetsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}