package com.pryanya.summerwallet.presentation.ops.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Operation
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HistoryViewModel(val repository: Repository) : ViewModel() {


    var budget = MutableLiveData<Double>()


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var deleteOperation: Operation? = null

    fun deleteOperation() {
        viewModelScope.launch { repository.deleteOperation(deleteOperation!!) }
    }

    var categoryFilter = MutableLiveData<Long>()

    val categories = repository.allCategories

    init {
        categoryFilter.value = 0L
    }

    val operations = repository.operations

    val floatingButtonClick = MutableLiveData<Boolean>()

    fun onButtonClick() {
        floatingButtonClick.value = true
    }

    fun onStopClicking() {
        floatingButtonClick.value = null
    }

}

class HistoryViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}