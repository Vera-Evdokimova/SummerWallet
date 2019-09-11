package com.pryanya.summerwallet.presentation.targs.targets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pryanya.summerwallet.R
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.databinding.ItemTargetBinding
import com.pryanya.summerwallet.utils.targetMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class TargetsAdapter(
    val clickListener: TargetClickListener,
    val longClickListener: TargetLongClickListener
) : ListAdapter<Account, TargetsAdapter.ViewHolder>(OperationDiffCallback()) {

    private val timeMs = System.currentTimeMillis()

    private val adapterScope = CoroutineScope(Dispatchers.IO)

    fun addAndSubmitList(list: List<Account>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, longClickListener, item, timeMs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemTargetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            clickListener: TargetClickListener,
            longClickListener: TargetLongClickListener,
            item: Account,
            timeMs: Long
        ) {
            binding.account = item
            binding.clickListener = clickListener
            binding.longClickListener = longClickListener
            binding.targetProgressbar.progress = item.calculateProgress()
            val state = item.state(timeMs)
            if (state != 0) {
                binding.targetMessageDaily.text = ""
            } else {
                binding.targetMessageDaily.text = item.calculateDailyPayment(timeMs)
            }
            binding.targetMessageText.targetMessage(state)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTargetBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}


class OperationDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }
}

class TargetClickListener(val clickListener: (account: Account) -> Unit) {
    fun onClick(account: Account) = clickListener(account)
}

class TargetLongClickListener(val clickListener: (account: Account) -> Boolean) {
    fun onClick(account: Account) = clickListener(account)
}

fun Account.calculateProgress() = (this.sum / this.targetSum!! * 100).toInt()

fun Account.calculateDailyPayment(timeMs: Long): String {
    val days = TimeUnit.DAYS.convert(this.endDate!! - timeMs, TimeUnit.MILLISECONDS) + 1
    val diff = targetSum!! - sum
    return String.format("%.2f P", diff / days)
}



fun Account.state(timeMs: Long): Int {
    val days = TimeUnit.DAYS.convert(this.endDate!! - timeMs, TimeUnit.MILLISECONDS) + 1
    val diff = targetSum!! - sum
    return when {
        diff <= 0 -> 1 //success
        days > 0 -> 0 // in progress
        else -> -1 //failed
    }
}