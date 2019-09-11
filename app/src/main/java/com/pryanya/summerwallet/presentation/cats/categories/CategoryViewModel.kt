package com.pryanya.summerwallet.presentation.cats.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Category
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CategoryViewModel(val repository: Repository): ViewModel() {


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _categories = repository.allCategories
    val accounts: LiveData<List<Category>>
        get() = _categories

    var categoryToDelete: Category? = null

    fun deleteCategory() {
        viewModelScope.launch {
            categoryToDelete?.let {
                repository.deleteCategory(categoryToDelete!!)
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


class CategoryViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}