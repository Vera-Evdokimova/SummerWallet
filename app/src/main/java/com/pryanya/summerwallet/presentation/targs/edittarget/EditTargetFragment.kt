package com.pryanya.summerwallet.presentation.targs.edittarget


import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.database.MoneyDatabase
import com.pryanya.summerwallet.data.repositories.Repository
import com.pryanya.summerwallet.databinding.FragmentEditTargetBinding
import java.util.*
import com.pryanya.summerwallet.utils.DecimalDigitsInputFilter


class EditTargetFragment : Fragment() {

    lateinit var viewModel: EditTargetViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = EditTargetViewModelFactory(
            repository,
            EditTargetFragmentArgs.fromBundle(arguments!!).targetId
        )
        viewModel = ViewModelProviders.of(this, factory).get(EditTargetViewModel::class.java)

        val binding: FragmentEditTargetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_target, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.buttonClick.observe(this, Observer {
            it?.let {
                if (viewModel.name.isNotEmpty()) {
                    this.findNavController()
                        .navigate(
                            EditTargetFragmentDirections
                                .actionEditTargetFragmentToTargetsFragment()
                        )
                    viewModel.updateTarget()
                } else {
                    Toast.makeText(context, getString(R.string.name_request), Toast.LENGTH_LONG)
                        .show()
                }
                viewModel.onStopClicking()
            }
        })

        binding.newTargSumEt.filters =
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


    private fun DatePicker.getDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }

}

