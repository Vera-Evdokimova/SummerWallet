package com.pryanya.summerwallet.presentation.ops.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pryanya.summerwallet.data.entities.Operation
import com.pryanya.summerwallet.databinding.ItemOperationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistoryAdapter(
    val clickListener: OperationClickListener,
    val longClickListener: OperationLongClickListener
) : ListAdapter<Operation,
        HistoryAdapter.ViewHolder>(OperationDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.IO)

    fun addAndSubmitList(list: List<Operation>?) {
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

    class ViewHolder private constructor(private val binding: ItemOperationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            clickListener: OperationClickListener,
            longClickListener: OperationLongClickListener,
            item: Operation
        ) {
            binding.operation = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOperationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OperationDiffCallback : DiffUtil.ItemCallback<Operation>() {
    override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem == newItem
    }
}

class OperationClickListener(val clickListener: (operation: Operation) -> Unit) {
    fun onClick(operation: Operation) = clickListener(operation)
}

class OperationLongClickListener(val clickListener: (operation: Operation) -> Boolean) {
    fun onClick(operation: Operation) = clickListener(operation)
}
