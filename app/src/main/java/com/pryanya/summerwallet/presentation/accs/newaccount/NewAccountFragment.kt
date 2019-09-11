package com.pryanya.summerwallet.presentation.accs.newaccount

import android.os.Bundle
import android.text.InputFilter
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
import com.pryanya.summerwallet.data.entities.AccountType
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentNewAccountBinding
import com.pryanya.summerwallet.utils.DecimalDigitsInputFilter

class NewAccountFragment : Fragment() {

    lateinit var viewModel: NewAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = NewAccountViewModelFactory(
            repository
        )
        viewModel = ViewModelProviders.of(this, factory).get(NewAccountViewModel::class.java)


        val binding: FragmentNewAccountBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_account, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        setupSpinner(listOf(AccountType.CASH.id, AccountType.CREDIT_CARD.id), binding)

        binding.newAccSumEt.filters =
            arrayOf<InputFilter>(DecimalDigitsInputFilter())

        viewModel.buttonClick.observe(this, Observer {
            it?.let {
                if (!viewModel.name.isNullOrEmpty()) {
                    viewModel.addAccount()
                    this.findNavController()
                        .navigate(
                            NewAccountFragmentDirections
                                .actionNewAccountFragmentToAccountsFragment()
                        )
                } else {
                    Toast.makeText(context, getString(R.string.name_request), Toast.LENGTH_LONG)
                        .show()
                }
                viewModel.onStopClicking()
            }
        })


        return binding.root
    }

    private fun setupSpinner(list: List<Int>, binding: FragmentNewAccountBinding) {
        val adapterTo = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.acc_cash), getString(R.string.acc_creditcard))
        )
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerTransferTo = binding.newAccTypeSpinner
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