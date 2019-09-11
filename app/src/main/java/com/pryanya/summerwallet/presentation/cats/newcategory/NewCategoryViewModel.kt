package com.pryanya.summerwallet.presentation.cats.newcategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Category
import com.pryanya.summerwallet.data.entities.CategoryType
import com.pryanya.summerwallet.data.entities.OperationType
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NewCategoryViewModel(val repository: Repository) : ViewModel() {

    var name: String? = null
    var type = CategoryType.PROFIT.id


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun addOperation() {

        viewModelScope.launch {
            repository.addCategory(
                Category(
                    name = name!!,
                    type = type
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

class NewCategoryViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewCategoryViewModel::class.java)) {
            return NewCategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}