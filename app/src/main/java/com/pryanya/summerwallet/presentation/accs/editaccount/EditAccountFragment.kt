package com.pryanya.summerwallet.presentation.accs.editaccount

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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.entities.AccountType
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.EditAccountFragmentBinding

class EditAccountFragment : Fragment() {

    lateinit var viewModel: EditAccountViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = EditAccountViewModelFactory(
            repository,
            EditAccountFragmentArgs.fromBundle(arguments!!).accountId
        )
        viewModel = ViewModelProviders.of(this, factory).get(EditAccountViewModel::class.java)

        val binding: EditAccountFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.edit_account_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupSpinner(listOf(AccountType.CASH.id, AccountType.CREDIT_CARD.id), binding)

        viewModel.buttonClick.observe(this, Observer {
            it?.let {
                if (viewModel.name.isNotEmpty()) {
                    this.findNavController()
                        .navigate(
                            EditAccountFragmentDirections
                                .actionEditAccountFragmentToAccountsFragment()
                        )
                    viewModel.updateAccount()
                } else {
                    Toast.makeText(context, getString(R.string.name_request), Toast.LENGTH_LONG)
                        .show()
                }
                viewModel.onStopClicking()
            }
        })

        return binding.root
    }


    private fun setupSpinner(list: List<Int>, binding: EditAccountFragmentBinding) {
        val adapterTo = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.acc_cash), getString(R.string.acc_creditcard))
        )
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerTransferTo = binding.editAccTypeSpinner
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
