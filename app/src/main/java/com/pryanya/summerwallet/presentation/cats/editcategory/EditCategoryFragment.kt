package com.pryanya.summerwallet.presentation.cats.editcategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.EditCategoryFragmentBinding

class EditCategoryFragment : Fragment() {

    lateinit var viewModel: EditCategoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = EditCategoryViewModelFactory(
            repository,
            EditCategoryFragmentArgs.fromBundle(arguments!!).categoryId
        )
        viewModel = ViewModelProviders.of(this, factory).get(EditCategoryViewModel::class.java)

        val binding: EditCategoryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.edit_category_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.buttonClick.observe(this, Observer {
            it?.let {
                if (viewModel.name.isNotEmpty()) {
                    this.findNavController()
                        .navigate(
                            EditCategoryFragmentDirections
                                .actionEditCategoryFragmentToCategoriesFragment()
                        )
                    viewModel.updateCategory()
                } else {
                    Toast.makeText(context, getString(R.string.name_request), Toast.LENGTH_LONG)
                        .show()
                }
                viewModel.onStopClicking()
            }
        })

        return binding.root
    }
}

