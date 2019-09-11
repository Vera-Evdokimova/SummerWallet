package com.pryanya.summerwallet.presentation.cats.editcategory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider
import com.pryanya.summerwallet.data.entities.Category
import com.pryanya.summerwallet.data.repositories.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EditCategoryViewModel(val repository: Repository, val id: Long) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var name: String = ""
    lateinit var category: Category

    init {
        viewModelScope.launch {
            category = repository.getCategoryById(id)
            name = category.name
        }
    }


    fun updateCategory() {
        viewModelScope.launch {
            Log.i("update Category", "ac id")
            Log.i("update Category", "name : $name")
            repository.updateCategory(
                Category(
                    id = category.id,
                    name = name,
                    type = category.type
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


class EditCategoryViewModelFactory(
    private val repository: Repository,
    val id: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditCategoryViewModel::class.java)) {
            return EditCategoryViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}