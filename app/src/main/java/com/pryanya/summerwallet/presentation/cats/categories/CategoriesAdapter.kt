package com.pryanya.summerwallet.presentation.cats.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pryanya.summerwallet.data.entities.Category
import com.pryanya.summerwallet.databinding.ItemCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesAdapter(
    val clickListener: CategoryClickListener,
    val longClickListener: CategoryLongClickListener
) : ListAdapter<Category,
        CategoriesAdapter.ViewHolder>(CategoryDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.IO)

    fun addAndSubmitList(list: List<Category>?) {
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

    class ViewHolder private constructor(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            clickListener: CategoryClickListener,
            longClickListener: CategoryLongClickListener,
            item: Category
        ) {
            binding.category = item
            binding.clickListener = clickListener
            binding.longClickListener = longClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}


class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

class CategoryClickListener(val clickListener: (account: Category) -> Unit) {
    fun onClick(account: Category) = clickListener(account)
}

class CategoryLongClickListener(val clickListener: (account: Category) -> Boolean) {
    fun onClick(account: Category) = clickListener(account)
}