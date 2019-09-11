package com.pryanya.summerwallet.presentation.ops.newoperation

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.data.entities.Category
import com.pryanya.summerwallet.data.entities.OperationType
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentNewOperationBinding
import com.pryanya.summerwallet.utils.DecimalDigitsInputFilter
import java.util.*

class NewOperationFragment : Fragment() {

    lateinit var viewModel: NewOperationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = NewOperationViewModelFactory(
            repository,
            NewOperationFragmentArgs.fromBundle(arguments!!).type
        )

        viewModel =
            ViewModelProviders.of(this, factory).get(NewOperationViewModel::class.java)

        val binding: FragmentNewOperationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_operation, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.accounts.observe(this, Observer {
            setupSpinnerAccounts(it, binding)
            if (!it.isNullOrEmpty()) {
                viewModel.accountInd = 0
            }
        })

        viewModel.accounts.observe(this, Observer {
            setupSpinnersTransfer(it, binding)
            if (!it.isNullOrEmpty()) {
                viewModel.targetInd = 0
            }
        })

        viewModel.categories.observe(this, Observer {
            setupSpinnerCategory(it, binding)
            if (!it.isNullOrEmpty()) {
                viewModel.categoryInd = 0
            }
        })

        viewModel.buttonClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.sum != null) {
                    if (viewModel.targetInd == -1 && viewModel.type == OperationType.REMITTANCE.id) {
                        Toast.makeText(
                            context,
                            getString(R.string.fill_target_field_request),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.addOperation()
                        this.findNavController().navigate(
                            NewOperationFragmentDirections
                                .actionNewOperationFragmentToHistoryFragment()
                        )
                    }
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.fill_fields_request),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.onStopClicking()
            }
        })

        binding.newOpSumEt.filters =
            arrayOf<InputFilter>(DecimalDigitsInputFilter())

        val datePicker = binding.datePicker
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { picker, _, _, _ ->
            viewModel.date = picker.getDate().time
        }

        return binding.root

    }

    private fun setupSpinnerAccounts(list: List<Account>, binding: FragmentNewOperationBinding) {
        val adapterAccount = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_item,
            list.map { it.name })
        adapterAccount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerAcc = binding.newOpAccSpinner
        spinnerAcc.adapter = adapterAccount
        spinnerAcc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                viewModel.accountInd = position
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

    }

    private fun setupSpinnersTransfer(list: List<Account>, binding: FragmentNewOperationBinding) {
        val adapterTo = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_item,
            list.map { it.name })
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerTransferTo = binding.newOpToAccSpinner
        spinnerTransferTo.adapter = adapterTo
        spinnerTransferTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                viewModel.targetInd = position
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }

    private fun setupSpinnerCategory(list: List<Category>, binding: FragmentNewOperationBinding) {
        val adapterCategory = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_item,
            list.map { it.name })
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerCat = binding.newOpCatSpinner
        spinnerCat.adapter = adapterCategory
        spinnerCat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                viewModel.categoryInd = position
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }

    private fun DatePicker.getDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }
}