package com.pryanya.summerwallet.presentation.ops.history


import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.entities.Operation
import com.pryanya.summerwallet.data.entities.OperationType
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentHistoryBinding
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = HistoryViewModelFactory(repository)

        viewModel =
            ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)


        val binding: FragmentHistoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        val adapter = HistoryAdapter(
            OperationClickListener {
                viewModel.deleteOperation = it
                buildDialog().show()
            },
            OperationLongClickListener {
                Toast.makeText(context, "LongCLICK , weeeeee", Toast.LENGTH_LONG).show()
                true
            })

        binding.fragmentHistoryList.adapter = adapter

        var list = listOf<Operation>()
        var filter = 0L


        viewModel.operations.observe(this, Observer {
            it?.let {
                list = it
                val filtered = list.filterByCategoryId(filter)
                adapter.addAndSubmitList(filtered)
                viewModel.budget.value =
                    filtered.filter { op -> op.toId == null }.sumByDouble { op -> op.sum }
            }
        })

        viewModel.categories.observe(this, Observer {
            it?.let {

            }
        })

        viewModel.categoryFilter.observe(this, Observer {
            it?.let {
                filter = it
                val filtered = list.filterByCategoryId(filter)
                adapter.addAndSubmitList(filtered)
                viewModel.budget.value =
                    filtered.filter { op -> op.toId == null }.sumByDouble { op -> op.sum }
            }
        })


        viewModel.floatingButtonClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                val popup = PopupMenu(context!!, fragment_history_fab)
                popup.inflate(R.menu.new_operation_menu)
                popup.setOnMenuItemClickListener { item ->
                    var res = -1
                    when (item.itemId) {
                        R.id.new_profit -> {
                            res = OperationType.PROFIT.id
                        }
                        R.id.new_expense -> {
                            res = OperationType.EXPENSE.id
                        }
                        R.id.new_remmitance -> {
                            res = OperationType.REMITTANCE.id
                        }

                    }
                    this.findNavController().navigate(
                        HistoryFragmentDirections
                            .actionHistoryFragmentToNewOperationFragment(res)
                    )
                    true
                }
                popup.setOnDismissListener {
                    viewModel.onStopClicking()
                }
                popup.show()
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }


    private fun List<Operation>.filterByCategoryId(id: Long) =
        if (id == 0L) this else this.filter { it.categoryId == id }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    val ALL_ID = -42
    val GROUP_ID = 42

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        if (menu.findItem(ALL_ID) == null) {
            menu.add(GROUP_ID, ALL_ID, Menu.NONE, "All")
        }
        viewModel.categories.value?.forEachIndexed { index, category ->
            if (menu.findItem(index) == null)
                menu.add(GROUP_ID, index, Menu.NONE, category.name)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.groupId == GROUP_ID) {
            if (item.itemId == ALL_ID) {
                viewModel.categoryFilter.value = 0L
            } else {
                viewModel.categories.value?.let {
                    viewModel.categoryFilter.value = viewModel.categories.value!![item.itemId].id
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildDialog() = AlertDialog.Builder(context!!).apply {
        setTitle(getString(R.string.delete_dialog_title))  // заголовок
        setMessage(getString(R.string.delete_op_dialog)) // сообщение
        setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.deleteOperation()
        }
        setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->

        }
        setCancelable(true)
    }
}

