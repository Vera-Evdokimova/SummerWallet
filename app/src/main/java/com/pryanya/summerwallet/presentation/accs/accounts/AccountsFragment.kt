package com.pryanya.summerwallet.presentation.accs.accounts


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.entities.AccountType
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentAccountsBinding
import kotlinx.android.synthetic.main.fragment_accounts.*


class AccountsFragment : Fragment() {

    lateinit var viewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = AccountsViewModelFactory(repository)

        viewModel =
            ViewModelProviders.of(this, factory).get(AccountsViewModel::class.java)


        val binding: FragmentAccountsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_accounts, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = AccountsAdapter(
            AccountClickListener {
                this.findNavController().navigate(
                    AccountsFragmentDirections
                        .actionAccountsFragmentToEditAccountFragment(it.id)
                )
            },
            AccountLongClickListener {
                viewModel.accountToDelete = it
                buildDialog().show()
                true
            }
        )

        binding.fragmentAccountsList.adapter = adapter

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addAndSubmitList(it)
            }
        })

        viewModel.floatingButtonClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    AccountsFragmentDirections
                        .actionAccountsFragmentToNewAccountFragment()
                )
                viewModel.onStopClicking()
            }
        })
        return binding.root
    }

    private fun buildDialog() = AlertDialog.Builder(context!!).apply {
        setTitle(getString(R.string.delete_dialog_title))  // заголовок
        setMessage(getString(R.string.acc_del_dialog)) // сообщение
        setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.deleteAccount()
        }
        setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->

        }
        setCancelable(true)
    }


}
