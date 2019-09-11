package com.pryanya.summerwallet.presentation.accs.accounts


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pryanya.summerwallet.data.entities.Account
import com.pryanya.summerwallet.databinding.ItemAccountBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AccountsAdapter(
    val clickListener: AccountClickListener,
    val longClickListener: AccountLongClickListener
) : ListAdapter<Account,
        AccountsAdapter.ViewHolder>(OperationDiffCallback()) {


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
        holder.bind(clickListener, longClickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            clickListener: AccountClickListener,
            longClickListener: AccountLongClickListener,
            item: Account
        ) {
            binding.account = item
            binding.clickListener = clickListener
            binding.longClickListener = longClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAccountBinding.inflate(layoutInflater, parent, false)

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

class AccountClickListener(val clickListener: (account: Account) -> Unit) {
    fun onClick(account: Account) = clickListener(account)
}

class AccountLongClickListener(val clickListener: (account: Account) -> Boolean) {
    fun onClick(account: Account) = clickListener(account)
}