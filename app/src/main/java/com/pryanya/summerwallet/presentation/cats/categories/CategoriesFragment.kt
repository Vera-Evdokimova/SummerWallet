package com.pryanya.summerwallet.presentation.cats.categories


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = CategoryViewModelFactory(repository)

        viewModel =
            ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)


        val binding: FragmentCategoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = CategoriesAdapter(
            CategoryClickListener {
                this.findNavController().navigate(
                    CategoriesFragmentDirections
                        .actionCategoriesFragmentToEditCategoryFragment(it.id)
                )
            },
            CategoryLongClickListener {
                viewModel.categoryToDelete = it
                buildDialog().show()
                true
            }
        )

        binding.fragmentCategoriesList.adapter = adapter

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addAndSubmitList(it)
            }
        })

        viewModel.floatingButtonClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController()
                    .navigate(
                        CategoriesFragmentDirections
                            .actionCategoriesFragmentToNewCategoryFragment()
                    )
                viewModel.onStopClicking()
            }
        })
        return binding.root
    }

    private fun buildDialog() = AlertDialog.Builder(context!!).apply {
        setTitle(getString(R.string.delete_dialog_title))  // заголовок
        setMessage(getString(R.string.del_dialog_cat)) // сообщение
        setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.deleteCategory()
        }
        setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->

        }
        setCancelable(true)
    }


}
