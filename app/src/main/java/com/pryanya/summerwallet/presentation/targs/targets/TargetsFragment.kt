package com.pryanya.summerwallet.presentation.targs.targets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentTargetsBinding

class TargetsFragment : Fragment() {

    lateinit var viewModel: TargetsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = TargetsViewModelFactory(repository)

        viewModel =
            ViewModelProviders.of(this, factory).get(TargetsViewModel::class.java)


        val binding: FragmentTargetsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_targets, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = TargetsAdapter(
            TargetClickListener {
                this.findNavController().navigate(
                    TargetsFragmentDirections
                        .actionTargetsFragmentToEditTargetFragment(it.id)
                )
            },
            TargetLongClickListener {
                viewModel.targetToDelete = it
                buildDialog().show()
                true
            }
        )

        binding.fragmentAccountsList.adapter = adapter

        viewModel.targets.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addAndSubmitList(it)
            }
        })

        viewModel.floatingButtonClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    TargetsFragmentDirections
                        .actionTargetsFragmentToNewTargetFragment()
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
            viewModel.deleteTarget()
        }
        setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->

        }
        setCancelable(true)
    }


}