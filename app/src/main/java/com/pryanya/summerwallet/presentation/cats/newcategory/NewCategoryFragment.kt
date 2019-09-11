package com.pryanya.summerwallet.presentation.cats.newcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.entities.CategoryType
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentNewCategoryBinding


class NewCategoryFragment : Fragment() {

    lateinit var viewModel: NewCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = NewCategoryViewModelFactory(
            repository
        )
        viewModel = ViewModelProviders.of(this, factory).get(NewCategoryViewModel::class.java)


        val binding: FragmentNewCategoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_category, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        setupSpinner(
            listOf(
                CategoryType.PROFIT.id,
                CategoryType.EXPENSE.id,
                CategoryType.REMITTANCE.id
            ), binding
        )


        viewModel.buttonClick.observe(this, Observer {
            it?.let {
                if (viewModel.name.isNullOrEmpty()) {
                    Toast.makeText(context, getString(R.string.name_request), Toast.LENGTH_LONG)
                        .show()
                } else {
                    viewModel.addOperation()
                    this.findNavController()
                        .navigate(
                            NewCategoryFragmentDirections
                                .actionNewCategoryFragmentToCategoriesFragment()
                        )

                }
                viewModel.onStopClicking()
            }
        })


        return binding.root
    }


    private fun setupSpinner(list: List<Int>, binding: FragmentNewCategoryBinding) {
        val adapterTo = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            listOf(
                getString(R.string.profit_category),
                getString(R.string.expense_category),
                getString(R.string.remittance_category)
            )
        )

        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerTransferTo = binding.newCatTypeSpinner
        spinnerTransferTo.adapter = adapterTo
        spinnerTransferTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                viewModel.type = list[position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }
}
