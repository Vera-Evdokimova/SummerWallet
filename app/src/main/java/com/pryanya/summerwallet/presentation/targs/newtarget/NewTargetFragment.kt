package com.pryanya.summerwallet.presentation.targs.newtarget

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
import com.pryanya.summerwallet.databinding.FragmentNewTargetBinding
import com.pryanya.summerwallet.utils.DecimalDigitsInputFilter
import java.util.*


class NewTargetFragment : Fragment() {

    lateinit var viewModel: NewTargetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val database = MoneyDatabase.getInstance(application)

        val repository = Repository(database)

        val factory = NewTargetViewModelFactory(
            repository
        )
        viewModel = ViewModelProviders.of(this, factory).get(NewTargetViewModel::class.java)


        val binding: FragmentNewTargetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_target, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        viewModel.buttonClick.observe(this, Observer {
            it?.let {
                if (!viewModel.name.isNullOrEmpty()) {
                    viewModel.addTarget()
                    this.findNavController()
                        .navigate(
                            NewTargetFragmentDirections
                                .actionNewTargetFragmentToTargetsFragment()
                        )
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
